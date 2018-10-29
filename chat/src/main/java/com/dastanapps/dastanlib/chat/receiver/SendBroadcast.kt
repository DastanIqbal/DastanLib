package com.dastanapps.dastanlib.chat.receiver

import android.content.Intent
import android.util.Log
import com.dastanapps.dastanlib.DastanLibApp


/**
 * Created by DastanIqbal on 20/10/16.
 */

object SendBroadcast {
    private val TAG = SendBroadcast::class.java.simpleName

    val CONNECTION_STATE = "com.zylahealth.chat.connectionstate"
    val MESSAGE_STATE = "com.zylahealth.chat.messagestate"
    val NEW_MESSAGE = "com.zylahealth.newmessage"
    val CONNECTION = "connection"
    val BUNDLE_MESSAGE_BODY = "b_body"
    val BUNDLE_TO = "b_to"
    val BUNDLE_FROM_JID = "b_from"

    fun sendConnectionState(connectionState: DChatStates.ConnectionState) {
        DChatStates.sConnectionState = connectionState
        val i = Intent(CONNECTION_STATE)
        i.setPackage(DastanLibApp.INSTANCE.packageName)
        i.putExtra(CONNECTION, connectionState.name)
        DastanLibApp.INSTANCE.sendBroadcast(i)
        Log.d(TAG, "sendConnectionState: " + connectionState.name)
    }

    fun sendMessageState(messageState: DChatStates.MessageState) {
        DChatStates.sMessageState = messageState
        val i = Intent(MESSAGE_STATE)
        i.setPackage(DastanLibApp.INSTANCE.packageName)
        i.putExtra(CONNECTION, messageState.name)
        DastanLibApp.INSTANCE.sendBroadcast(i)
        Log.d(TAG, "sendMessageState: " + messageState.name)
    }

}
