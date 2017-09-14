package com.dastanapps.dastanlib.chat.beans;

import org.jivesoftware.smack.packet.Message;

import static com.dastanapps.dastanlib.chat.DChatXMPP.XMPP_HOST;


/**
 * Created by DastanIqbal on 7/10/16.
 */

public class DChatMessage {
    private final Message message;
    private String msgId;
    private String to;
    //public String from;
    private String msg;
    private boolean isGroup;

    public DChatMessage(String to, String msg, boolean isGroup) {
        this.to = to + "@" + XMPP_HOST;
        //  this.from = from;
        this.msg = msg;
        this.isGroup = isGroup;
        message = new Message(this.to, Message.Type.chat);
        message.setBody(this.msg);
        msgId = message.getStanzaId();

    }

    public Message getChatMessage() {
        if (isGroup) {
            Message message = new Message(to, Message.Type.groupchat);
            message.setBody(msg);
            return message;
        } else {
            return message;
        }
    }
    public String getTo() {
        return to;
    }
    public String getMsgId() {
        return msgId;
    }
}
