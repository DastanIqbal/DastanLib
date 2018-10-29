package com.dastanapps.dastanlib.chat

import android.content.BroadcastReceiver
import android.util.Log
import com.dastanapps.dastanlib.chat.ChatErrorConstant.MESSAGE_EXCEPTION
import com.dastanapps.dastanlib.chat.base.ChatBase
import com.dastanapps.dastanlib.chat.beans.DChatMessage
import com.dastanapps.dastanlib.log.Logger
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.StanzaListener
import org.jivesoftware.smack.chat.Chat
import org.jivesoftware.smack.chat.ChatManager
import org.jivesoftware.smack.chat.ChatManagerListener
import org.jivesoftware.smack.filter.StanzaTypeFilter
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smackx.chatstates.ChatState
import org.jivesoftware.smackx.chatstates.ChatStateListener
import org.jivesoftware.smackx.chatstates.ChatStateManager
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jivesoftware.smackx.receipts.DeliveryReceipt
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener


/**
 * Created by DastanIqbal on 7/10/16.
 */

open class XMPPChat : ChatBase() {
    private val chatManager: ChatManager
    protected val multiChatUserMgr: MultiUserChatManager
    private val deliveryReceiptManager: DeliveryReceiptManager
    private val chatStateManager: ChatStateManager
    private var currentChat: Chat? = null
    private val uiThreadMessageReceiver: BroadcastReceiver? = null

    val isCallbackNull: Boolean
        get() {
            if (iChatEvent != null) {
                return true
            } else {
                Logger.d(TAG, "Not Connected")
            }
            return false
        }

    private val chatManagerListener = ChatManagerListener { chat, createdLocally ->
        Log.d(TAG, "ChatManagerListener:::chatCreated -> " + chat.toString())
        chat.addMessageListener(chatMessageListener)
    }

    private val chatMessageListener = object : ChatStateListener {

        override fun stateChanged(chat: Chat, state: ChatState) {
            //State Change composing,active,paused,gone,etc
            Log.d(TAG, "ChatStateListener:::stateChanged -> " + chat.toString() + " \n -> " + state.toString())
            if (isCallbackNull)
                iChatEvent!!.onStateChanged(state.toString())
        }

        override fun processMessage(chat: Chat, message: Message) {
            //Incoming Message
            Log.d(TAG, "ChatStateListener:::processMessage -> " + chat.toString() + " \n -> " + message.toString())
            if (message.body != null) {
                if (isCallbackNull)
                    iChatEvent!!.onIncomingMessage(message.from, message.body)
            }
        }
    }

    private val messageListener = MessageListener { message -> Log.d(TAG, "MessageListener:::processMessage -> " + message.toString()) }

    private val receiptReceivedListener = ReceiptReceivedListener { fromJid, toJid, receiptId, receipt ->
        Log.d(TAG, "ReceiptReceivedListener:::onReceiptReceived -> " + receipt.toString())
        if (isCallbackNull) {
            Log.d(TAG, "From:" + fromJid +
                    " To:" + toJid +
                    " ReceiptId:" + receiptId +
                    " receiptError:" + receipt.error +
                    " stanzaId:" + receipt.stanzaId)
            if (receipt.error != null) {
                iChatEvent!!.onReceiptReceived(receipt.to, receipt.stanzaId, true)
            } else {
                iChatEvent!!.onReceiptReceived(receipt.to, receipt.stanzaId, false)
            }
        }
    }

    private val msgPacketListener = StanzaListener { packet ->
        Log.d(TAG, "StanzaListener:::processMessage -> " + packet.toString())
        if (packet.hasExtension(ChatStateManager.NAMESPACE)) {
            val extensionElement = packet.getExtension(ChatStateManager.NAMESPACE)
            if (extensionElement != null) {
                val state = ChatState.valueOf(extensionElement.elementName)
                if (isCallbackNull)
                    iChatEvent!!.onStateChanged(state.toString())
            }
            val deliveryElement = packet.getExtension(DeliveryReceipt.NAMESPACE)
            if (deliveryElement != null) {
                if (isCallbackNull) {
                    if (packet.error != null) {
                        if (packet is Message) {
                            if (packet.getError() != null)
                                iChatEvent!!.onError(ChatErrorConstant.MESSAGE_ERROR, packet.to, packet.stanzaId, packet.getError().condition.name)
                        }
                    }
                }
            }
        } else if (packet.hasExtension(DeliveryReceipt.NAMESPACE)) {
            //                ExtensionElement deliveryElement = packet.getExtension(DeliveryReceipt.NAMESPACE);
            //                if (deliveryElement != null) {
            //                    if (isCallbackNull()) {
            //                        if (packet.getError() != null) {
            //                            if (packet instanceof Message) {
            //                                Message message = (Message) packet;
            //                                iChatEvent.onError(ChatErrorConstant.MESSAGE_ERROR, message.getStanzaId(), packet.getError().getCondition().name());
            //                            }
            //                        }
            //                    }
            //                }
        }
    }

    init {
        Log.d(TAG, "XMPPChat Initialize")
        chatManager = ChatManager.getInstanceFor(dastanXMPP.getXmpptcpConnection())
        chatStateManager = ChatStateManager.getInstance(dastanXMPP.getXmpptcpConnection())
        deliveryReceiptManager = DeliveryReceiptManager.getInstanceFor(dastanXMPP.getXmpptcpConnection())
        deliveryReceiptManager.autoAddDeliveryReceiptRequests()
        deliveryReceiptManager.autoReceiptMode = DeliveryReceiptManager.AutoReceiptMode.always

        chatManager.addChatListener(chatManagerListener)
        chatManager.addOutgoingMessageInterceptor(messageListener)
        deliveryReceiptManager.addReceiptReceivedListener(receiptReceivedListener)

        //Group Chat
        multiChatUserMgr = MultiUserChatManager.getInstanceFor(dastanXMPP.getXmpptcpConnection())

        val messageFilter = StanzaTypeFilter(Message::class.java)
        dastanXMPP.getXmpptcpConnection()!!.addAsyncStanzaListener(msgPacketListener, messageFilter)

        //Set up the ui thread broadcast message receiver.
        //      setupUiThreadBroadCastMessageReceiver();
    }

    //    private void setupUiThreadBroadCastMessageReceiver() {
    //        uiThreadMessageReceiver = new BroadcastReceiver() {
    //            @Override
    //            public void onReceive(Context context, Intent intent) {
    //                //Check if the Intents purpose is to send the message.
    //                String action = intent.getAction();
    //                if (action.equals(DChatStates.MessageState.SEND)) {
    //                    //Send the message.
    //                    sendMessage(intent.getStringExtra(XMPPService.BUNDLE_MESSAGE_BODY),
    //                            intent.getStringExtra(XMPPService.BUNDLE_TO));
    //                }
    //            }
    //        };
    //
    //        IntentFilter filter = new IntentFilter();
    //        filter.addAction(XMPPService.SEND_MESSAGE);
    //        DastanApp.getInstance().registerReceiver(uiThreadMessageReceiver, filter);
    //
    //    }

    fun sendMessage(dchatmessage: DChatMessage) {
        try {
            val message = dchatmessage.chatMessage
            // Log.d(TAG, "Delivery Supported:::" + deliveryReceiptManager.isSupported(message.getTo()));
            if (message.type == Message.Type.chat) {
                currentChat = chatManager.createChat(message.to, chatMessageListener)
                DeliveryReceiptRequest.addTo(message)
                currentChat!!.sendMessage(message)
            } else if (message.type == Message.Type.groupchat) {
                val multiUserChat = multiChatUserMgr.getMultiUserChat(message.to)
                if (multiUserChat.isJoined) {
                    multiUserChat.sendMessage(message)
                }
            }
            //XMPP Server Ack
            //            dastanXMPP.getXmpptcpConnection().addStanzaIdAcknowledgedListener(message.getStanzaId(), new StanzaListener() {
            //                @Override
            //                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
            //                    Log.d(TAG, "Ack -> " + packet.toString());
            //                }
            //            });
        } catch (e: SmackException) {
            if (isCallbackNull) {
                iChatEvent!!.onError(MESSAGE_EXCEPTION, dchatmessage.to, dchatmessage.msgId, e.message!!)
            }
            e.printStackTrace()
        }

    }

    fun sendComposing(to: String) {
        try {
            currentChat = chatManager.createChat(DChatXMPP.getJID(to), chatMessageListener)
            chatStateManager.setCurrentState(ChatState.composing, currentChat!!)
        } catch (e: SmackException.NotConnectedException) {
            e.printStackTrace()
        }

    }

    fun sendOfflineMessage(from: String, msg: String) {
        if (isCallbackNull)
            iChatEvent!!.onIncomingMessage(from, msg)
    }

    fun addFriend(accountId: String, name: String) {
        DChatXMPP.xmppContacts?.requestFriend(DChatXMPP.getJID(accountId), name)
    }

    fun setIChatEventListener(iChatEvent: IChatEvent) {
        XMPPChat.iChatEvent = iChatEvent
    }

    interface IChatEvent {
        fun onStateChanged(chatState: String)

        fun onIncomingMessage(from: String, message: String)

        fun onReceiptReceived(toJid: String, msgId: String, isError: Boolean)

        fun onError(errorConstant: ChatErrorConstant, to: String, msgid: String, error: String)
    }

    companion object {
        private val TAG = XMPPChat::class.java.simpleName
        private var iChatEvent: IChatEvent? = null
    }
}