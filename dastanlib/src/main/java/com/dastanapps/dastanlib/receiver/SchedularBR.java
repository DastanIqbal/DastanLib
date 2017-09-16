package com.dastanapps.dastanlib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.dastanapps.dastanlib.log.Logger;

/**
 * Created by dastaniqbal on 16/12/2016.
 * ask2iqbal@gmail.com
 * 16/12/2016 3:38
 */

public class SchedularBR extends BroadcastReceiver {
    private static final String TAG = SchedularBR.class.getSimpleName();

    public SchedularBR() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d(TAG, "Schedular Called");
        Intent localSmsReciver = new Intent(ReceiverFilter.OTP_SMS_RECEIVER);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localSmsReciver);
    }
}
