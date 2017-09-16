package com.dastanapps.dastanlib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;


public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent localSmsReciver = new Intent(ReceiverFilter.BOOTCOMPLETE_RECEIVER);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localSmsReciver);
    }
}
