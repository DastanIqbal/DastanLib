package com.dastanapps.dastanlib.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager

import com.dastanapps.dastanlib.log.Logger

/**
 * Created by dastaniqbal on 16/12/2016.
 * ask2iqbal@gmail.com
 * 16/12/2016 3:38
 */

class SchedularBR : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Logger.d(TAG, "Schedular Called")
        val localSmsReciver = Intent(ReceiverFilter.OTP_SMS_RECEIVER)
        LocalBroadcastManager.getInstance(context).sendBroadcast(localSmsReciver)
    }

    companion object {
        private val TAG = SchedularBR::class.java.simpleName
    }
}
