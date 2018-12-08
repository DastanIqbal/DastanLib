package com.dastanapps.dastanlib.analytics;

import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.AddToCartEvent;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.PurchaseEvent;
import com.crashlytics.android.answers.StartCheckoutEvent;
import com.dastanapps.dastanlib.DastanAdsApp;
import com.dastanapps.dastanlib.utils.SPUtils;
import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vungle.publisher.log.Logger;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;

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
        String apiKey = (String) SPUtils.INSTANCE.readSP(ANALYTICS_API_KEY,"");
        if (DastanAdsApp.INSTANCE.isRelease()) {
            new FlurryAgent.Builder()
                    .withLogEnabled(false)
                    .withListener(() -> Logger.d(TAG, "Flurry Session Started"))
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
            CustomEvent customEvent = new CustomEvent(key);
            HashMap<String, String> hashMap = new HashMap<>();
            for (String keyE : bundle.keySet()) {
                String value = bundle.getString(keyE);
                hashMap.put(keyE, value);
                customEvent.putCustomAttribute(keyE, value);
            }
            if (FlurryAgent.isSessionActive()) {
                FlurryAgent.logEvent(key, hashMap);
            }
            Answers.getInstance().logCustom(customEvent);
        }
    }

    public void setContentView(String name, String type, String id) {
        if (DastanAdsApp.INSTANCE.isRelease()) {
            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName(name)
                    .putContentType(type)
                    .putContentId(id));
        }
    }

    public void sendLogs(String tag, String value) {
        if (DastanAdsApp.INSTANCE.isRelease()) {
            Bundle bundle = new Bundle();
            bundle.putString(tag, value);
            sendParams(tag, bundle);
            sendProperty(tag, value);
            CustomEvent customEvent = new CustomEvent(tag);
            customEvent.putCustomAttribute(tag, value);
            Answers.getInstance().logCustom(customEvent);
            Crashlytics.log(Log.DEBUG, tag, value);
        }
    }

    public void sendLogs(String tag, String key, String value) {
        if (DastanAdsApp.INSTANCE.isRelease()) {
            Bundle bundle = new Bundle();
            bundle.putString(key, value);
            sendParams(tag, bundle);
            sendProperty(tag, value);
            CustomEvent customEvent = new CustomEvent(tag);
            customEvent.putCustomAttribute(key, value);
            Answers.getInstance().logCustom(customEvent);
            Logger.d(TAG, key + " : " + value);
        }
    }

    public void addPurchase(long price, String item, String type, String id, boolean success) {
        if (DastanAdsApp.INSTANCE.isRelease()) {
            Answers.getInstance().logPurchase(new PurchaseEvent()
                    .putItemPrice(BigDecimal.valueOf(price))
                    .putCurrency(Currency.getInstance("USD"))
                    .putItemName(item)
                    .putItemType(type)
                    .putItemId(id)
                    .putSuccess(success));
        }
    }

    public void addToCart(String key, String value) {
        if (DastanAdsApp.INSTANCE.isRelease()) {
            Answers.getInstance().logAddToCart(new AddToCartEvent()
                    .putCustomAttribute(key, value));
        }
    }

    public void startCheckout(String key, String value) {
        if (DastanAdsApp.INSTANCE.isRelease()) {
            Answers.getInstance().logStartCheckout(new StartCheckoutEvent()
                    .putCustomAttribute(key, value));
        }
    }

    public void addPurchase(String item, long price, String type, boolean success) {
        if (DastanAdsApp.INSTANCE.isRelease()) {
            Answers.getInstance().logPurchase(new PurchaseEvent()
                    .putItemPrice(BigDecimal.valueOf(price))
                    .putCurrency(Currency.getInstance("USD"))
                    .putItemName(item)
                    .putItemType(type)
                    .putSuccess(success));
        }
    }
}
