package com.dastanapps.dastanlib.analytics;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.dastanapps.dastanlib.analytics.google.GAnalytics;
import com.dastanapps.dastanlib.analytics.google.GEcommerceTracking;

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
}
