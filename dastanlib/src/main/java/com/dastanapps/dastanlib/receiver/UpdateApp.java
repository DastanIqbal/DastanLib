package com.dastanapps.dastanlib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class UpdateApp extends BroadcastReceiver {
    public UpdateApp() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SendBroadcast.AppUpdate(intent);
    }
}
