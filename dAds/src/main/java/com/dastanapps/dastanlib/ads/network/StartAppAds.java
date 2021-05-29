package com.dastanapps.dastanlib.ads.network;

import android.app.Activity;
import android.os.Bundle;

import com.dastanapps.dastanlib.DastanAdsApp;
import com.dastanapps.dastanlib.ads.interfaces.IAdMobAds;
import com.dastanapps.dastanlib.ads.interfaces.IMarvelAds;
import com.dastanapps.dastanlib.ads.interfaces.IStartApp;
import com.dastanapps.dastanlib.analytics.DAnalytics;
import com.dastanapps.dastanlib.log.Logger;
import com.startapp.android.publish.ads.nativead.NativeAdDetails;
import com.startapp.android.publish.ads.nativead.NativeAdPreferences;
import com.startapp.android.publish.ads.nativead.StartAppNativeAd;
import com.startapp.android.publish.ads.splash.SplashConfig;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.VideoListener;
import com.startapp.android.publish.adsCommon.adListeners.AdDisplayListener;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;
import com.dastanapps.dastanlib.ads.AdsBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by dastaniqbal on 15/03/2017.
 * ask2iqbal@gmail.com
 * 15/03/2017 11:22
 */

public class StartAppAds extends AdsBase {
    private HashMap listnerHashMap = new HashMap<String, IMarvelAds>();
    public ArrayList mNativeAdsList = new ArrayList<NativeAdDetails>();

    private final String TAG = StartAppAds.class.getSimpleName();
    private StartAppNativeAd startAppNativeAd = new StartAppNativeAd(DastanAdsApp.INSTANCE);

    private IAdMobAds rewardedVideoListener;
    private StartAppAd rewardAds;

    public void setIStartApp(IStartApp startApp, String tag) {
        listnerHashMap.put(tag, startApp);
    }

    public void setIStartApp(IMarvelAds startApp, String tag) {
        listnerHashMap.put(tag, startApp);
    }

    private AdEventListener nativeAdListener = new AdEventListener() {     // Callback Listener
        @Override
        public void onReceiveAd(Ad arg0) {
            try {
                StartAppNativeAd startAppAds = (StartAppNativeAd) arg0;
                // Native Ad received
                ArrayList<NativeAdDetails> ads = startAppAds.getNativeAds();    // get NativeAds list
                if (ads != null) {
                    if (!listnerHashMap.isEmpty()) {
                        Set<String> startAppKeys = listnerHashMap.keySet();
                        for (String key : startAppKeys) {
                            ((IMarvelAds) listnerHashMap.get(key)).adLoaded(ads);
                        }
                    }
                    // Print all ads details to log
                    for (NativeAdDetails nativeAdDetails : ads) {

                        if (nativeAdDetails != null)
                            Logger.INSTANCE.d(TAG, nativeAdDetails.toString());
                    }
                }
            } catch (Exception ignored) {
            }
        }

        @Override
        public void onFailedToReceiveAd(Ad arg0) {
            // Native Ad failed to receive
            Logger.INSTANCE.e(TAG, "Error while loading Ad");
            if (!listnerHashMap.isEmpty()) {
                Set<String> startAppKeys = listnerHashMap.keySet();
                for (String key : startAppKeys) {
                    IMarvelAds startAppListener = (IMarvelAds) listnerHashMap.get(key);
                    startAppListener.adError("failedToReceived");
                }
            }
            DAnalytics.getInstance().sendLogs("StartApp", "nativeAds", "failed_loading");
        }
    };

    public void init() {
        StartAppSDK.init(DastanAdsApp.INSTANCE, DastanAdsApp.INSTANCE.getAdsConfiguration().getStartAppId(), false);
        startAppNativeAd = new StartAppNativeAd(DastanAdsApp.INSTANCE);
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
        StartAppAd.onBackPressed(DastanAdsApp.INSTANCE);
    }

    public void setInterstialAds(final StartAppAd startAppAd) {
        if (!DastanAdsApp.INSTANCE.disableAds())
            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC, new AdEventListener() {
                @Override
                public void onReceiveAd(Ad ad) {
                    Logger.INSTANCE.d(TAG, "IntersialAd onReceived");
                }

                @Override
                public void onFailedToReceiveAd(Ad ad) {
                    Logger.INSTANCE.d(TAG, "IntersialAd onFailedToReceiveAd");
                    startAppAd.close();
                    DAnalytics.getInstance().sendLogs("StartApp", "IntersitalAds", "failed_loading");
                }
            });
    }

    public boolean isAdsReady() {
        if (rewardAds != null && rewardAds.isReady()) {

            if (!DastanAdsApp.INSTANCE.disableAds())
                rewardAds.loadAd(StartAppAd.AdMode.REWARDED_VIDEO);

            return true;
        } else {
            return false;
        }
    }

    public void loadRewardedAds() {
        rewardAds = new StartAppAd(DastanAdsApp.INSTANCE);
        if (!DastanAdsApp.INSTANCE.disableAds())
            rewardAds.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
                @Override
                public void onReceiveAd(Ad ad) {
                    if (rewardedVideoListener != null) rewardedVideoListener.adLoaded(ad);
                }

                @Override
                public void onFailedToReceiveAd(Ad ad) {
                    try {
                        if (rewardedVideoListener != null)
                            rewardedVideoListener.adError(ad.getErrorMessage());
                    } catch (Exception ignored) {
                    }
                }
            });
        rewardAds.setVideoListener(new VideoListener() {
            @Override
            public void onVideoCompleted() {
                if (rewardedVideoListener != null) rewardedVideoListener.completed();
            }
        });
    }

    public void showRewardAds() {
        if (rewardAds != null) rewardAds.showAd();
    }

    public void showInterstialAds(StartAppAd startAppAd) {
        startAppAd.showAd(new AdDisplayListener() {
            @Override
            public void adHidden(Ad ad) {
                Logger.INSTANCE.d(TAG, "IntersialShowAd adHidden");
            }

            @Override
            public void adDisplayed(Ad ad) {
                Logger.INSTANCE.d(TAG, "IntersialShowAd adDisplayed");
            }

            @Override
            public void adClicked(Ad ad) {
                Logger.INSTANCE.d(TAG, "IntersialShowAd adClicked");
                DAnalytics.getInstance().sendLogs("StartApp", "interstialAds", "adClicked");
                DAnalytics.getInstance().addPurchase("StartApp", 1, "interstialAds", true);
            }

            @Override
            public void adNotDisplayed(Ad ad) {
                Logger.INSTANCE.d(TAG, "IntersialShowAd adNotDisplayed");
                DAnalytics.getInstance().sendLogs("StartApp", "interstialAds", "adNotDisplayed");
            }
        });
    }

    public void setupNativeAds() {
        if (startAppNativeAd == null) return;
        if (!DastanAdsApp.INSTANCE.disableAds())
            startAppNativeAd.loadAd(new NativeAdPreferences()
                    .setAutoBitmapDownload(true)
                    .setAdsNumber(21)
                    .setPrimaryImageSize(3)
                    .setSecondaryImageSize(2), nativeAdListener);
    }

    public void setupSingleNativeAds(final String tag) {
        if (!DastanAdsApp.INSTANCE.disableAds())
            startAppNativeAd.loadAd(new NativeAdPreferences()
                            .setAutoBitmapDownload(true)
                            .setAdsNumber(1)
                            .setPrimaryImageSize(3)
                            .setSecondaryImageSize(0)
                    , new AdEventListener() {     // Callback Listener
                        @Override
                        public void onReceiveAd(Ad arg0) {
                            try {
                                StartAppNativeAd startAppAds = (StartAppNativeAd) arg0;
                                // Native Ad received
                                ArrayList<NativeAdDetails> ads = startAppAds.getNativeAds();    // get NativeAds list
                                if (ads != null && listnerHashMap.get(tag) != null) {
                                    ((IMarvelAds) listnerHashMap.get(tag)).adLoaded(ads);
                                    listnerHashMap.remove(tag);
                                    // Print all ads details to log
                                    for (NativeAdDetails nativeAdDetails : ads) {

                                        if (nativeAdDetails != null)
                                            Logger.INSTANCE.d(TAG, nativeAdDetails.toString());
                                    }
                                }
                            } catch (Exception ignored) {
                            }
                        }

                        @Override
                        public void onFailedToReceiveAd(Ad arg0) {
                            // Native Ad failed to receive
                            Logger.INSTANCE.e(TAG, "Error while loading Ad");
                            if (listnerHashMap.get(tag) != null)
                                ((IMarvelAds) listnerHashMap.get(tag)).adError("failedToReceived");
                            DAnalytics.getInstance().sendLogs("StartApp", "nativeAds", "failed_loading");
                        }
                    });
    }

    public void setupNativeAds(String tag, int adsNumber) {
        if (!DastanAdsApp.INSTANCE.disableAds())
            startAppNativeAd.loadAd(new NativeAdPreferences()
                            .setAutoBitmapDownload(true)
                            .setAdsNumber(adsNumber)
                            .setPrimaryImageSize(6)
                            .setSecondaryImageSize(2),
                    new AdEventListener() {     // Callback Listener
                        @Override
                        public void onReceiveAd(Ad arg0) {
                            try {
                                StartAppNativeAd startAppAds = (StartAppNativeAd) arg0;
                                // Native Ad received
                                ArrayList<NativeAdDetails> ads = startAppAds.getNativeAds();    // get NativeAds list
                                mNativeAdsList = ads;
                            } catch (Exception ignored) {
                            }
                        }

                        @Override
                        public void onFailedToReceiveAd(Ad arg0) {
                            // Native Ad failed to receive
                            Logger.INSTANCE.e(TAG, "Error while loading Ad");
                        }
                    });
    }

    public void onDestroy(String tag) {
        listnerHashMap.remove(tag);
    }

    public void setRewardedVideoListener(IAdMobAds rewardedVideoListener) {
        this.rewardedVideoListener = rewardedVideoListener;
    }
}
