package com.dastanapps.dastanlib.log;

import android.util.Log;

import static com.dastanapps.dastanlib.utils.CommonUtils.checkNull;


/**
 * Created by Iqbal Ahmed on 10/8/2015.
 */
public class Logger {

    public static void i(String tag, String log) {
            Log.d(tag, checkNull(log,"Null"));
//        DFabric.log(tag, checkNull(log));
    }

    public static void w(String tag, String log) {
            Log.w(tag, checkNull(log,"Null"));
//        DFabric.log(tag, checkNull(log));
    }

    public static void d(String tag, String log) {
            Log.d(tag, checkNull(log,"Null"));
//        DFabric.log(tag, checkNull(log));
    }

    public static void e(String tag, String log) {
            Log.e(tag, checkNull(log,"Null"));
//        DFabric.log(tag, checkNull(log));
    }

    public static void v(String tag, String log) {
            Log.v(tag, checkNull(log,"Null"));
//        DFabric.log(tag, log);
    }
}
