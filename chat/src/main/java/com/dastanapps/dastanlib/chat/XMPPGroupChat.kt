package com.dastanapps.dastanlib.chat

import android.util.Log
import com.dastanapps.dastanlib.DastanChatApp
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smackx.muc.*

/**
 * Created by DastanIqbal on 17/10/16.
 */

class XMPPGroupChat : XMPPChat() {
    private val TAG = XMPPGroupChat::class.java.simpleName

    private val mMultiUserChat: MultiUserChat? = multiChatUserMgr.getMultiUserChat(DChatXMPP.getJID(DastanChatApp.INSTANCE.chatUser!!))

    private val invitationListener = InvitationListener { conn, room, inviter, reason, password, message -> Log.d(TAG, "invitationReceived:" + room.toString() + ": " + inviter + " : " + reason + ": " + password + ": " + message.toString()) }
    private val invitationRejectionListener = InvitationRejectionListener { invitee, reason -> Log.d(TAG, "invitationDeclined:$invitee : $reason") }
    private val groupMessageListener = MessageListener { message -> Log.d(TAG, "processMessage:" + message.toString()) }
    private val subjectUpdateChangeListener = SubjectUpdatedListener { subject, from -> Log.d(TAG, "subjectUpdated:$subject: $from") }
    private val userStatusListener = object : UserStatusListener {
        override fun kicked(actor: String, reason: String) {
            Log.d(TAG, "kicked:$actor: $reason")
        }

        override fun voiceGranted() {
            Log.d(TAG, "voiceGranted")
        }

        override fun voiceRevoked() {
            Log.d(TAG, "voiceGranted")
        }

        override fun banned(actor: String, reason: String) {
            Log.d(TAG, "banned:$actor: $reason")
        }

        override fun membershipGranted() {
            Log.d(TAG, "membershipGranted")
        }

        override fun membershipRevoked() {
            Log.d(TAG, "membershipRevoked")
        }

        override fun moderatorGranted() {
            Log.d(TAG, "moderatorGranted")
        }

        override fun moderatorRevoked() {
            Log.d(TAG, "moderatorRevoked")
        }

        override fun ownershipGranted() {
            Log.d(TAG, "ownershipGranted")
        }

        override fun ownershipRevoked() {
            Log.d(TAG, "ownershipRevoked")
        }

        override fun adminGranted() {
            Log.d(TAG, "adminGranted")
        }

        override fun adminRevoked() {
            Log.d(TAG, "adminRevoked")
        }
    }
    private val participantStatusListener = object : ParticipantStatusListener {
        override fun joined(participant: String) {
            Log.d(TAG, "joined:$participant")
        }

        override fun left(participant: String) {
            Log.d(TAG, "left:$participant")
        }

        override fun kicked(participant: String, actor: String, reason: String) {
            Log.d(TAG, "kicked:$participant: $actor: $reason")
        }

        override fun voiceGranted(participant: String) {
            Log.d(TAG, "voiceGranted:$participant")
        }

        override fun voiceRevoked(participant: String) {
            Log.d(TAG, "voiceRevoked:$participant")
        }

        override fun banned(participant: String, actor: String, reason: String) {
            Log.d(TAG, "banned:$participant: $actor: $reason")
        }

        override fun membershipGranted(participant: String) {
            Log.d(TAG, "membershipGranted:$participant")
        }

        override fun membershipRevoked(participant: String) {
            Log.d(TAG, "membershipRevoked:$participant")
        }

        override fun moderatorGranted(participant: String) {
            Log.d(TAG, "moderatorGranted:$participant")
        }

        override fun moderatorRevoked(participant: String) {
            Log.d(TAG, "moderatorRevoked:$participant")
        }

        override fun ownershipGranted(participant: String) {
            Log.d(TAG, "ownershipGranted:$participant")
        }

        override fun ownershipRevoked(participant: String) {
            Log.d(TAG, "ownershipRevoked:$participant")
        }

        override fun adminGranted(participant: String) {
            Log.d(TAG, "adminGranted:$participant")
        }

        override fun adminRevoked(participant: String) {
            Log.d(TAG, "adminRevoked:$participant")
        }

        override fun nicknameChanged(participant: String, newNickname: String) {
            Log.d(TAG, "nicknameChanged:$participant: $newNickname")
        }
    }

    init {
        multiChatUserMgr.addInvitationListener(invitationListener)
        mMultiUserChat!!.addInvitationRejectionListener(invitationRejectionListener)
        mMultiUserChat.addMessageListener(groupMessageListener)
        mMultiUserChat.addParticipantStatusListener(participantStatusListener)
        mMultiUserChat.addSubjectUpdatedListener(subjectUpdateChangeListener)
        mMultiUserChat.addUserStatusListener(userStatusListener)

        try {
            Log.d(TAG, "GroupChat Supported:::" + multiChatUserMgr.isServiceEnabled(DChatXMPP.getJID(DastanChatApp.INSTANCE.chatUser!!) + "/" + DChatXMPP.RESOURCE))
        } catch (e: SmackException.NoResponseException) {
            e.printStackTrace()
        } catch (e: XMPPException.XMPPErrorException) {
            e.printStackTrace()
        } catch (e: SmackException.NotConnectedException) {
            e.printStackTrace()
        }

        getJoinedRoom()
    }

    fun createGroup(groupName: String, nickName: String) {
        if (mMultiUserChat == null) {
            throw NullPointerException("Create XMPPGroupChat Object First")
        }
        try {
            // Create the XMPP address (JID) of the MUC.
            mMultiUserChat.create(groupName)
            //mMultiUserChat.changeSubject(groupName);
            //  mMultiUserChat.sendConfigurationForm(new Form(DataForm.Type.submit));
            mMultiUserChat.join(groupName)
        } catch (e: XMPPException.XMPPErrorException) {
            e.printStackTrace()
        } catch (e: SmackException) {
            e.printStackTrace()
        }

    }

    fun joinGroup(nickName: String) {
        if (mMultiUserChat == null) {
            throw NullPointerException("Create XMPPGroupChat Object First")
        }
        try {
            mMultiUserChat.join(nickName)
        } catch (e: SmackException.NoResponseException) {
            e.printStackTrace()
        } catch (e: XMPPException.XMPPErrorException) {
            e.printStackTrace()
        } catch (e: SmackException.NotConnectedException) {
            e.printStackTrace()
        }

    }

    fun getJoinedRoom() {
        val set = multiChatUserMgr.joinedRooms
        for (grpName in set) {
            Log.d(TAG, "GroupJoined$grpName")
        }
    }

    fun leaveGroup() {
        if (mMultiUserChat == null) {
            throw NullPointerException("Create XMPPGroupChat Object First")
        }
        try {
            mMultiUserChat.leave()
        } catch (e: SmackException.NotConnectedException) {
            e.printStackTrace()
        }

    }

    fun sendInvite(accountId: String) {
        if (mMultiUserChat == null) {
            throw NullPointerException("Create XMPPGroupChat Object First")
        }
        try {
            mMultiUserChat.invite(DChatXMPP.getJID(accountId) + "/" + DChatXMPP.RESOURCE, "Sent Invitation")
        } catch (e: SmackException.NotConnectedException) {
            e.printStackTrace()
        }

    }

    fun changeSubject(subjName: String) {
        if (mMultiUserChat == null) {
            throw NullPointerException("Create XMPPGroupChat Object First")
        }
        try {
            mMultiUserChat.changeSubject(subjName)
        } catch (e: SmackException.NotConnectedException) {
            e.printStackTrace()
        } catch (e: XMPPException.XMPPErrorException) {
            e.printStackTrace()
        } catch (e: SmackException.NoResponseException) {
            e.printStackTrace()
        }

    }

    fun blockUser(accountId: String) {
        if (mMultiUserChat == null) {
            throw NullPointerException("Create XMPPGroupChat Object First")
        }
        try {
            mMultiUserChat.banUser(accountId, "Blocked")
        } catch (e: XMPPException.XMPPErrorException) {
            e.printStackTrace()
        } catch (e: SmackException.NoResponseException) {
            e.printStackTrace()
        } catch (e: SmackException.NotConnectedException) {
            e.printStackTrace()
        }

    }

    fun blockUsers(accountIds: Collection<String>) {
        if (mMultiUserChat == null) {
            throw NullPointerException("Create XMPPGroupChat Object First")
        }
        try {
            mMultiUserChat.banUsers(accountIds)
        } catch (e: XMPPException.XMPPErrorException) {
            e.printStackTrace()
        } catch (e: SmackException.NoResponseException) {
            e.printStackTrace()
        } catch (e: SmackException.NotConnectedException) {
            e.printStackTrace()
        }

    }

    fun getMembers() {
        if (mMultiUserChat == null) {
            throw NullPointerException("Create XMPPGroupChat Object First")
        }
        try {
            mMultiUserChat.admins
            mMultiUserChat.participants
            mMultiUserChat.moderators
        } catch (e: SmackException.NoResponseException) {
            e.printStackTrace()
        } catch (e: XMPPException.XMPPErrorException) {
            e.printStackTrace()
        } catch (e: SmackException.NotConnectedException) {
            e.printStackTrace()
        }

    }

    fun grantAdmin(accountId: String) {
        try {
            mMultiUserChat!!.grantAdmin(accountId)
        } catch (e: XMPPException.XMPPErrorException) {
            e.printStackTrace()
        } catch (e: SmackException.NoResponseException) {
            e.printStackTrace()
        } catch (e: SmackException.NotConnectedException) {
            e.printStackTrace()
        }

    }

    fun grantAdmins(accountIds: Collection<String>) {
        try {
            mMultiUserChat!!.grantAdmin(accountIds)
        } catch (e: XMPPException.XMPPErrorException) {
            e.printStackTrace()
        } catch (e: SmackException.NoResponseException) {
            e.printStackTrace()
        } catch (e: SmackException.NotConnectedException) {
            e.printStackTrace()
        }

    }
}
