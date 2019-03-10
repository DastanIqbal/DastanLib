package com.dastanapps.dastanlib.chat;

import android.text.TextUtils;
import android.util.Log;

import com.dastanapps.dastanlib.DastanApp;
import com.dastanapps.dastanlib.chat.receiver.DChatStates;
import com.dastanapps.dastanlib.chat.receiver.SendBroadcast;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.ChatStateListener;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.jivesoftware.smackx.ping.android.ServerPingWithAlarmManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by DastanIqbal on 5/10/16.
 */

public class DChatXMPP {
    private static final String TAG = DChatXMPP.class.getSimpleName();
    private static final int DEFAULT_RECONNECTION_DELAY = 5; // sec
    private static final int DEFAULT_SEND_MESSAGE_TIMEOUT = 5; // sec
    public static final String XMPP_HOST = "im.zylahealth.com";
    public static final String RESOURCE = "zyla_android";
    private static XMPPTCPConnection xmpptcpConnection;
    private static XMPPChat xmppChat;
    private static XMPPContacts xmppContacts;
    private static XMPPGroupChat xmppGroupChat;


    public static String getJID(String accountId) {
        return accountId + "@" + XMPP_HOST;
    }

    public static XMPPChat getXMPPChat() {
        return xmppChat;
    }

    public static XMPPContacts getXMPPContacts() {
        return xmppContacts;
    }

    public static XMPPGroupChat getXmppGroupChat() {
        return xmppGroupChat;
    }

    protected XMPPTCPConnection getXmpptcpConnection() {
        //No need to intialize if Its null then there is bug in code flow
        return xmpptcpConnection;
    }

    public DChatXMPP() {
    }

    public static void initEngine() {
        xmppChat = new XMPPChat();
        xmppContacts = new XMPPContacts();
        //xmppGroupChat = new XMPPGroupChat();
    }

    public void configXMPP() {
        Log.d(TAG, "configXMPP");
        if (xmpptcpConnection != null && !xmpptcpConnection.isConnected()) {
            reset();
        }
        //new AndroidSmackInitializer().initialize();
        XMPPTCPConnectionConfiguration.Builder xmppbuilder = XMPPTCPConnectionConfiguration.builder();
        xmppbuilder.setResource(RESOURCE);
        xmppbuilder.setServiceName(XMPP_HOST);
        xmppbuilder.setHost(XMPP_HOST);
        xmppbuilder.setPort(5222);
        xmppbuilder.setUsernameAndPassword(DastanApp.getChatUser(), DastanApp.getChatPwd());
        xmppbuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        //if (BuildConfig.DEBUG) {
            xmppbuilder.setDebuggerEnabled(true);
        //}
        xmppbuilder.setConnectTimeout(60000);       //60 Sec

        xmpptcpConnection = new XMPPTCPConnection(xmppbuilder.build());
        xmpptcpConnection.setPacketReplyTimeout(30000); //30 sec
        xmpptcpConnection.addConnectionListener(xmpptcpConnectionListener);

        XMPPTCPConnection.setUseStreamManagementResumptionDefault(true);
        //XMPPTCPConnection.setUseStreamManagementDefault(true);

        ReconnectionManager.setEnabledPerDefault(true);
        ReconnectionManager.setDefaultFixedDelay(DEFAULT_RECONNECTION_DELAY);

        ServerPingWithAlarmManager.onCreate(DastanApp.getInstance());
        ServerPingWithAlarmManager.getInstanceFor(xmpptcpConnection).setEnabled(true);

        initEngine();
    }

    private void sendMessage(String body, String toJid) {
        Log.d(TAG, "Sending message to :" + toJid);
        Chat chat = ChatManager.getInstanceFor(xmpptcpConnection)
                .createChat(toJid, new ChatStateListener() {
                    @Override
                    public void stateChanged(Chat chat, ChatState state) {
                        Log.d(TAG, state.toString());
                    }

                    @Override
                    public void processMessage(Chat chat, Message message) {
                        Log.d(TAG, message.toString());
                    }
                });
        try {
            chat.sendMessage(body);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }


    }

    private void loadOfflineMessages() {
        OfflineMessageManager offlineMessageManager = new OfflineMessageManager(xmpptcpConnection);
        try {
            List<Message> offlineMessage = offlineMessageManager.getMessages();
            offlineMessageManager.supportsFlexibleRetrieval();
            for (Message message : offlineMessage) {
                if (!TextUtils.isEmpty(message.getFrom())
                        && !TextUtils.isEmpty(message.getBody())) {
                    xmppChat.sendOfflineMessage(message.getFrom(), message.getBody());
                }
            }
            offlineMessageManager.deleteMessages();
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            if (!xmpptcpConnection.isConnected()) {
                SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED);
                xmpptcpConnection.connect();
            } else {
                availablePresence();
                SendBroadcast.sendConnectionState(DChatStates.ConnectionState.CONNECTED);
                Log.d(TAG, "already connected");
            }
        } catch (SmackException | IOException | XMPPException e) {
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED);
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        if (xmpptcpConnection != null)
            return xmpptcpConnection.isConnected();
        return false;
    }

    public boolean isAuthenticated() {
        if (xmpptcpConnection != null)
            return xmpptcpConnection.isAuthenticated();
        return false;
    }

    public void disconnect() {
        if (xmpptcpConnection != null) {
            unavailablePresence();
            ReconnectionManager.setDefaultFixedDelay(0);
            ReconnectionManager.getInstanceFor(xmpptcpConnection).disableAutomaticReconnection();
        }
    }

    private void reset() {
        Log.d(TAG, "RESET");
        ServerPingWithAlarmManager.onDestroy();
        xmpptcpConnection.disconnect();
        xmpptcpConnection.instantShutdown();
        xmpptcpConnection = null;
        xmppChat = null;
        xmppContacts = null;
        xmppGroupChat = null;
    }

    private ConnectionListener xmpptcpConnectionListener = new ConnectionListener() {

        @Override
        public void connected(org.jivesoftware.smack.XMPPConnection connection) {
            Log.d(TAG, "Connected");
            if (!connection.isAuthenticated()) {
                try {
                    xmpptcpConnection.login();
                } catch (XMPPException | SmackException | IOException e) {
                    SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED);
                    e.printStackTrace();
                }
            } else {
                SendBroadcast.sendConnectionState(DChatStates.ConnectionState.CONNECTED);
            }
        }

        @Override
        public void authenticated(org.jivesoftware.smack.XMPPConnection connection, boolean resumed) {
            Log.d(TAG, "authenticated");
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.AUTHENTICATED);
            loadOfflineMessages();
            availablePresence();
        }

        @Override
        public void connectionClosed() {
            Log.d(TAG, "connectionClosed");
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED);
        }

        @Override
        public void connectionClosedOnError(Exception e) {
            Log.d(TAG, "connectionClosedOnError: " + e.getMessage());
            e.printStackTrace();
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED);
//            reset();
//            if(Util.isConnectingToInternet(DastanApp.getInstance())){
//                configXMPP();
//            }
        }

        @Override
        public void reconnectionSuccessful() {
            Log.d(TAG, "reconnectionSuccessful");
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.AUTHENTICATED);
            loadOfflineMessages();
            availablePresence();
        }

        @Override
        public void reconnectingIn(int seconds) {
            Log.d(TAG, "reconnectingIn " + seconds);
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.CONNECTING);
        }

        @Override
        public void reconnectionFailed(Exception e) {
            Log.d(TAG, "reconnectionFailed: " + e.getMessage());
            e.printStackTrace();
            unavailablePresence();
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED);
        }
    };

    private void availablePresence() {
        Presence presence = new Presence(Presence.Type.available);
        try {
            xmpptcpConnection.sendStanza(presence);
        } catch (SmackException.NotConnectedException e) {
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED);
            e.printStackTrace();
        }
    }

    private void unavailablePresence() {
        Presence presence = new Presence(Presence.Type.unavailable);
        try {
            xmpptcpConnection.sendStanza(presence);
        } catch (SmackException.NotConnectedException e) {
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED);
            e.printStackTrace();
        }
    }

}
