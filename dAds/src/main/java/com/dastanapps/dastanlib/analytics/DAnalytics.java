package com.dastanapps.dastanlib.analytics;

import android.os.Bundle;

import com.dastanapps.dastanlib.DastanAdsApp;
import com.dastanapps.dastanlib.utils.SPUtils;
import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
//import com.vungle.publisher.log.Logger;

/**
 * Created by dastaniqbal on 28/12/2016.
 * 28/12/2016 12:19
 */

public class DAnalytics {
    public static String ANALYTICS_API_KEY = "analytics_api_key";

    private static String TAG = DAnalytics.class.getSimpleName();
    private static DAnalytics danalytics;
    private FirebaseAnalytics firebaseAnalytics;

    public DAnalytics() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(DastanAdsApp.INSTANCE);
        String apiKey = (String) SPUtils.INSTANCE.readSP(ANALYTICS_API_KEY, "");
        if (DastanAdsApp.INSTANCE.isRelease()) {
            new FlurryAgent.Builder()
                    .withLogEnabled(false)
//                    .withListener(() -> Logger.d(TAG, "Flurry Session Started"))
                    .build(DastanAdsApp.INSTANCE, apiKey);
        }
    }

    public static DAnalytics getInstance() {
        if (danalytics == null) {
            danalytics = new DAnalytics();
        }
        return danalytics;
    }

    public void setVersion(String versionName) {
        if (DastanAdsApp.INSTANCE.isRelease()) {
            FlurryAgent.setVersionName(versionName);
        }
    }

    public void sendProperty(String key, String value) {
        if (DastanAdsApp.INSTANCE.isRelease()) {
            firebaseAnalytics.setUserProperty(key, value);
            if (FlurryAgent.isSessionActive()) {
                FlurryAgent.logEvent(key + " : " + value);
            }
        }
    }

    public void sendParams(String key, Bundle bundle) {
        if (DastanAdsApp.INSTANCE.isRelease()) {
            firebaseAnalytics.logEvent(key, bundle);
        }
    }


    public void sendLogs(String tag, String value) {
        if (DastanAdsApp.INSTANCE.isRelease()) {
            Bundle bundle = new Bundle();
            bundle.putString(tag, value);
            sendParams(tag, bundle);
            sendProperty(tag, value);
        }
    }

    public void sendLogs(String tag, String key, String value) {
        if (DastanAdsApp.INSTANCE.isRelease()) {
            Bundle bundle = new Bundle();
            bundle.putString(key, value);
            sendParams(tag, bundle);
            sendProperty(tag, value);
        }
    }

    public void addPurchase(long price, String item, String type, String id, boolean success) {
        if (DastanAdsApp.INSTANCE.isRelease()) {

        }
    }

    public void addToCart(String key, String value) {
        if (DastanAdsApp.INSTANCE.isRelease()) {

        }
    }

    public void startCheckout(String key, String value) {
        if (DastanAdsApp.INSTANCE.isRelease()) {

        }
    }

    public void addPurchase(String item, long price, String type, boolean success) {
        if (DastanAdsApp.INSTANCE.isRelease()) {

        }
    }
}
