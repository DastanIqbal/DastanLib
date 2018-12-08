/*
package com.dastanapps.dastanlib.analytics.google;

import android.content.Context;

import com.dastanapps.dastanlib.BuildConfig;
import com.dastanapps.dastanlib.log.Logger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

*/
/**
 * Created by IQBAL-MEBELKART on 5/11/2016.
 *//*

public class GAnalytics {
    private static final String TAG = GAnalytics.class.getSimpleName();
    private static Tracker mTracker;

    public static void checkTrackerNull(Context context, int trackerId) {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
            if (BuildConfig.DEBUG) {
                analytics.setDryRun(true);
                analytics.setAppOptOut(true); //To disable analytics across the app
            }
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(trackerId);
        }
    }

    public static void sendScreen(String screenName) {
        Logger.i(TAG, "Setting screen name: " + screenName);
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static void sendEvent(Context ctxt, String cat, String action) {
        Logger.i(TAG, "Cat: " + cat + " Action:" + action);
        try {
            if (mTracker == null)
                throw new RuntimeException("Tracker is NULL");
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(cat)
                .setAction(action)
                .setLabel(cat)
                .build());
    }

}
*/
