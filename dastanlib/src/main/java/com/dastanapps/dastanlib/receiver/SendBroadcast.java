package com.dastanapps.dastanlib.receiver;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.dastanapps.dastanlib.DastanApp;


/**
 * Created by dastaniqbal on 09/01/2017.
 * 09/01/2017 11:53
 */

public class SendBroadcast {
    public static String APP_UPDATE = "app_update";
    public static String GPlayS_Missing = "gplayservice_missing";
    public static String SCHEDULE = "schedule";
    public static String GCM_MESSAGE = "gcm_schedule";
    public static final String PUSH_TOKEN = "push_token";
    public static final String IS_AVAIL="isavailable";

    public static void AppUpdate(Intent pkgIntent) {
        Intent intent = new Intent(APP_UPDATE);
        intent.putExtra("intent", pkgIntent);
        LocalBroadcastManager.getInstance(DastanApp.getInstance()).sendBroadcast(intent);
    }

    public static void GooglePlayServiceMission(boolean isAvailable) {
        Intent intent = new Intent(GPlayS_Missing);
        intent.putExtra(IS_AVAIL,isAvailable);
        LocalBroadcastManager.getInstance(DastanApp.getInstance()).sendBroadcast(intent);
    }


    public static void ScheduleEvent() {
        Intent intent = new Intent(SCHEDULE);
        LocalBroadcastManager.getInstance(DastanApp.getInstance()).sendBroadcast(intent);
    }

    public static void PushMsg(String message) {
        Intent intent = new Intent(SendBroadcast.GCM_MESSAGE);
        intent.putExtra("msg", message);
        LocalBroadcastManager.getInstance(DastanApp.getInstance()).sendBroadcast(intent);
    }
}
