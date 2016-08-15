package com.dastanapps.dastanlib;

import android.content.Context;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by IQBAL-MEBELKART on 8/15/2016.
 */

public class LibInit {

    public LibInit(Context ctxt) {
        Fabric.with(ctxt, new Crashlytics());
    }
}
