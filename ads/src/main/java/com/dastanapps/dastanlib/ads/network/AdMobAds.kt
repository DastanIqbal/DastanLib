package com.dastanapps.dastanlib.ads.network

import com.dastanapps.dastanlib.DastanAdsApp
import com.dastanapps.dastanlib.ads.AdsBase
import com.dastanapps.dastanlib.ads.interfaces.IAdMobAds
import com.dastanapps.dastanlib.ads.interfaces.IAdsLifecycle
import com.dastanapps.dastanlib.ads.interfaces.IMarvelAds
import com.dastanapps.dastanlib.analytics.DAnalytics
import com.dastanapps.dastanlib.log.Logger
import com.google.android.gms.ads.*
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener

/**
 * Created by dastaniqbal on 12/12/2017.
 * dastanIqbal@marvelmedia.com
 * 12/12/2017 2:01
 */
class AdMobAds : AdsBase(), IAdsLifecycle {
    private var mRewardedVideoAd: RewardedVideoAd
    fun setAdsListener(adsListener: IAdMobAds, tag: String) {
        listnerHashMap[tag] = adsListener
    }

    fun setAdsListener(adsListener: IMarvelAds, tag: String) {
        listnerHashMap[tag] = adsListener
    }

    init {
        MobileAds.initialize(context, DastanAdsApp.INSTANCE.adsConfiguration.adMobAppId)
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context)
    }

    fun loadRewardedVideoAd(tag: String) {
        if (!DastanAdsApp.INSTANCE.disableAds)
            mRewardedVideoAd.loadAd(DastanAdsApp.INSTANCE.adsConfiguration.adMobRewardId, AdRequest.Builder().build())

        mRewardedVideoAd.rewardedVideoAdListener = object : RewardedVideoAdListener {
            override fun onRewardedVideoCompleted() {
                Logger.onlyDebug("admob:rewardVideo:onRewardedVideoCompleted")
                if (listnerHashMap[tag] != null)
                    (listnerHashMap[tag] as IAdMobAds).completed()
            }

            override fun onRewardedVideoAdClosed() {
                Logger.onlyDebug("admob:rewardVideo:onRewardedVideoAdClosed")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adDismissed(tag)
            }

            override fun onRewardedVideoAdLeftApplication() {
                Logger.onlyDebug("admob:rewardVideo:onRewardedVideoAdLeftApplication")
            }

            override fun onRewardedVideoAdLoaded() {
                Logger.onlyDebug("admob:rewardVideo:onRewardedVideoAdLoaded")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adLoaded("")
            }

            override fun onRewardedVideoAdOpened() {
                Logger.onlyDebug("admob:rewardVideo:onRewardedVideoAdOpened")
                if (listnerHashMap[tag] != null)
                    (listnerHashMap[tag] as IAdMobAds).clicked()
            }

            override fun onRewarded(p0: RewardItem?) {
                Logger.onlyDebug("admob:rewardVideo:onRewarded")
                if (listnerHashMap[tag] != null)
                    (listnerHashMap[tag] as IAdMobAds).rewarded()
            }

            override fun onRewardedVideoStarted() {
                Logger.onlyDebug("admob:rewardVideo:onRewardedVideoStarted")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.addDisplayed()
            }

            override fun onRewardedVideoAdFailedToLoad(p0: Int) {
                Logger.onlyDebug("admob:rewardVideo:onRewardedVideoAdFailedToLoad:$p0")
                when (p0) {
                    0 -> {
                        listnerHashMap[tag]?.adError("Internal Error")
                    }
                    1 -> {
                        if (listnerHashMap[tag] != null) listnerHashMap[tag]?.adError("Invalid Request")
                    }
                    2 -> {
                        if (listnerHashMap[tag] != null) listnerHashMap[tag]?.adError("Network Error")
                    }
                    3 -> {
                        if (listnerHashMap[tag] != null) listnerHashMap[tag]?.adError("No Fill")
                    }
                }
            }
        }
    }

    fun showRewardedVideo() {
        if (mRewardedVideoAd.isLoaded) {
            mRewardedVideoAd.show()
        }
    }

    fun loadBannerAds(tag: String, bannerId: String) {
        if (cahcedBannerAdMap[tag] != null) return
        val adView = AdView(DastanAdsApp.INSTANCE)
        adView.adSize = AdSize.SMART_BANNER
        adView.adUnitId = bannerId
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Logger.onlyDebug("admob:Banner:onAdLoaded")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adLoaded(adView)
                cahcedBannerAdMap[tag] = adView
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                Logger.onlyDebug("admob:Banner:onAdFailedToLoad")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adError(errorCode.toString())
            }

            override fun onAdOpened() {
                Logger.onlyDebug("admob:Banner:onAdOpened")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.addDisplayed()
            }

            override fun onAdLeftApplication() {
                Logger.onlyDebug("admob:Banner:onAdLeftApplication")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adDismissed(tag)
            }

            override fun onAdClosed() {
                Logger.onlyDebug("admob:Banner:onAdClosed")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adDismissed(tag)
            }
        }
        if (!DastanAdsApp.INSTANCE.disableAds)
            adView.loadAd(AdRequest.Builder().build())
    }

    fun showBanner(tag: String) {
        val adView = cahcedBannerAdMap[tag]
        if (adView != null) {
            if (listnerHashMap[tag] != null)
                listnerHashMap[tag]?.adLoaded(adView)
        } else {
            if (listnerHashMap[tag] != null)
                listnerHashMap[tag]?.adError("No Ads Found")
        }
    }

    fun loadInterstialAd(tag: String) {
        val interstitialAd = InterstitialAd(DastanAdsApp.INSTANCE)
        interstitialAd.adUnitId = DastanAdsApp.INSTANCE.adsConfiguration.adMobInterstialAd

        if (!DastanAdsApp.INSTANCE.disableAds)
            interstitialAd.loadAd(AdRequest.Builder().build())

        interstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Logger.onlyDebug("admob:Interstial:onAdLoaded")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adLoaded(interstitialAd)
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                Logger.onlyDebug("admob:Interstial:onAdFailedToLoad")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adError(errorCode.toString())
            }

            override fun onAdOpened() {
                Logger.onlyDebug("admob:Interstial:onAdOpened")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.addDisplayed()
            }

            override fun onAdLeftApplication() {
                Logger.onlyDebug("admob:Interstial:onAdLeftApplication")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adDismissed(tag)
            }

            override fun onAdClosed() {
                Logger.onlyDebug("admob:Interstial:onAdClosed")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adDismissed(tag)
            }
        }
    }

    fun showInterstialAd(tag: String, inetrstialAd: Any) {
        if (inetrstialAd is InterstitialAd && inetrstialAd.isLoaded) {
            inetrstialAd.show()
            DAnalytics.getInstance().sendLogs("Ads", "onSaveClicked", "AdMob InterstialAds")
        } else Logger.onlyDebug("admob:InterstitialAdNotLoaded")
    }

    fun isAdAvailable(): Boolean {
        return mRewardedVideoAd.isLoaded
    }

    override fun onStart() {}

    override fun onResume() {
        mRewardedVideoAd.resume(context)
    }

    override fun onPause() {
        mRewardedVideoAd.pause(context)
    }

    override fun onStop() {}

    override fun onDestroy(tag: String) {
        super.removeListener(tag)
        mRewardedVideoAd.destroy(context)
    }
}