package com.dastanapps.dastanlib.ads.network;

import android.app.Activity;
import android.content.Context;

import com.applovin.adview.AppLovinAdView;
import com.applovin.adview.AppLovinIncentivizedInterstitial;
import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.nativeAds.AppLovinNativeAd;
import com.applovin.nativeAds.AppLovinNativeAdLoadListener;
import com.applovin.nativeAds.AppLovinNativeAdPrecacheListener;
import com.applovin.nativeAds.AppLovinNativeAdService;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdClickListener;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdRewardListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinAdVideoPlaybackListener;
import com.applovin.sdk.AppLovinErrorCodes;
import com.applovin.sdk.AppLovinPostbackListener;
import com.applovin.sdk.AppLovinSdk;
import com.dastanapps.dastanlib.DastanAdsApp;
import com.dastanapps.dastanlib.ads.interfaces.IAppLovin;
import com.dastanapps.dastanlib.ads.interfaces.IMarvelAds;
import com.dastanapps.dastanlib.analytics.DAnalytics;
import com.dastanapps.dastanlib.log.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dastaniqbal on 16/03/2017.
 * dastanIqbal@marvelmedia.com
 * 16/03/2017 6:33
 */

public class AppLovin {
    private final String TAG = AppLovin.class.getSimpleName();
    Context context = DastanAdsApp.INSTANCE;
    AppLovinSdk sdk = AppLovinSdk.getInstance(context);
    AppLovinIncentivizedInterstitial myIncent = AppLovinIncentivizedInterstitial.create(context);
    private HashMap<String, IMarvelAds> listnerHashMap = new HashMap<>();

    public void setIAppLovin(IAppLovin iAppLovin, String tag) {
        listnerHashMap.put(tag, iAppLovin);
    }

    private AppLovinAdLoadListener adLoadListener = new AppLovinAdLoadListener() {
        @Override
        public void adReceived(AppLovinAd appLovinAd) {
            Logger.INSTANCE.d(TAG, "Interstitial loaded");
            if (!listnerHashMap.isEmpty()) {
                Set<String> startAppKeys = listnerHashMap.keySet();
                for (String key : startAppKeys) {
                    IAppLovin appLovinListener = (IAppLovin) listnerHashMap.get(key);
                    appLovinListener.currentLoadedAd(appLovinAd);
                }
            } else
                DAnalytics.getInstance().sendLogs("AppLovin", "appLovin", "not Initialized");
        }

        @Override
        public void failedToReceiveAd(int errorCode) {

            // Look at AppLovinErrorCodes.java for list of error codes

            if (errorCode == AppLovinErrorCodes.NO_FILL) {
                Logger.INSTANCE.d(TAG, "No-fill: No ads are currently available for this device/country");
            } else {
                Logger.INSTANCE.d(TAG, "Interstitial failed to load with error code " + errorCode);
            }
        }
    };
    private AppLovinAdDisplayListener adDisplayListener = new AppLovinAdDisplayListener() {
        @Override
        public void adDisplayed(AppLovinAd appLovinAd) {
            Logger.INSTANCE.d(TAG, "Interstitial Displayed");
        }

        @Override
        public void adHidden(AppLovinAd appLovinAd) {
            Logger.INSTANCE.d(TAG, "Interstitial Hidden");
        }
    };
    private AppLovinAdClickListener adClickListener = new AppLovinAdClickListener() {
        @Override
        public void adClicked(AppLovinAd appLovinAd) {
            Logger.INSTANCE.d(TAG, "Interstitial Clicked");
        }
    };
    private AppLovinAdVideoPlaybackListener adVideoPlayListener = new AppLovinAdVideoPlaybackListener() {
        @Override
        public void videoPlaybackBegan(AppLovinAd appLovinAd) {
            Logger.INSTANCE.d(TAG, "Video Started");
        }

        @Override
        public void videoPlaybackEnded(AppLovinAd appLovinAd, double percentViewed, boolean wasFullyViewed) {
            Logger.INSTANCE.d(TAG, "Video Ended");
        }
    };
    // Reward Listener
    AppLovinAdRewardListener adRewardListener = new AppLovinAdRewardListener() {
        @Override
        public void userRewardVerified(AppLovinAd appLovinAd, Map map) {
            // AppLovin servers validated the reward. Refresh user balance from your server.  We will also pass the number of coins
            // awarded and the name of the currency.  However, ideally, you should verify this with your server before granting it.

            // i.e. - "Coins", "Gold", whatever you set in the dashboard.
            String currencyName = (String) map.get("currency");

            // For example, "5" or "5.00" if you've specified an amount in the UI.
            String amountGivenString = (String) map.get("amount");

            Logger.INSTANCE.d(TAG, "Rewarded " + amountGivenString + " " + currencyName);

            // By default we'll show a alert informing your user of the currency & amount earned.
            // If you don't want this, you can turn it off in the Manage Apps UI.
        }

        @Override
        public void userOverQuota(AppLovinAd appLovinAd, Map map) {
            // Your user has already earned the max amount you allowed for the day at this point, so
            // don't give them any more money. By default we'll show them a alert explaining this,
            // though you can change that from the AppLovin dashboard.

            Logger.INSTANCE.d(TAG, "Reward validation request exceeded quota with response: " + map);
        }

        @Override
        public void userRewardRejected(AppLovinAd appLovinAd, Map map) {
            // Your user couldn't be granted a reward for this view. This could happen if you've blacklisted
            // them, for example. Don't grant them any currency. By default we'll show them an alert explaining this,
            // though you can change that from the AppLovin dashboard.

            Logger.INSTANCE.d(TAG, "Reward validation request was rejected with response: " + map);
        }

        @Override
        public void validationRequestFailed(AppLovinAd appLovinAd, int responseCode) {
            if (responseCode == AppLovinErrorCodes.INCENTIVIZED_USER_CLOSED_VIDEO) {
                // Your user exited the video prematurely. It's up to you if you'd still like to grant
                // a reward in this case. Most developers choose not to. Note that this case can occur
                // after a reward was initially granted (since reward validation happens as soon as a
                // video is launched).
            } else if (responseCode == AppLovinErrorCodes.INCENTIVIZED_SERVER_TIMEOUT || responseCode == AppLovinErrorCodes.INCENTIVIZED_UNKNOWN_SERVER_ERROR) {
                // Some server issue happened here. Don't grant a reward. By default we'll show the user
                // a alert telling them to try again later, but you can change this in the
                // AppLovin dashboard.
            } else if (responseCode == AppLovinErrorCodes.INCENTIVIZED_NO_AD_PRELOADED) {
                // Indicates that the developer called for a rewarded video before one was available.
                // Note: This code is only possible when working with rewarded videos.
            }

            Logger.INSTANCE.d(TAG, "Reward validation request failed with error code: " + responseCode);
        }

        @Override
        public void userDeclinedToViewAd(AppLovinAd appLovinAd) {
            // This method will be invoked if the user selected "no" when asked if they want to view an ad.
            // If you've disabled the pre-video prompt in the "Manage Apps" UI on our website, then this method won't be called.

            Logger.INSTANCE.d(TAG, "User declined to view ad");
        }
    };
    private AppLovinNativeAdLoadListener nativeAdsListener = new AppLovinNativeAdLoadListener() {
        @Override
        public void onNativeAdsLoaded(final List list) {
            // Native ads loaded; do something with this, e.g. render into your custom view.
            Logger.INSTANCE.d(TAG, "Native ad loaded, assets not retrieved yet.");
            if (!listnerHashMap.isEmpty()) {
                Set<String> startAppKeys = listnerHashMap.keySet();
                for (String key : startAppKeys) {
                    IAppLovin appLovinListener = (IAppLovin) listnerHashMap.get(key);
                    appLovinListener.loadNativeAds(list);
                }
            } else {
                DAnalytics.getInstance().sendLogs("AppLovin", "appLovin", "not Initialized in onNativeAdsLoaded");
            }
        }

        @Override
        public void onNativeAdsFailedToLoad(final int errorCode) {
            // Native ads failed to load for some reason, likely a network error.
            // Compare errorCode to the available constants in AppLovinErrorCodes.

            Logger.INSTANCE.d(TAG, "Native ad failed to load with error code " + errorCode);

            if (errorCode == AppLovinErrorCodes.NO_FILL) {
                // No ad was available for this placement
            }
            // else if (errorCode == .... ) { ... }
        }
    };
    private AppLovinPostbackListener appLovingPostbackListener = new AppLovinPostbackListener() {
        @Override
        public void onPostbackSuccess(String url) {
            // Impression tracked!
            Logger.INSTANCE.d(TAG, "Impression Tracked!");
        }

        @Override
        public void onPostbackFailure(String url, int errorCode) {
            // Impression could not be tracked. Retry the postback later.
            Logger.INSTANCE.d(TAG, "Impression Failed to Track!");
        }
    };

    public void init() {
        AppLovinSdk.initializeSdk(context);
        sdk = AppLovinSdk.getInstance(context);
        myIncent = AppLovinIncentivizedInterstitial.create(context);
    }

    public void showJustInterstialAds(Activity activity) {
        if (AppLovinInterstitialAd.isAdReadyToDisplay(activity)) {
            AppLovinInterstitialAd.show(activity);
            Logger.INSTANCE.d(TAG, "Interstitial Displayed");
            DAnalytics.getInstance().sendLogs("AppLovin", "interstialAds", "displayed");
        } else {
            Logger.INSTANCE.d(TAG, "Interstitial failed");
        }
    }

    //Step 1: init Interstial Ads
    public void initInterstialAds(Activity activity) {
        if (AppLovinInterstitialAd.isAdReadyToDisplay(activity)) {
            AppLovinInterstitialAdDialog interstitialAdDialog = AppLovinInterstitialAd.create(sdk, activity);

            //
            // Optional: Set ad load, ad display, ad click, and ad video playback callback listeners
            //
            interstitialAdDialog.setAdLoadListener(adLoadListener);

            interstitialAdDialog.setAdDisplayListener(adDisplayListener);

            interstitialAdDialog.setAdClickListener(adClickListener);

            // This will only ever be used if you have video ads enabled.
            interstitialAdDialog.setAdVideoPlaybackListener(adVideoPlayListener);

                    /*
                     NOTE: We recommend the use of placements (AFTER creating them in your dashboard):
                     interstitialAdDialog.showAndRender(currentAd, "MANUAL_LOADING_SCREEN");
                     To learn more about placements, check out https://applovin.com/integration#androidPlacementsIntegration
                    */
        } else {
            // No ad is available to display.  Perform failover logic...
        }
    }

    //Step 2: show Interstial Ads
    public void showInterstialAds(Activity activity) {
        if (AppLovinInterstitialAd.isAdReadyToDisplay(activity)) {
            AppLovinInterstitialAdDialog interstitialAdDialog = AppLovinInterstitialAd.create(sdk, activity);
            interstitialAdDialog.show();
        }
    }

    //Step 1: Load Manually Interstial Ads
    public void initManualInterstialAds(Activity activity) {
        AppLovinSdk.getInstance(DastanAdsApp.INSTANCE).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, adLoadListener);
        initInterstialAds(activity);
    }

    //Step 2: show Manually Interstial Ads
    public void showInterstialAdsManually(Activity activity, AppLovinAd appLovinAd) {
        if (AppLovinInterstitialAd.isAdReadyToDisplay(activity)) {
            AppLovinInterstitialAdDialog interstitialAdDialog = AppLovinInterstitialAd.create(sdk, activity);
            interstitialAdDialog.showAndRender(appLovinAd);
        }
    }

    //Step 1: preloadVideo
    public void preloadVideo() {
        myIncent.preload(adLoadListener);
    }

    //Step 2: showVideo
    public void showVideoAds(Activity activity) {
        // For rewarded videos
        if (myIncent.isAdReadyToDisplay()) {
            myIncent.show(activity, adRewardListener, adVideoPlayListener, adDisplayListener, adClickListener);
        }
    }

    public void showBannerAds(AppLovinAdView adView) {
        if (adView == null) return;
        adView.setAdLoadListener(new AppLovinAdLoadListener() {
            @Override
            public void adReceived(final AppLovinAd ad) {
                Logger.INSTANCE.d(TAG, "Banner loaded");
            }

            @Override
            public void failedToReceiveAd(final int errorCode) {
                // Look at AppLovinErrorCodes.java for list of error codes
                Logger.INSTANCE.d(TAG, "Banner failed to load with error code " + errorCode);
            }
        });

        adView.setAdDisplayListener(adDisplayListener);

        adView.setAdClickListener(adClickListener);
        adView.loadNextAd();
    }


    //Step 1: Load Native Ads
    public void loadNativeAds(int num) {
        if (sdk != null && sdk.getPostbackService() != null)
            sdk.getNativeAdService().loadNativeAds(num, nativeAdsListener);
    }

    //Step 2: Retrieve Image Resources
    public void preacheResources(AppLovinNativeAdService nativeAdService, final List<AppLovinNativeAd> nativeAds) {
        for (final AppLovinNativeAd nativeAd : nativeAds) {
            nativeAdService.precacheResources(nativeAd, new AppLovinNativeAdPrecacheListener() {
                @Override
                public void onNativeAdImagesPrecached(final AppLovinNativeAd appLovinNativeAd) {
                    Logger.INSTANCE.d(TAG, "NaitveAd Preached" + nativeAds.indexOf(appLovinNativeAd));
                    if (!listnerHashMap.isEmpty()) {
                        Set<String> startAppKeys = listnerHashMap.keySet();
                        for (String key : startAppKeys) {
                            IAppLovin appLovinListener = (IAppLovin) listnerHashMap.get(key);
                            appLovinListener.onNativeAdImagesPrecached(nativeAds.indexOf(appLovinNativeAd), appLovinNativeAd);
                        }
                    } else {
                        DAnalytics.getInstance().sendLogs("AppLovin", "NativeAdImagesPreached", "failed");
                    }
                }

                @Override
                public void onNativeAdVideoPreceached(AppLovinNativeAd appLovinNativeAd) {
                    // This example does not implement videos; see CarouselUINativeAdActivity for an example of a widget which does.
                    Logger.INSTANCE.d(TAG, "NativeAd Video Preached");
                }

                @Override
                public void onNativeAdImagePrecachingFailed(final AppLovinNativeAd appLovinNativeAd, final int errorCode) {
                    Logger.INSTANCE.d(TAG, "Failed to load images for native ad: " + errorCode);
                }

                @Override
                public void onNativeAdVideoPrecachingFailed(AppLovinNativeAd appLovinNativeAd, int i) {
                    // This example does not implement videos; see CarouselUINativeAdActivity for an example of a widget which does.
                    Logger.INSTANCE.d(TAG, "onNativeAdVideoPrecachingFailed");
                }
            });
        }
    }

    //Step 3: Track Impression
    public void trackImpression(AppLovinNativeAd nativead) {
        if (sdk != null && sdk.getPostbackService() != null)
            sdk.getPostbackService().dispatchPostbackAsync(nativead.getImpressionTrackingUrl(), appLovingPostbackListener);
    }

    //Step 4: Send Clicks
    public void sendClickEvent(AppLovinNativeAd nativeAd) {
        if (nativeAd != null)
            nativeAd.launchClickTarget(context);
    }

    public void onDestroy(String tag) {
        listnerHashMap.remove(tag);
    }
}
