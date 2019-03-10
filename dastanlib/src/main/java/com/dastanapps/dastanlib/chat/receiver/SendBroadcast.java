package com.dastanapps.dastanlib.chat.receiver;

import android.content.Intent;
import android.util.Log;

import com.dastanapps.dastanlib.DastanApp;

/**
 * Created by DastanIqbal on 20/10/16.
 */

public class SendBroadcast {
    private static final String TAG = SendBroadcast.class.getSimpleName();

    public static void sendConnectionState(DChatStates.ConnectionState connectionState) {
        DChatStates.sConnectionState = connectionState;
        Intent i = new Intent(CONNECTION_STATE);
        i.setPackage(DastanApp.getInstance().getPackageName());
        i.putExtra(CONNECTION, connectionState.name());
        DastanApp.getInstance().sendBroadcast(i);
        Log.d(TAG, "sendConnectionState: " + connectionState.name());
    }

    public static void sendMessageState(DChatStates.MessageState messageState) {
        DChatStates.sMessageState = messageState;
        Intent i = new Intent(MESSAGE_STATE);
        i.setPackage(DastanApp.getInstance().getPackageName());
        i.putExtra(CONNECTION, messageState.name());
        DastanApp.getInstance().sendBroadcast(i);
        Log.d(TAG, "sendMessageState: " + messageState.name());
    }

    public static final String CONNECTION_STATE = "com.zylahealth.chat.connectionstate";
    public static final String MESSAGE_STATE = "com.zylahealth.chat.messagestate";
    public static final String NEW_MESSAGE = "com.zylahealth.newmessage";
    public static final String CONNECTION = "connection";
    public static final String BUNDLE_MESSAGE_BODY = "b_body";
    public static final String BUNDLE_TO = "b_to";
    public static final String BUNDLE_FROM_JID = "b_from";

}
