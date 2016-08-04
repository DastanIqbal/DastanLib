package com.dastanapps.dastanlib.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mebelkart.app.Utils.SPUtils;

/**
 * Created by IQBAL-MEBELKART on 5/4/2016.
 */
public class UpdateAppRecevier extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SPUtils.writeAppConfig("");
        SPUtils.writeMenu(context,"");
    }
}


