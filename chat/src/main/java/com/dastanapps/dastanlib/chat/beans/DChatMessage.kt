package com.dastanapps.dastanlib.chat.beans

import com.dastanapps.dastanlib.chat.DChatXMPP.XMPP_HOST
import org.jivesoftware.smack.packet.Message


/**
 * Created by DastanIqbal on 7/10/16.
 */

class DChatMessage(to: String, //public String from;
                   private val msg: String, private val isGroup: Boolean) {
    private val message: Message
    val msgId: String
    val to: String = "$to@$XMPP_HOST"

    val chatMessage: Message
        get() {
            return if (isGroup) {
                val message = Message(to, Message.Type.groupchat)
                message.body = msg
                message
            } else {
                message
            }
        }

    init {
        message = Message(this.to, Message.Type.chat)
        message.body = this.msg
        msgId = message.stanzaId

    }//  this.from = from;
}
