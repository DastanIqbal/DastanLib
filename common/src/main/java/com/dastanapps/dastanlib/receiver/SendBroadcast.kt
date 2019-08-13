package com.dastanapps.dastanlib.receiver

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager

import com.dastanapps.dastanlib.DastanLibApp
import com.dastanapps.dastanlib.utils.R


/**
 * Created by dastaniqbal on 09/01/2017.
 * 09/01/2017 11:53
 */

object SendBroadcast {
    var APP_UPDATE = "app_update"
    var GPlayS_Missing = "gplayservice_missing"
    var SCHEDULE = "schedule"
    var GCM_MESSAGE = "gcm_schedule"
    val PUSH_TOKEN = "push_token"
    val IS_AVAIL = "isavailable"

    fun AppUpdate(pkgIntent: Intent) {
        val intent = Intent(APP_UPDATE)
        intent.putExtra("intent", pkgIntent)
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(DastanLibApp.INSTANCE).sendBroadcast(intent)
    }

    fun GooglePlayServiceMission(isAvailable: Boolean) {
        val intent = Intent(GPlayS_Missing)
        intent.putExtra(IS_AVAIL, isAvailable)
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(DastanLibApp.INSTANCE).sendBroadcast(intent)
    }


    fun ScheduleEvent() {
        val intent = Intent(SCHEDULE)
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(DastanLibApp.INSTANCE).sendBroadcast(intent)
    }

    fun PushMsg(message: String) {
        val intent = Intent(SendBroadcast.GCM_MESSAGE)
        intent.putExtra("msg", message)
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(DastanLibApp.INSTANCE).sendBroadcast(intent)
    }

    fun PushToken(token: String) {
        val intent = Intent()
        intent.action = DastanLibApp.INSTANCE.getString(R.string.token_refresh_broadcast)
        intent.putExtra("token", token)
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(DastanLibApp.INSTANCE).sendBroadcast(intent)
    }
}
