package com.dastanapps.dastanlib.log;

import android.util.Log;

import com.dastanapps.dastanlib.BuildConfig;


/**
 * Created by Iqbal Ahmed on 10/8/2015.
 */
public class Logger {
    private static String checkNull(String log) {
        return null == log ? "Null" : log;
    }

    public static void i(String tag, String log) {
        if (BuildConfig.DEBUG)
            Log.d("Test", checkNull(log));
//        DFabric.log(tag, checkNull(log));
    }

    public static void w(String tag, String log) {
        if (BuildConfig.DEBUG)
            Log.w("Test", checkNull(log));
//        DFabric.log(tag, checkNull(log));
    }

    public static void d(String tag, String log) {
        if (BuildConfig.DEBUG)
            Log.d("Test", checkNull(log));
//        DFabric.log(tag, checkNull(log));
    }

    public static void e(String tag, String log) {
        if (BuildConfig.DEBUG)
            Log.e("Test", checkNull(log));
//        DFabric.log(tag, checkNull(log));
    }

    public static void v(String tag, String log) {
        if (BuildConfig.DEBUG)
            Log.v("Test", log);
//        DFabric.log(tag, log);
    }
}
