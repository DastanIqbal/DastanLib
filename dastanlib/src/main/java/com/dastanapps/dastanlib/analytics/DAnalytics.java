package com.dastanapps.dastanlib.analytics;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.PurchaseEvent;
import com.dastanapps.dastanlib.DastanApp;
import com.dastanapps.dastanlib.SPConstant;
import com.dastanapps.dastanlib.analytics.google.GAnalytics;
import com.dastanapps.dastanlib.analytics.google.GEcommerceTracking;
import com.dastanapps.dastanlib.log.Logger;
import com.dastanapps.dastanlib.utils.SPUtils;
import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAgentListener;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;

/**
 * Created by IQBAL-MEBELKART on 1/30/2016.
 */
public class DAnalytics {
    private static final String TAG = DAnalytics.class.getSimpleName();

    private static DAnalytics mkartAnalytics;
    private Activity activity;
    private FirebaseAnalytics firebaseAnalytics;

    public DAnalytics() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(DastanApp.getInstance());
        String apiKey = SPUtils.readString(SPConstant.ANALYTICS_API_KEY);
        if (DastanApp.getInstance().isRelease()) {
            new FlurryAgent.Builder()
                    .withLogEnabled(false)
                    .withListener(new FlurryAgentListener() {
                        @Override
                        public void onSessionStarted() {
                            Logger.d(TAG, "Flurry Session Started");
                        }
                    })
                    .build(DastanApp.getInstance(), apiKey);//"PJT93QQMC5MNY4X76JS3");
        }
    }

    //Adobe
    public void onCreate() {

    }

    
    public DAnalytics getInstance(Context ctxt, int trackerId) {
        checkTrackerNull(ctxt, trackerId);
        return this;
    }

    public DAnalytics getGEInstance(Context ctxt, int trackerId) {
        checkGETrackerNull(ctxt, trackerId);
        return this;
    }

    private synchronized void checkTrackerNull(Context ctxt, int trackerId) {
        GAnalytics.checkTrackerNull(ctxt, trackerId);
    }

    private synchronized void checkGETrackerNull(Context ctxt, int trackerId) {
        GEcommerceTracking.checkTrackerNull(ctxt, trackerId);
    }

    // For Adobe collecting lifecycle data
    @NonNull
    public DAnalytics setCurrentActivity(@NonNull Activity activity) {
        this.activity = activity;
        return this;
    }

    public DAnalytics sendScreenName(String screenName) {
        GAnalytics.sendScreen(screenName);
        return this;
    }

    public DAnalytics onPause() {
        return this;
    }

    public DAnalytics sendEvent(Context ctxt, String cat, String action) {
        GAnalytics.sendEvent(ctxt, cat, action);
        return this;
    }

    public void sendLocation(Location mLastLocation) {

    }

    public void setVisitorId(String visitorId, String authType) {

    }


    public void sendSearch(Context ctxt, String searchValues) {
        sendEvent(ctxt, "HotSearch", "Searching:" + searchValues);
        //DFabric.logSearch(searchValues);
    }

    public enum VisitorIDAuthenticationState {
        VISITOR_ID_AUTHENTICATION_STATE_UNKNOWN(),
        VISITOR_ID_AUTHENTICATION_STATE_AUTHENTICATED(),
        VISITOR_ID_AUTHENTICATION_STATE_LOGGED_OUT();
    }


    //Ecommerce Analytics
    public void measureImpression(JSONObject impressionJson, String screenName) {
        GEcommerceTracking.measureImpression(impressionJson, screenName);
    }

    public void measureAction(JSONObject impressionJson, String screenName) {
        GEcommerceTracking.measureAction(impressionJson, screenName);
    }

    public void measureImpressionAndAction(JSONObject impressionJson, String screenName) {
        GEcommerceTracking.measureImpressionAndAction(impressionJson);
    }

    public static DAnalytics getInstance() {
        if (mkartAnalytics == null) {
            mkartAnalytics = new DAnalytics();
        }
        return mkartAnalytics;
    }

    public void setVersion(String versionName) {
        if (DastanApp.getInstance().isRelease()) {
            FlurryAgent.setVersionName(versionName);
        }
    }

    public void sendProperty(String key, String value) {
        if (DastanApp.getInstance().isRelease()) {
            firebaseAnalytics.setUserProperty(key, value);
            if(FlurryAgent.isSessionActive()) {
                FlurryAgent.logEvent(key + " : " + value);
            }
        }
    }

    public void sendParams(String key, Bundle bundle) {
        if (DastanApp.getInstance().isRelease()) {
            firebaseAnalytics.logEvent(key, bundle);
            CustomEvent customEvent = new CustomEvent(key);
            HashMap<String, String> hashMap = new HashMap<>();
            for (String keyE : bundle.keySet()) {
                String value = bundle.getString(keyE);
                hashMap.put(keyE, value);
                customEvent.putCustomAttribute(keyE, value);
            }
            if(FlurryAgent.isSessionActive()) {
                FlurryAgent.logEvent(key, hashMap);
            }
            Answers.getInstance().logCustom(customEvent);
        }
    }

    public void setContentView(String name, String type, String id) {
        if (DastanApp.getInstance().isRelease()) {
            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName(name)
                    .putContentType(type)
                    .putContentId(id));
        }
    }

    public void sendLogs(String tag, String value) {
        if (DastanApp.getInstance().isRelease()) {
            sendProperty(tag, value);
            CustomEvent customEvent = new CustomEvent(tag);
            customEvent.putCustomAttribute(tag, value);
            Answers.getInstance().logCustom(customEvent);
        }
    }

    public void sendLogs(String tag, String key, String value) {
        if (DastanApp.getInstance().isRelease()) {
            Bundle bundle = new Bundle();
            bundle.putString(key, value);
            sendParams(tag, bundle);
            CustomEvent customEvent = new CustomEvent(tag);
            customEvent.putCustomAttribute(key, value);
            Answers.getInstance().logCustom(customEvent);
        }
    }

    public void addPurchase(long price, String item, String type, String id, boolean success) {
        if (DastanApp.getInstance().isRelease()) {
            Answers.getInstance().logPurchase(new PurchaseEvent()
                    .putItemPrice(BigDecimal.valueOf(price))
                    .putCurrency(Currency.getInstance("USD"))
                    .putItemName(item)
                    .putItemType(type)
                    .putItemId(id)
                    .putSuccess(success));
        }
    }

    public void addPurchase(String item, long price, String type, boolean success) {
        if (DastanApp.getInstance().isRelease()) {
            Answers.getInstance().logPurchase(new PurchaseEvent()
                    .putItemPrice(BigDecimal.valueOf(price))
                    .putCurrency(Currency.getInstance("USD"))
                    .putItemName(item)
                    .putItemType(type)
                    .putSuccess(success));
        }
    }
}
