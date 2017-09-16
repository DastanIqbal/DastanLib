package com.dastanapps.dastanlib.log;

import android.util.Log;

import com.dastanapps.dastanlib.DastanApp;


/**
 * Created by Iqbal Ahmed on 10/8/2015.
 * dastanIqbal@marvelmedia.com
 * 10/8/2015 11:07
 */
public class Logger {
    private static String checkNull(String log) {
        return null == log ? "Null" : log;
    }

    public static void i(String tag, String log) {
        if (!DastanApp.getAppInstance().isRelease())
            Log.d(tag, checkNull(log));
//        DFabric.log(tag, checkNull(log));
    }

    public static void w(String tag, String log) {
        if (!DastanApp.getAppInstance().isRelease())
            Log.w(tag, checkNull(log));
//        DFabric.log(tag, checkNull(log));
    }

    public static void d(String tag, String log) {
        if (!DastanApp.getAppInstance().isRelease())
            Log.d(tag, checkNull(log));
//        DFabric.log(tag, checkNull(log));
    }

    public static void e(String tag, String log) {
        if (!DastanApp.getAppInstance().isRelease())
            Log.e(tag, checkNull(log));
//        DFabric.log(tag, checkNull(log));
    }

    public static void v(String tag, String log) {
        if (!DastanApp.getAppInstance().isRelease())
            Log.v(tag, log);
//        DFabric.log(tag, log);
    }

    public static void onlyDebug(String log) {
        if (!DastanApp.getAppInstance().isRelease())
            Log.d("dastanLib", checkNull(log));
//        DFabric.log(tag, checkNull(log));
    }
}
