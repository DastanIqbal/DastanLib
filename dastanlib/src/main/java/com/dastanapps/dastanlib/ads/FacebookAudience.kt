package com.dastanapps.dastanlib.ads

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.dastanapps.dastanlib.DastanApp
import com.dastanapps.dastanlib.analytics.DAnalytics
import com.dastanapps.dastanlib.log.Logger
import com.facebook.ads.*
import java.util.*

/**
 * Created by dastaniqbal on 22/08/2017.
 * 22/08/2017 12:24
 */

class FacebookAudience : IMarvelAds {
    private val context = DastanApp.getInstance()
    private val listnerHashMap = HashMap<String, IFBNativeAds>()
    private var nativeAdScrollView: NativeAdScrollView? = null

    init {
        val testDevices = ArrayList<String>()
        //Testing Device HashCode
        if (!DastanApp.getInstance().isRelease) {
            AdSettings.addTestDevices(testDevices)
        }
    }

    fun setNativeAdsListener(fbNativeAds: IFBNativeAds, tag: String) {
        listnerHashMap.put(tag, fbNativeAds)
    }

    //**************************
    // Banner Ads
    //**************************
    fun showBanner(adContainer: ViewGroup) {
        // Instantiate an AdView view
        val adView = AdView(context, DastanApp.getInstance().fbBannerAdId,
                AdSize.BANNER_HEIGHT_50)

        // Add the ad view to container
        adContainer.addView(adView)

        adView.setAdListener(object : AdListener {
            override fun onError(ad: com.facebook.ads.Ad, adError: AdError) {
                Logger.onlyDebug("Error: " + adError.errorMessage)
                callAdError(adError.errorMessage)
            }

            override fun onAdLoaded(ad: com.facebook.ads.Ad) {
                Logger.onlyDebug("Ad loaded!")
                callAdLoaded(adView)
            }

            override fun onAdClicked(ad: com.facebook.ads.Ad) {
                Logger.onlyDebug("Ad clicked!")
                DAnalytics.getInstance().sendLogs("FBBannerAd", "clicked")
            }

            override fun onLoggingImpression(ad: com.facebook.ads.Ad) {
                Logger.onlyDebug("Impression logged!")
                DAnalytics.getInstance().sendLogs("FBBannerAd", "clicked")
            }
        })

        // Request an ad
        adView.loadAd()
    }

    //**************************
    // Interstial Ads
    //**************************
    fun loadInterstitial(tag: String) {
        // Instantiate an InterstitialAd object
        val interstitialAd = InterstitialAd(context, DastanApp.getInstance().fbInterstialAdId)
        interstitialAd.loadAd()

        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: com.facebook.ads.Ad) {
                Logger.onlyDebug("Interstitial Ad displayed!")
                listnerHashMap[tag]?.addDisplayed()
            }

            override fun onInterstitialDismissed(ad: com.facebook.ads.Ad) {
                Logger.onlyDebug("Interstitial Ad dismissed!")
                //callAdDismissed()
                listnerHashMap[tag]?.adDismissed()
            }

            override fun onError(ad: com.facebook.ads.Ad, adError: com.facebook.ads.AdError) {
                Logger.onlyDebug("Error: " + adError.errorMessage)
                //callAdError(adError.errorMessage)
                listnerHashMap[tag]?.adError(adError.errorMessage)
            }

            override fun onAdLoaded(ad: com.facebook.ads.Ad) {
                Logger.onlyDebug("Ad Loaded!")
//                Toast.makeText(context, "Ad Loaded!",
//                        Toast.LENGTH_LONG).show()
//                callAdLoaded(interstitialAd!!)
                listnerHashMap[tag]?.adLoaded(interstitialAd)
            }

            override fun onAdClicked(ad: com.facebook.ads.Ad) {
                Logger.onlyDebug("Interstitial Ad clicked!")
                DAnalytics.getInstance().sendLogs("FBInterstialAd", "clicked")
            }

            override fun onLoggingImpression(ad: com.facebook.ads.Ad) {
                Logger.onlyDebug("Impression logged!")
                DAnalytics.getInstance().sendLogs("FBInterstialAd", "impression")
            }
        })
    }

    fun showInterstitial(interstitialAd: InterstitialAd) {
        interstitialAd.show()
    }

    //**************************
    // Native Ads
    //**************************
    fun showNativeAd(tag: String) {
        val nativeAd = NativeAd(context, DastanApp.getInstance().fbNativeAdId)
        nativeAd.setAdListener(object : AdListener {

            override fun onError(ad: com.facebook.ads.Ad, error: AdError) {
                // Ad error callback
                // callAdError(error.errorMessage)
                listnerHashMap[tag]?.adError(error.errorMessage)
                Logger.onlyDebug("Error: " + error.errorMessage)
            }

            override fun onAdLoaded(ad: com.facebook.ads.Ad) {
                Logger.onlyDebug("Ad Loaded!")
                listnerHashMap[tag]?.adLoaded(nativeAd)
                //callAdLoaded(nativeAd as NativeAd)
            }

            override fun onAdClicked(ad: com.facebook.ads.Ad) {
                Logger.onlyDebug("Interstitial Ad clicked!")
                // Ad clicked callback
                DAnalytics.getInstance().sendLogs("FBNativeAd", "clicked")
            }

            override fun onLoggingImpression(ad: com.facebook.ads.Ad) {
                Logger.onlyDebug("Impression logged!")
                // On logging impression callback
                DAnalytics.getInstance().sendLogs("FBNativeAd", "impression")
            }
        })

        // Request an ad
        nativeAd.loadAd()
    }

    fun setAdIcon(nativeAd: NativeAd, showOnthis: ImageView?) {
        if (showOnthis == null)
            throw RuntimeException("ImageView cannot be null")
        // Download and display the ad icon.
        val adIcon = nativeAd.adIcon
        NativeAd.downloadAndDisplayImage(adIcon, showOnthis)
    }

    fun setFBMedia(nativeAd: NativeAd, nativeAdMedia: MediaView) {
        // Download and display the cover image.
        nativeAdMedia.setNativeAd(nativeAd)
    }

    fun enableAdChoiceIcon(nativeAd: NativeAd, adChoicesContainer: ViewGroup) {
        val adChoicesView = AdChoicesView(context, nativeAd, true)
        adChoicesContainer.addView(adChoicesView)
    }

    // Register the Title and CTA button to listen for clicks.
    fun setCallToAction(nativeAd: NativeAd, clickableViews: List<View>, nativeAdContainer: ViewGroup) {
        nativeAd.registerViewForInteraction(nativeAdContainer, clickableViews)
    }

    fun setCallToAction(nativeAd: NativeAd, nativeAdContainer: View) {
        nativeAd.registerViewForInteraction(nativeAdContainer)
    }

    fun showNativeAdInHScroll(num: Int, adContainer: ViewGroup) {
        // Initialize a NativeAdsManager and request 5 ads
        val manager = NativeAdsManager(context, DastanApp.getInstance().fbNativeAdId, num)
        manager.setListener(object : NativeAdsManager.Listener {
            override fun onAdsLoaded() {
                nativeAdScrollView = NativeAdScrollView(context, manager,
                        NativeAdView.Type.HEIGHT_300)
                nativeAdScrollView?.orientation = LinearLayout.VERTICAL
                adContainer.addView(nativeAdScrollView)
            }

            override fun onAdError(adError: AdError) {
                // Ad error callback
                listnerHashMap.keys
                        .map { listnerHashMap[it] as IFBNativeAds }
                        .forEach { it.adError(adError.errorMessage) }
            }
        })
        manager.loadAds(NativeAd.MediaCacheFlag.ALL)
    }

    private fun callAdError(errorMessage: String) {
        val startAppKeys = listnerHashMap.keys
        startAppKeys
                .map { listnerHashMap[it] as IFBNativeAds }
                .forEach { it.adError(errorMessage) }
    }

    private fun callAdLoaded(nativeAd: Any) {
        val startAppKeys = listnerHashMap.keys
        startAppKeys
                .map { listnerHashMap[it] as IFBNativeAds }
                .forEach {
                    it.adLoaded(nativeAd)
                }
    }

    private fun callAdDismissed() {
        val startAppKeys = listnerHashMap.keys
        startAppKeys
                .map { listnerHashMap[it] as IFBNativeAds }
                .forEach { it.adDismissed() }
    }

    fun onDestroy(tag: String) {
        listnerHashMap.remove(tag)
    }
}
