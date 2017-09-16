package com.dastanapps.dastanlib.ads;

import android.app.Activity;
import android.os.Bundle;

import com.dastanapps.dastanlib.DastanApp;
import com.dastanapps.dastanlib.analytics.DAnalytics;
import com.dastanapps.dastanlib.log.Logger;
import com.dastanapps.dastanlib.utils.SPUtils;
import com.startapp.android.publish.ads.nativead.NativeAdDetails;
import com.startapp.android.publish.ads.nativead.NativeAdPreferences;
import com.startapp.android.publish.ads.nativead.StartAppNativeAd;
import com.startapp.android.publish.ads.splash.SplashConfig;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.adListeners.AdDisplayListener;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by dastaniqbal on 15/03/2017.
 * dastanIqbal@marvelmedia.com
 * 15/03/2017 11:22
 */

public class StartAppAds {
    private final String TAG = StartAppAds.class.getSimpleName();
    private StartAppNativeAd startAppNativeAd = new StartAppNativeAd(DastanApp.getInstance());

    private HashMap<String, IMarvelAds> listnerHashMap = new HashMap<>();

    public void setIStartApp(IStartApp startApp, String tag) {
        listnerHashMap.put(tag, startApp);
    }

    private AdEventListener nativeAdListener = new AdEventListener() {     // Callback Listener
        @Override
        public void onReceiveAd(Ad arg0) {
            // Native Ad received
            ArrayList<NativeAdDetails> ads = startAppNativeAd.getNativeAds();    // get NativeAds list
            if (ads != null) {
                if (!listnerHashMap.isEmpty()) {
                    Set<String> startAppKeys = listnerHashMap.keySet();
                    for (String key : startAppKeys) {
                        IStartApp startAppListener = (IStartApp) listnerHashMap.get(key);
                        startAppListener.onReceived(ads);
                    }
                }
                // Print all ads details to log
                for (NativeAdDetails nativeAdDetails : ads) {
                    try {
                        if (nativeAdDetails != null)
                            Logger.d(TAG, nativeAdDetails.toString());
                    } catch (Exception e) {
                    }
                }
            }
        }

        @Override
        public void onFailedToReceiveAd(Ad arg0) {
            // Native Ad failed to receive
            Logger.e(TAG, "Error while loading Ad");
            if (!listnerHashMap.isEmpty()) {
                Set<String> startAppKeys = listnerHashMap.keySet();
                for (String key : startAppKeys) {
                    IStartApp startAppListener = (IStartApp) listnerHashMap.get(key);
                    startAppListener.onFailed();
                }
            }
            DAnalytics.getInstance().sendLogs("StartApp", "nativeAds", "failed_loading");
        }
    };

    public void init() {
        //102362101,202271055
        StartAppSDK.init(DastanApp.getAppInstance(), SPUtils.readString("adnetworkId"), false);
        startAppNativeAd = new StartAppNativeAd(DastanApp.getInstance());
    }

    public void splashAds(Activity activity, Bundle savedInstanceState, String appName, int logo) {
        StartAppAd.showSplash(activity, savedInstanceState, new SplashConfig()
                .setTheme(SplashConfig.Theme.DEEP_BLUE)
                .setAppName(appName)
                .setLogo(logo)   // resource ID
                .setOrientation(SplashConfig.Orientation.PORTRAIT));
    }

    public void backPressed(final StartAppAd startAppAd) {
        setInterstialAds(startAppAd);
        startAppAd.showAd(new AdDisplayListener() {
            @Override
            public void adHidden(Ad ad) {

            }

            @Override
            public void adDisplayed(Ad ad) {

            }

            @Override
            public void adClicked(Ad ad) {
                DAnalytics.getInstance().sendLogs("StartApp", "backPressed", "adClicked");
                DAnalytics.getInstance().addPurchase("StartApp", 1, "backPressed", true);
            }

            @Override
            public void adNotDisplayed(Ad ad) {
                DAnalytics.getInstance().sendLogs("StartApp", "interstialAds", "adNotDisplayed");
            }
        });
        StartAppAd.onBackPressed(DastanApp.getAppInstance());
    }

    public void setInterstialAds(final StartAppAd startAppAd) {
        startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC, new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                Logger.d(TAG, "IntersialAd onReceived");
            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
                Logger.d(TAG, "IntersialAd onFailedToReceiveAd");
                startAppAd.close();
                DAnalytics.getInstance().sendLogs("StartApp", "IntersitalAds", "failed_loading");
            }
        });
    }

    public void showVideoAds(StartAppAd startAppAd) {
        startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO);
    }

    public void showInterstialAds(StartAppAd startAppAd) {
        startAppAd.showAd(new AdDisplayListener() {
            @Override
            public void adHidden(Ad ad) {
                Logger.d(TAG, "IntersialShowAd adHidden");
            }

            @Override
            public void adDisplayed(Ad ad) {
                Logger.d(TAG, "IntersialShowAd adDisplayed");
            }

            @Override
            public void adClicked(Ad ad) {
                Logger.d(TAG, "IntersialShowAd adClicked");
                DAnalytics.getInstance().sendLogs("StartApp", "interstialAds", "adClicked");
                DAnalytics.getInstance().addPurchase("StartApp", 1, "interstialAds", true);
            }

            @Override
            public void adNotDisplayed(Ad ad) {
                Logger.d(TAG, "IntersialShowAd adNotDisplayed");
                DAnalytics.getInstance().sendLogs("StartApp", "interstialAds", "adNotDisplayed");
            }
        });
    }

    public void setupNativeAds() {
        if (startAppNativeAd == null) return;
        startAppNativeAd.loadAd(new NativeAdPreferences()
                .setAutoBitmapDownload(true)
                .setAdsNumber(21)
                .setPrimaryImageSize(3)
                .setSecondaryImageSize(2), nativeAdListener);
    }

    public void setupNativeAds(int adsNumber) {
        if (startAppNativeAd == null) return;
        startAppNativeAd.loadAd(new NativeAdPreferences()
                .setAutoBitmapDownload(true)
                .setAdsNumber(adsNumber)
                .setPrimaryImageSize(6)
                .setSecondaryImageSize(2), nativeAdListener);
    }

    public void onDestroy(String tag) {
        listnerHashMap.remove(tag);
    }
}
