package com.dastanapps.dastanlib.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class UpdateApp : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        SendBroadcast.AppUpdate(intent)
    }
}
