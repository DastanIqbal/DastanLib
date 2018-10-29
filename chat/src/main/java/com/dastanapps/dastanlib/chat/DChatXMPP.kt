package com.dastanapps.dastanlib.chat

import android.text.TextUtils
import android.util.Log
import com.dastanapps.dastanlib.DastanChatApp
import com.dastanapps.dastanlib.DastanLibApp
import com.dastanapps.dastanlib.chat.receiver.DChatStates
import com.dastanapps.dastanlib.chat.receiver.SendBroadcast
import org.jivesoftware.smack.*
import org.jivesoftware.smack.chat.Chat
import org.jivesoftware.smack.chat.ChatManager
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.chatstates.ChatState
import org.jivesoftware.smackx.chatstates.ChatStateListener
import org.jivesoftware.smackx.offline.OfflineMessageManager
import org.jivesoftware.smackx.ping.android.ServerPingWithAlarmManager
import java.io.IOException

/**
 * Created by DastanIqbal on 5/10/16.
 */

class DChatXMPP {

    val isConnected: Boolean
        get() = if (xmpptcpConnection != null) xmpptcpConnection!!.isConnected else false

    val isAuthenticated: Boolean
        get() = if (xmpptcpConnection != null) xmpptcpConnection!!.isAuthenticated else false

    private val xmpptcpConnectionListener = object : ConnectionListener {

        override fun connected(connection: org.jivesoftware.smack.XMPPConnection) {
            Log.d(TAG, "Connected")
            if (!connection.isAuthenticated) {
                try {
                    xmpptcpConnection!!.login()
                } catch (e: XMPPException) {
                    SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED)
                    e.printStackTrace()
                } catch (e: SmackException) {
                    SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED)
                    e.printStackTrace()
                } catch (e: IOException) {
                    SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED)
                    e.printStackTrace()
                }

            } else {
                SendBroadcast.sendConnectionState(DChatStates.ConnectionState.CONNECTED)
            }
        }

        override fun authenticated(connection: org.jivesoftware.smack.XMPPConnection, resumed: Boolean) {
            Log.d(TAG, "authenticated")
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.AUTHENTICATED)
            loadOfflineMessages()
            availablePresence()
        }

        override fun connectionClosed() {
            Log.d(TAG, "connectionClosed")
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED)
        }

        override fun connectionClosedOnError(e: Exception) {
            Log.d(TAG, "connectionClosedOnError: " + e.message)
            e.printStackTrace()
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED)
            //            reset();
            //            if(Util.isConnectingToInternet(DastanApp.getInstance())){
            //                configXMPP();
            //            }
        }

        override fun reconnectionSuccessful() {
            Log.d(TAG, "reconnectionSuccessful")
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.AUTHENTICATED)
            loadOfflineMessages()
            availablePresence()
        }

        override fun reconnectingIn(seconds: Int) {
            Log.d(TAG, "reconnectingIn $seconds")
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.CONNECTING)
        }

        override fun reconnectionFailed(e: Exception) {
            Log.d(TAG, "reconnectionFailed: " + e.message)
            e.printStackTrace()
            unavailablePresence()
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED)
        }
    }

    fun getXmpptcpConnection(): XMPPTCPConnection? {
        //No need to intialize if Its null then there is bug in code flow
        return xmpptcpConnection
    }

    fun configXMPP() {
        Log.d(TAG, "configXMPP")
        if (xmpptcpConnection != null && !xmpptcpConnection!!.isConnected) {
            reset()
        }
        //new AndroidSmackInitializer().initialize();
        val xmppbuilder = XMPPTCPConnectionConfiguration.builder()
        xmppbuilder.setResource(RESOURCE)
        xmppbuilder.setServiceName(XMPP_HOST)
        xmppbuilder.setHost(XMPP_HOST)
        xmppbuilder.setPort(5222)
        xmppbuilder.setUsernameAndPassword(DastanChatApp.INSTANCE.chatUser, DastanChatApp.INSTANCE.chatPwd)
        xmppbuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
        //if (BuildConfig.DEBUG) {
        xmppbuilder.setDebuggerEnabled(true)
        //}
        xmppbuilder.setConnectTimeout(60000)       //60 Sec

        xmpptcpConnection = XMPPTCPConnection(xmppbuilder.build())
        xmpptcpConnection!!.packetReplyTimeout = 30000 //30 sec
        xmpptcpConnection!!.addConnectionListener(xmpptcpConnectionListener)

        XMPPTCPConnection.setUseStreamManagementResumptionDefault(true)
        //XMPPTCPConnection.setUseStreamManagementDefault(true);

        ReconnectionManager.setEnabledPerDefault(true)
        ReconnectionManager.setDefaultFixedDelay(DEFAULT_RECONNECTION_DELAY)

        ServerPingWithAlarmManager.onCreate(DastanLibApp.INSTANCE)
        ServerPingWithAlarmManager.getInstanceFor(xmpptcpConnection).isEnabled = true

        initEngine()
    }

    private fun sendMessage(body: String, toJid: String) {
        Log.d(TAG, "Sending message to :$toJid")
        val chat = ChatManager.getInstanceFor(xmpptcpConnection)
                .createChat(toJid, object : ChatStateListener {
                    override fun stateChanged(chat: Chat, state: ChatState) {
                        Log.d(TAG, state.toString())
                    }

                    override fun processMessage(chat: Chat, message: Message) {
                        Log.d(TAG, message.toString())
                    }
                })
        try {
            chat.sendMessage(body)
        } catch (e: SmackException.NotConnectedException) {
            e.printStackTrace()
        }


    }

    private fun loadOfflineMessages() {
        val offlineMessageManager = OfflineMessageManager(xmpptcpConnection)
        try {
            val offlineMessage = offlineMessageManager.messages
            offlineMessageManager.supportsFlexibleRetrieval()
            for (message in offlineMessage) {
                if (!TextUtils.isEmpty(message.from) && !TextUtils.isEmpty(message.body)) {
                    xmppChat!!.sendOfflineMessage(message.from, message.body)
                }
            }
            offlineMessageManager.deleteMessages()
        } catch (e: SmackException.NoResponseException) {
            e.printStackTrace()
        } catch (e: XMPPException.XMPPErrorException) {
            e.printStackTrace()
        } catch (e: SmackException.NotConnectedException) {
            e.printStackTrace()
        }

    }

    fun connect() {
        try {
            if (!xmpptcpConnection!!.isConnected) {
                SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED)
                xmpptcpConnection!!.connect()
            } else {
                availablePresence()
                SendBroadcast.sendConnectionState(DChatStates.ConnectionState.CONNECTED)
                Log.d(TAG, "already connected")
            }
        } catch (e: SmackException) {
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED)
            e.printStackTrace()
        } catch (e: IOException) {
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED)
            e.printStackTrace()
        } catch (e: XMPPException) {
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED)
            e.printStackTrace()
        }

    }

    fun disconnect() {
        if (xmpptcpConnection != null) {
            unavailablePresence()
            ReconnectionManager.setDefaultFixedDelay(0)
            ReconnectionManager.getInstanceFor(xmpptcpConnection).disableAutomaticReconnection()
        }
    }

    private fun reset() {
        Log.d(TAG, "RESET")
        ServerPingWithAlarmManager.onDestroy()
        xmpptcpConnection!!.disconnect()
        xmpptcpConnection!!.instantShutdown()
        xmpptcpConnection = null
        xmppChat = null
        xmppContacts = null
        xmppGroupChat = null
    }

    private fun availablePresence() {
        val presence = Presence(Presence.Type.available)
        try {
            xmpptcpConnection!!.sendStanza(presence)
        } catch (e: SmackException.NotConnectedException) {
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED)
            e.printStackTrace()
        }

    }

    private fun unavailablePresence() {
        val presence = Presence(Presence.Type.unavailable)
        try {
            xmpptcpConnection!!.sendStanza(presence)
        } catch (e: SmackException.NotConnectedException) {
            SendBroadcast.sendConnectionState(DChatStates.ConnectionState.DISCONNECTED)
            e.printStackTrace()
        }

    }

    companion object {
        private val TAG = DChatXMPP::class.java.simpleName
        private val DEFAULT_RECONNECTION_DELAY = 5 // sec
        private val DEFAULT_SEND_MESSAGE_TIMEOUT = 5 // sec
        val XMPP_HOST = "im.zylahealth.com"
        val RESOURCE = "zyla_android"
        private var xmpptcpConnection: XMPPTCPConnection? = null
        var xmppChat: XMPPChat? = null
            private set
        var xmppContacts: XMPPContacts? = null
            private set
        var xmppGroupChat: XMPPGroupChat? = null
            private set


        fun getJID(accountId: String): String {
            return "$accountId@$XMPP_HOST"
        }

        fun initEngine() {
            xmppChat = XMPPChat()
            xmppContacts = XMPPContacts()
            //xmppGroupChat = new XMPPGroupChat();
        }
    }

}
