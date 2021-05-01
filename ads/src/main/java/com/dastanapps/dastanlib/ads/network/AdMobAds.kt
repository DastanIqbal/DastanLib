package com.dastanapps.dastanlib.ads.network

import android.app.Activity
import android.util.DisplayMetrics
import android.view.Display
import com.dastanapps.dastanlib.DastanAdsApp
import com.dastanapps.dastanlib.ads.AdsBase
import com.dastanapps.dastanlib.ads.interfaces.IAdMobAds
import com.dastanapps.dastanlib.ads.interfaces.IAdsLifecycle
import com.dastanapps.dastanlib.ads.interfaces.IMarvelAds
import com.dastanapps.dastanlib.analytics.DAnalytics
import com.dastanapps.dastanlib.log.Logger
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback


/**
 * Created by dastaniqbal on 12/12/2017.
 * ask2iqbal@gmail.com
 * 12/12/2017 2:01
 */
class AdMobAds : AdsBase(), IAdsLifecycle {
    private val adRequest by lazy {
        AdRequest.Builder().build()
    }

    fun setAdsListener(adsListener: IAdMobAds, tag: String) {
        listnerHashMap[tag] = adsListener
    }

    fun setAdsListener(adsListener: IMarvelAds, tag: String) {
        listnerHashMap[tag] = adsListener
    }

    init {
        MobileAds.initialize(context) {
            DastanAdsApp.INSTANCE.adsConfiguration.adMobAppId
        }
    }

    fun loadRewardedVideoAd(tag: String) {
        if (DastanAdsApp.INSTANCE.disableAds()) return
        if (cachedRewardedAdMap[tag] != null) {
            listnerHashMap[tag]?.adLoaded(cachedRewardedAdMap[tag]!!)
            return
        }

        RewardedAd.load(
            DastanAdsApp.INSTANCE,
            DastanAdsApp.INSTANCE.adsConfiguration.adMobRewardId!!,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(p0: RewardedAd) {
                    cachedRewardedAdMap[tag] = p0

                    listnerHashMap[tag]?.adLoaded(p0)

                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    cachedRewardedAdMap[tag] = null

                    listnerHashMap[tag]?.adError(p0.message)
                }

            })
    }

    fun showRewardedVideo(activity: Activity, tag: String) {
        val rewardedAd = cachedRewardedAdMap[tag]
        if (rewardedAd is RewardedAd) {
            rewardedAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdShowedFullScreenContent() {
                    cachedRewardedAdMap[tag] = null

                    listnerHashMap[tag]?.adDisplayed()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    listnerHashMap[tag]?.adError(p0.message)
                }

                override fun onAdDismissedFullScreenContent() {
                    listnerHashMap[tag]?.adDismissed(tag)
                }
            }
            rewardedAd.show(activity) {
                if (listnerHashMap[tag] != null)
                    (listnerHashMap[tag] as IAdMobAds).rewarded()
            }
        }
    }

    fun loadBannerAds(activity: Activity, tag: String, bannerId: String) {
        if (DastanAdsApp.INSTANCE.disableAds()) return
        if (cachedBannerAdMap[tag] != null) return

        val adView = AdView(DastanAdsApp.INSTANCE)
        adView.adSize = bannerAdSize(activity)
        adView.adUnitId = bannerId
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Logger.onlyDebug("admob:Banner:onAdLoaded")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adLoaded(adView)
                cachedBannerAdMap[tag] = adView
            }

            override fun onAdFailedToLoad(errorCode: LoadAdError) {
                Logger.onlyDebug("admob:Banner:onAdFailedToLoad")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adError(errorCode.toString())
            }

            override fun onAdOpened() {
                Logger.onlyDebug("admob:Banner:onAdOpened")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adDisplayed()
            }

            override fun onAdClosed() {
                Logger.onlyDebug("admob:Banner:onAdClosed")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adDismissed(tag)
            }

            override fun onAdImpression() {
                Logger.onlyDebug("admob:Banner:onAdImpression")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adDisplayed()
            }
        }
        if (!DastanAdsApp.INSTANCE.disableAds())
            adView.loadAd(AdRequest.Builder().build())
    }

    fun showBanner(tag: String) {
        val adView = cachedBannerAdMap[tag]
        if (adView != null) {
            listnerHashMap[tag]?.adLoaded(adView)
        } else {
            listnerHashMap[tag]?.adError("No Ads Found")
        }
    }

    fun loadInterstitialAd(tag: String) {
        if (DastanAdsApp.INSTANCE.disableAds()) return
        if (cachedInterstialAdMap[tag] != null) {
            listnerHashMap[tag]?.adLoaded(cachedInterstialAdMap[tag]!!)
            return
        }

        InterstitialAd.load(
            DastanAdsApp.INSTANCE,
            DastanAdsApp.INSTANCE.adsConfiguration.adMobInterstialAd!!,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(p0: InterstitialAd) {
                    cachedInterstialAdMap[tag] = p0

                    listnerHashMap[tag]?.adLoaded(p0)

                    Logger.onlyDebug("admob:Interstitial:onAdLoaded")
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    cachedInterstialAdMap[tag] = null

                    listnerHashMap[tag]?.adError(p0.message)

                    Logger.onlyDebug("admob:Interstitial:onAdFailedToLoad")
                }
            }
        )
    }

    fun showInterstitialAd(activity: Activity, tag: String) {
        val interstitialAd = cachedInterstialAdMap[tag]
        if (interstitialAd is InterstitialAd) {
            interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdShowedFullScreenContent() {
                    cachedInterstialAdMap[tag] = null

                    listnerHashMap[tag]?.adDisplayed()

                    Logger.onlyDebug("admob:Interstitial:onAdShowedFullScreenContent")
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    listnerHashMap[tag]?.adError(p0.message)

                    Logger.onlyDebug("admob:Interstitial:onAdFailedToShowFullScreenContent")
                }

                override fun onAdDismissedFullScreenContent() {
                    listnerHashMap[tag]?.adDismissed(tag)

                    Logger.onlyDebug("admob:Interstitial:onAdDismissedFullScreenContent")
                }


            }

            interstitialAd.show(activity)
            DAnalytics.getInstance().sendLogs("Ads", "show", "AdMob InterstialAds")
        }
    }

    /*fun loadNativeAds(template: TemplateView, tag: String) {
        template.visibility = View.GONE
        val adLoader = AdLoader.Builder(
            DastanAdsApp.INSTANCE,
            DastanAdsApp.INSTANCE.adsConfiguration.adMobNativeAd!!
        ).withAdListener(object : AdListener() {
            override fun onAdLoaded() {
                Logger.onlyDebug("admob:NaitveAds:onAdLoaded")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adLoaded("")
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                Logger.onlyDebug("admob:NaitveAds:onAdFailedToLoad")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adError(p0.toString())
            }

            override fun onAdOpened() {
                Logger.onlyDebug("admob:NaitveAds:onAdOpened")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adDisplayed()
            }

            override fun onAdClosed() {
                Logger.onlyDebug("admob:NaitveAds:onAdClosed")
                if (listnerHashMap[tag] != null)
                    listnerHashMap[tag]?.adDismissed(tag)
            }
        }).forUnifiedNativeAd {
            val styles = NativeTemplateStyle.Builder()
                .withMainBackgroundColor(ColorDrawable(Color.WHITE)).build()

            template.visibility = View.VISIBLE
            template.setStyles(styles)
            template.setNativeAd(it)
        }.build()
        if (!DastanAdsApp.INSTANCE.disableAds())
            adLoader.loadAd(AdRequest.Builder().build())
    }*/

    override fun onStart() {}

    override fun onResume() {}

    override fun onPause() {}

    override fun onStop() {}

    override fun onDestroy(tag: String) {
        super.removeListener(tag)
    }

    private fun bannerAdSize(activity: Activity): AdSize? {
        val display: Display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density
        val adWidth = (widthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
    }
}