package com.dastanapps.dastanlib.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val localSmsReciver = Intent(ReceiverFilter.BOOTCOMPLETE_RECEIVER)
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(context).sendBroadcast(localSmsReciver)
    }
}
