package com.dastanapps.dastanlib.Analytics;

import android.app.Activity;
import android.location.Location;
import android.support.annotation.NonNull;

import com.dastanapps.dastanlib.Analytics.google.GAnalytics;
import com.dastanapps.dastanlib.Analytics.google.GEcommerceTracking;

import org.json.JSONObject;

/**
 * Created by IQBAL-MEBELKART on 1/30/2016.
 */
public class DAnalytics {
    private static final String TAG = DAnalytics.class.getSimpleName();

    private DAnalytics mkartAnalytics;
    private Activity activity;

    public DAnalytics() {
    }

    //Adobe
    public void onCreate() {

    }


    public DAnalytics getInstance() {
        checkTrackerNull();
        return this;
    }

    public DAnalytics getGEInstance() {
        checkGETrackerNull();
        return this;
    }

    private synchronized void checkTrackerNull() {
        GAnalytics.checkTrackerNull();
    }

    private synchronized void checkGETrackerNull() {
        GEcommerceTracking.checkTrackerNull();
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

    public DAnalytics sendEvent(String cat, String action) {
        GAnalytics.sendEvent(cat, action);
        return this;
    }

    public void sendLocation(Location mLastLocation) {

    }

    public void setVisitorId(String visitorId, String authType) {

    }



    public void sendSearch(String searchValues) {
        sendEvent("HotSearch", "Searching:" + searchValues);
        DFabric.logSearch(searchValues);
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
}
