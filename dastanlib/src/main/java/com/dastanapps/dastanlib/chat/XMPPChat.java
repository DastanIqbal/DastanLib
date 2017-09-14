package com.dastanapps.dastanlib.chat;

import android.content.BroadcastReceiver;
import android.util.Log;

import com.dastanapps.dastanlib.chat.base.ChatBase;
import com.dastanapps.dastanlib.chat.beans.DChatMessage;
import com.dastanapps.dastanlib.log.Logger;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.ChatStateListener;
import org.jivesoftware.smackx.chatstates.ChatStateManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;

import static com.dastanapps.dastanlib.chat.ChatErrorConstant.MESSAGE_EXCEPTION;


/**
 * Created by DastanIqbal on 7/10/16.
 */

public class XMPPChat extends ChatBase {
    private static final String TAG = XMPPChat.class.getSimpleName();
    private final ChatManager chatManager;
    protected final MultiUserChatManager multiChatUserMgr;
    private final DeliveryReceiptManager deliveryReceiptManager;
    private final ChatStateManager chatStateManager;
    private static IChatEvent iChatEvent;
    private Chat currentChat;
    private BroadcastReceiver uiThreadMessageReceiver;

    public XMPPChat() {
        Log.d(TAG, "XMPPChat Initialize");
        chatManager = ChatManager.getInstanceFor(dastanXMPP.getXmpptcpConnection());
        chatStateManager = ChatStateManager.getInstance(dastanXMPP.getXmpptcpConnection());
        deliveryReceiptManager = DeliveryReceiptManager.getInstanceFor(dastanXMPP.getXmpptcpConnection());
        deliveryReceiptManager.autoAddDeliveryReceiptRequests();
        deliveryReceiptManager.setAutoReceiptMode(DeliveryReceiptManager.AutoReceiptMode.always);

        chatManager.addChatListener(chatManagerListener);
        chatManager.addOutgoingMessageInterceptor(messageListener);
        deliveryReceiptManager.addReceiptReceivedListener(receiptReceivedListener);

        //Group Chat
        multiChatUserMgr = MultiUserChatManager.getInstanceFor(dastanXMPP.getXmpptcpConnection());

        StanzaFilter messageFilter = new StanzaTypeFilter(Message.class);
        dastanXMPP.getXmpptcpConnection().addAsyncStanzaListener(msgPacketListener, messageFilter);

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

    public void sendMessage(DChatMessage dchatmessage) {
        try {
            Message message = dchatmessage.getChatMessage();
            // Log.d(TAG, "Delivery Supported:::" + deliveryReceiptManager.isSupported(message.getTo()));
            if (message.getType() == Message.Type.chat) {
                currentChat = chatManager.createChat(message.getTo(), chatMessageListener);
                DeliveryReceiptRequest.addTo(message);
                currentChat.sendMessage(message);
            } else if (message.getType() == Message.Type.groupchat) {
                MultiUserChat multiUserChat = multiChatUserMgr.getMultiUserChat(message.getTo());
                if (multiUserChat.isJoined()) {
                    multiUserChat.sendMessage(message);
                }
            }
            //XMPP Server Ack
//            dastanXMPP.getXmpptcpConnection().addStanzaIdAcknowledgedListener(message.getStanzaId(), new StanzaListener() {
//                @Override
//                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
//                    Log.d(TAG, "Ack -> " + packet.toString());
//                }
//            });
        } catch (SmackException e) {
            if (isCallbackNull()) {
                iChatEvent.onError(MESSAGE_EXCEPTION, dchatmessage.getTo(), dchatmessage.getMsgId(), e.getMessage());
            }
            e.printStackTrace();
        }
    }

    public boolean isCallbackNull() {
        if (iChatEvent != null) {
            return true;
        } else {
            Logger.d(TAG, "Not Connected");
        }
        return false;
    }

    public void sendComposing(String to) {
        try {
            currentChat = chatManager.createChat(DChatXMPP.getJID(to), chatMessageListener);
            chatStateManager.setCurrentState(ChatState.composing, currentChat);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void sendOfflineMessage(String from, String msg) {
        if (isCallbackNull())
            iChatEvent.onIncomingMessage(from, msg);
    }

    public void addFriend(String accountId,String name) {
        DChatXMPP.getXMPPContacts().requestFriend(DChatXMPP.getJID(accountId), name);
    }

    public void setIChatEventListener(IChatEvent iChatEvent) {
        XMPPChat.iChatEvent = iChatEvent;
    }

    private ChatManagerListener chatManagerListener = new ChatManagerListener() {
        @Override
        public void chatCreated(Chat chat, boolean createdLocally) {
            Log.d(TAG, "ChatManagerListener:::chatCreated -> " + chat.toString());
            chat.addMessageListener(chatMessageListener);
        }

    };

    private ChatStateListener chatMessageListener = new ChatStateListener() {

        @Override
        public void stateChanged(Chat chat, ChatState state) {
            //State Change composing,active,paused,gone,etc
            Log.d(TAG, "ChatStateListener:::stateChanged -> " + chat.toString() + " \n -> " + state.toString());
            if (isCallbackNull())
                iChatEvent.onStateChanged(state.toString());
        }

        @Override
        public void processMessage(Chat chat, Message message) {
            //Incoming Message
            Log.d(TAG, "ChatStateListener:::processMessage -> " + chat.toString() + " \n -> " + message.toString());
            if (message.getBody() != null) {
                if (isCallbackNull())
                    iChatEvent.onIncomingMessage(message.getFrom(), message.getBody());
            }
        }
    };

    private MessageListener messageListener = new MessageListener() {
        @Override
        public void processMessage(Message message) {
            Log.d(TAG, "MessageListener:::processMessage -> " + message.toString());
        }
    };

    private final ReceiptReceivedListener receiptReceivedListener = new ReceiptReceivedListener() {
        @Override
        public void onReceiptReceived(String fromJid, String toJid, String receiptId, Stanza receipt) {
            Log.d(TAG, "ReceiptReceivedListener:::onReceiptReceived -> " + receipt.toString());
            if (isCallbackNull()) {
                Log.d(TAG, "From:" + fromJid +
                        " To:" + toJid +
                        " ReceiptId:" + receiptId +
                        " receiptError:" + receipt.getError() +
                        " stanzaId:" + receipt.getStanzaId());
                if (receipt.getError() != null) {
                    iChatEvent.onReceiptReceived(receipt.getTo(), receipt.getStanzaId(), true);
                } else {
                    iChatEvent.onReceiptReceived(receipt.getTo(), receipt.getStanzaId(), false);
                }
            }
        }

    };

    private StanzaListener msgPacketListener = new StanzaListener() {
        @Override
        public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
            Log.d(TAG, "StanzaListener:::processMessage -> " + packet.toString());
            if (packet.hasExtension(ChatStateManager.NAMESPACE)) {
                ExtensionElement extensionElement = packet.getExtension(ChatStateManager.NAMESPACE);
                if (extensionElement != null) {
                    ChatState state = ChatState.valueOf(extensionElement.getElementName());
                    if (isCallbackNull())
                        iChatEvent.onStateChanged(state.toString());
                }
                ExtensionElement deliveryElement = packet.getExtension(DeliveryReceipt.NAMESPACE);
                if (deliveryElement != null) {
                    if (isCallbackNull()) {
                        if (packet.getError() != null) {
                            if (packet instanceof Message) {
                                Message message = (Message) packet;
                                if (packet.getError() != null)
                                    iChatEvent.onError(ChatErrorConstant.MESSAGE_ERROR, message.getTo(),message.getStanzaId(), packet.getError().getCondition().name());
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
    };

    public interface IChatEvent {
        void onStateChanged(String chatState);

        void onIncomingMessage(String from, String message);

        void onReceiptReceived(String toJid, String msgId, boolean isError);

        void onError(ChatErrorConstant errorConstant, String to, String msgid, String error);
    }
}