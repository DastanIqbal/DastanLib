package com.dastanapps.dastanlib.chat;

import android.util.Log;

import com.dastanapps.dastanlib.DastanApp;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;
import org.jivesoftware.smackx.muc.SubjectUpdatedListener;
import org.jivesoftware.smackx.muc.UserStatusListener;

import java.util.Collection;
import java.util.Set;

/**
 * Created by DastanIqbal on 17/10/16.
 */

public class XMPPGroupChat extends XMPPChat {

    private final String TAG = XMPPGroupChat.class.getSimpleName();
    private MultiUserChat mMultiUserChat;

    public XMPPGroupChat() {
        super();
        mMultiUserChat = multiChatUserMgr.getMultiUserChat(DChatXMPP.getJID(DastanApp.getChatUser()));
        multiChatUserMgr.addInvitationListener(invitationListener);
        mMultiUserChat.addInvitationRejectionListener(invitationRejectionListener);
        mMultiUserChat.addMessageListener(groupMessageListener);
        mMultiUserChat.addParticipantStatusListener(participantStatusListener);
        mMultiUserChat.addSubjectUpdatedListener(subjectUpdateChangeListener);
        mMultiUserChat.addUserStatusListener(userStatusListener);

        try {
            Log.d(TAG, "GroupChat Supported:::" + multiChatUserMgr.isServiceEnabled(DChatXMPP.getJID(DastanApp.getChatUser()) + "/" + DChatXMPP.RESOURCE));
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        getJoinedRoom();
    }

    public void createGroup(String groupName, String nickName) {
        if (mMultiUserChat == null) {
            throw new NullPointerException("Create XMPPGroupChat Object First");
        }
        try {
            // Create the XMPP address (JID) of the MUC.
            mMultiUserChat.create(groupName);
            //mMultiUserChat.changeSubject(groupName);
            //  mMultiUserChat.sendConfigurationForm(new Form(DataForm.Type.submit));
            mMultiUserChat.join(groupName);
        } catch (XMPPException.XMPPErrorException | SmackException e) {
            e.printStackTrace();
        }
    }

    public void joinGroup(String nickName) {
        if (mMultiUserChat == null) {
            throw new NullPointerException("Create XMPPGroupChat Object First");
        }
        try {
            mMultiUserChat.join(nickName);
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void getJoinedRoom() {
        Set<String> set = multiChatUserMgr.getJoinedRooms();
        for (String grpName : set) {
            Log.d(TAG, "GroupJoined" + grpName);
        }
    }

    public void leaveGroup() {
        if (mMultiUserChat == null) {
            throw new NullPointerException("Create XMPPGroupChat Object First");
        }
        try {
            mMultiUserChat.leave();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void sendInvite(String accountId) {
        if (mMultiUserChat == null) {
            throw new NullPointerException("Create XMPPGroupChat Object First");
        }
        try {
            mMultiUserChat.invite(DChatXMPP.getJID(accountId) + "/" + DChatXMPP.RESOURCE, "Sent Invitation");
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void changeSubject(String subjName) {
        if (mMultiUserChat == null) {
            throw new NullPointerException("Create XMPPGroupChat Object First");
        }
        try {
            mMultiUserChat.changeSubject(subjName);
        } catch (SmackException.NotConnectedException | XMPPException.XMPPErrorException | SmackException.NoResponseException e) {
            e.printStackTrace();
        }
    }

    public void blockUser(String accountId) {
        if (mMultiUserChat == null) {
            throw new NullPointerException("Create XMPPGroupChat Object First");
        }
        try {
            mMultiUserChat.banUser(accountId, "Blocked");
        } catch (XMPPException.XMPPErrorException | SmackException.NoResponseException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void blockUsers(Collection<String> accountIds) {
        if (mMultiUserChat == null) {
            throw new NullPointerException("Create XMPPGroupChat Object First");
        }
        try {
            mMultiUserChat.banUsers(accountIds);
        } catch (XMPPException.XMPPErrorException | SmackException.NoResponseException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void getMembers() {
        if (mMultiUserChat == null) {
            throw new NullPointerException("Create XMPPGroupChat Object First");
        }
        try {
            mMultiUserChat.getAdmins();
            mMultiUserChat.getParticipants();
            mMultiUserChat.getModerators();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void grantAdmin(String accountId) {
        try {
            mMultiUserChat.grantAdmin(accountId);
        } catch (XMPPException.XMPPErrorException | SmackException.NoResponseException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void grantAdmins(Collection<String> accountIds) {
        try {
            mMultiUserChat.grantAdmin(accountIds);
        } catch (XMPPException.XMPPErrorException | SmackException.NoResponseException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    private InvitationListener invitationListener = new InvitationListener() {
        @Override
        public void invitationReceived(XMPPConnection conn, MultiUserChat room, String inviter, String reason, String password, Message message) {
            Log.d(TAG, "invitationReceived:" + room.toString() + ": " + inviter + " : " + reason + ": " + password + ": " + message.toString());
        }
    };
    private InvitationRejectionListener invitationRejectionListener = new InvitationRejectionListener() {
        @Override
        public void invitationDeclined(String invitee, String reason) {
            Log.d(TAG, "invitationDeclined:" + invitee + " : " + reason);
        }
    };
    private MessageListener groupMessageListener = new MessageListener() {
        @Override
        public void processMessage(Message message) {
            Log.d(TAG, "processMessage:" + message.toString());
        }
    };
    private SubjectUpdatedListener subjectUpdateChangeListener = new SubjectUpdatedListener() {
        @Override
        public void subjectUpdated(String subject, String from) {
            Log.d(TAG, "subjectUpdated:" + subject + ": " + from);
        }
    };
    private UserStatusListener userStatusListener = new UserStatusListener() {
        @Override
        public void kicked(String actor, String reason) {
            Log.d(TAG, "kicked:" + actor + ": " + reason);
        }

        @Override
        public void voiceGranted() {
            Log.d(TAG, "voiceGranted");
        }

        @Override
        public void voiceRevoked() {
            Log.d(TAG, "voiceGranted");
        }

        @Override
        public void banned(String actor, String reason) {
            Log.d(TAG, "banned:" + actor + ": " + reason);
        }

        @Override
        public void membershipGranted() {
            Log.d(TAG, "membershipGranted");
        }

        @Override
        public void membershipRevoked() {
            Log.d(TAG, "membershipRevoked");
        }

        @Override
        public void moderatorGranted() {
            Log.d(TAG, "moderatorGranted");
        }

        @Override
        public void moderatorRevoked() {
            Log.d(TAG, "moderatorRevoked");
        }

        @Override
        public void ownershipGranted() {
            Log.d(TAG, "ownershipGranted");
        }

        @Override
        public void ownershipRevoked() {
            Log.d(TAG, "ownershipRevoked");
        }

        @Override
        public void adminGranted() {
            Log.d(TAG, "adminGranted");
        }

        @Override
        public void adminRevoked() {
            Log.d(TAG, "adminRevoked");
        }
    };
    private ParticipantStatusListener participantStatusListener = new ParticipantStatusListener() {
        @Override
        public void joined(String participant) {
            Log.d(TAG, "joined:" + participant);
        }

        @Override
        public void left(String participant) {
            Log.d(TAG, "left:" + participant);
        }

        @Override
        public void kicked(String participant, String actor, String reason) {
            Log.d(TAG, "kicked:" + participant + ": " + actor + ": " + reason);
        }

        @Override
        public void voiceGranted(String participant) {
            Log.d(TAG, "voiceGranted:" + participant);
        }

        @Override
        public void voiceRevoked(String participant) {
            Log.d(TAG, "voiceRevoked:" + participant);
        }

        @Override
        public void banned(String participant, String actor, String reason) {
            Log.d(TAG, "banned:" + participant + ": " + actor + ": " + reason);
        }

        @Override
        public void membershipGranted(String participant) {
            Log.d(TAG, "membershipGranted:" + participant);
        }

        @Override
        public void membershipRevoked(String participant) {
            Log.d(TAG, "membershipRevoked:" + participant);
        }

        @Override
        public void moderatorGranted(String participant) {
            Log.d(TAG, "moderatorGranted:" + participant);
        }

        @Override
        public void moderatorRevoked(String participant) {
            Log.d(TAG, "moderatorRevoked:" + participant);
        }

        @Override
        public void ownershipGranted(String participant) {
            Log.d(TAG, "ownershipGranted:" + participant);
        }

        @Override
        public void ownershipRevoked(String participant) {
            Log.d(TAG, "ownershipRevoked:" + participant);
        }

        @Override
        public void adminGranted(String participant) {
            Log.d(TAG, "adminGranted:" + participant);
        }

        @Override
        public void adminRevoked(String participant) {
            Log.d(TAG, "adminRevoked:" + participant);
        }

        @Override
        public void nicknameChanged(String participant, String newNickname) {
            Log.d(TAG, "nicknameChanged:" + participant + ": " + newNickname);
        }
    };
}
