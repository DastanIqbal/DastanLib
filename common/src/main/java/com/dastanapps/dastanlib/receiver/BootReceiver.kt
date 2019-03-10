package com.dastanapps.dastanlib.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager


class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val localSmsReciver = Intent(ReceiverFilter.BOOTCOMPLETE_RECEIVER)
        LocalBroadcastManager.getInstance(context).sendBroadcast(localSmsReciver)
    }
}
