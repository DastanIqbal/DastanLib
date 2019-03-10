package com.dastanapps.dastanlib.receiver

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager

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
        LocalBroadcastManager.getInstance(DastanLibApp.INSTANCE).sendBroadcast(intent)
    }

    fun GooglePlayServiceMission(isAvailable: Boolean) {
        val intent = Intent(GPlayS_Missing)
        intent.putExtra(IS_AVAIL, isAvailable)
        LocalBroadcastManager.getInstance(DastanLibApp.INSTANCE).sendBroadcast(intent)
    }


    fun ScheduleEvent() {
        val intent = Intent(SCHEDULE)
        LocalBroadcastManager.getInstance(DastanLibApp.INSTANCE).sendBroadcast(intent)
    }

    fun PushMsg(message: String) {
        val intent = Intent(SendBroadcast.GCM_MESSAGE)
        intent.putExtra("msg", message)
        LocalBroadcastManager.getInstance(DastanLibApp.INSTANCE).sendBroadcast(intent)
    }

    fun PushToken(){
        val intent = Intent()
        intent.action = DastanLibApp.INSTANCE.getString(R.string.token_refresh_broadcast)
        LocalBroadcastManager.getInstance(DastanLibApp.INSTANCE).sendBroadcast(intent)
    }
}
