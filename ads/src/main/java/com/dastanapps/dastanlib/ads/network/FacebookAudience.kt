package com.dastanapps.dastanlib.ads.network

import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.dastanapps.dastanlib.DastanAdsApp
import com.dastanapps.dastanlib.ads.AdsBase
import com.dastanapps.dastanlib.ads.interfaces.IFBNativeAds
import com.dastanapps.dastanlib.analytics.DAnalytics
import com.dastanapps.dastanlib.log.Logger
import com.facebook.ads.*

/**
 * Created by dastaniqbal on 22/08/2017.
 * ask2iqbal@gmail.com
 * 22/08/2017 12:24
 */

class FacebookAudience : AdsBase() {
    private var nativeAdScrollView: NativeAdScrollView? = null
    protected val mNativeAdsList = ArrayList<NativeAd>()

    init {
//        val testDevices = ArrayList<String>()
//        //Testing Device HashCode
//        testDevices.add("84D410C86FB9A3713B6AE1CD69B1EE3A")
//        if (!DastanAdsApp.INSTANCE.isRelease) {
//            AdSettings.addTestDevices(testDevices)
//        }
    }

    //**************************
    // Banner Ads
    //**************************

    fun setNativeAdsListener(fbNativeAds: IFBNativeAds, tag: String) {
        listnerHashMap[tag] = fbNativeAds
    }

    fun showBanner(adContainer: ViewGroup, tag: String) {
        // Slight delayed the banner to prevent UI blocking.
        Handler().postDelayed({
            // Instantiate an AdView view
            val adView = AdView(context, DastanAdsApp.INSTANCE.adsConfiguration.fbBannerAdId, AdSize.BANNER_HEIGHT_50)

            // Add the ad view to container
            adContainer.addView(adView)

            adView.setAdListener(object : AdListener {
                override fun onError(ad: Ad, adError: AdError) {
                    try {
                        Logger.onlyDebug("Error: " + adError.errorMessage)
                        //  notifyAdError(adError.errorMessage)
                        listnerHashMap[tag]?.adError(adError.errorMessage)
                        DAnalytics.getInstance().sendLogs("Ads", "FBBannerAd:$tag", "onError:$adError.errorMessage")
                    } catch (e: Exception) {
                    }
                }

                override fun onAdLoaded(ad: Ad) {
                    Logger.onlyDebug("Ad loaded!")
                    //   notifyAdLoaded(adView)
                    listnerHashMap[tag]?.adLoaded(adView)
                    DAnalytics.getInstance().sendLogs("Ads", "FBBannerAd:$tag", "onAdLoaded")
                }

                override fun onAdClicked(ad: Ad) {
                    Logger.onlyDebug("Ad clicked!")
                    DAnalytics.getInstance().sendLogs("Ads", "FBBannerAd:$tag", "clicked")
                }

                override fun onLoggingImpression(ad: Ad) {
                    Logger.onlyDebug("Impression logged!")
                    DAnalytics.getInstance().sendLogs("Ads", "FBBannerAd:$tag", "clicked")
                }
            })
            if (!DastanAdsApp.INSTANCE.disableAds())
                adView.loadAd()
        }, 500)
    }

    fun loadBanner(tag: String, bannerId: String) {
        if (cahcedBannerAdMap[tag] != null) return
        // Instantiate an AdView view
        val adView = AdView(context, bannerId, AdSize.BANNER_HEIGHT_50)
        adView.setAdListener(object : AdListener {
            override fun onError(ad: Ad, adError: AdError) {
                try {
                    Logger.onlyDebug("Error: " + adError.errorMessage)
                    //  notifyAdError(adError.errorMessage)
                    listnerHashMap[tag]?.adError(adError.errorMessage)
                    DAnalytics.getInstance().sendLogs("Ads", "FBBannerAd:$tag", "onError:${adError.errorMessage}")
                } catch (e: Exception) {
                }
            }

            override fun onAdLoaded(ad: Ad) {
                Logger.onlyDebug("Ad loaded!")
                //   notifyAdLoaded(adView)
                listnerHashMap[tag]?.adLoaded(adView)
                cahcedBannerAdMap[tag] = adView
                DAnalytics.getInstance().sendLogs("Ads", "FBBannerAd:$tag", "onAdLoaded")
            }

            override fun onAdClicked(ad: Ad) {
                Logger.onlyDebug("Ad clicked!")
                DAnalytics.getInstance().sendLogs("Ads", "FBBannerAd:$tag", "clicked")
            }

            override fun onLoggingImpression(ad: Ad) {
                Logger.onlyDebug("Impression logged!")
                DAnalytics.getInstance().sendLogs("Ads", "FBBannerAd:$tag", "clicked")
            }
        })
        if (!DastanAdsApp.INSTANCE.disableAds())
            adView.loadAd()
    }

    fun showBanner(tag: String) {
        val adView = cahcedBannerAdMap[tag]
        if (adView != null) {
            listnerHashMap[tag]?.adLoaded(adView)
        } else {
            listnerHashMap[tag]?.adError("No Ads Found")
        }
    }

    //**************************
    // Interstial Ads
    //**************************
    fun loadInterstitial(tag: String) {
        // Instantiate an InterstitialAd object
        val interstitialAd = InterstitialAd(context, DastanAdsApp.INSTANCE.adsConfiguration.fbInterstialAdId)

        if (!DastanAdsApp.INSTANCE.disableAds())
            interstitialAd.loadAd()

        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad) {
                DAnalytics.getInstance().sendLogs("Ads", "FBInterstialAd:$tag", "onInterstitialDisplayed")
                Logger.onlyDebug("Interstitial Ad displayed!")
                listnerHashMap[tag]?.addDisplayed()
                //  notifyAdDiplayed() //Be cautious , will notify to all
            }

            override fun onInterstitialDismissed(ad: Ad) {
                DAnalytics.getInstance().sendLogs("Ads", "FBInterstialAd:$tag", "onInterstitialDismissed")
                Logger.onlyDebug("Interstitial Ad dismissed!")
                //callAdDismissed()
                listnerHashMap[tag]?.adDismissed(tag)
                //    notifyAdDismissed(tag) //Be cautious , will notify to all
            }

            override fun onError(ad: Ad, adError: AdError) {
                try {
                    DAnalytics.getInstance().sendLogs("Ads", "FBInterstialAd:$tag", "onError" + adError.errorMessage)
                    Logger.onlyDebug("Error: " + adError.errorMessage)
                    //callAdError(adError.errorMessage)
                    listnerHashMap[tag]?.adError(adError.errorMessage)
                } catch (e: Exception) {
                }
            }

            override fun onAdLoaded(ad: Ad) {
                DAnalytics.getInstance().sendLogs("Ads", "FBInterstialAd:$tag", "onAdLoaded")
                Logger.onlyDebug("Ad Loaded!")
//                Toast.makeText(context, "Ad Loaded!",
//                        Toast.LENGTH_LONG).show()
//                callAdLoaded(interstitialAd!!)
                listnerHashMap[tag]?.adLoaded(interstitialAd)
            }

            override fun onAdClicked(ad: Ad) {
                Logger.onlyDebug("Interstitial Ad clicked!")
                DAnalytics.getInstance().sendLogs("Ads", "FBInterstialAd:$tag", "clicked")
            }

            override fun onLoggingImpression(ad: Ad) {
                Logger.onlyDebug("Impression logged!")
                DAnalytics.getInstance().sendLogs("Ads", "FBInterstialAd:$tag", "impression")
            }
        })
    }

    private fun notifyToAllisDisplayed() {

    }

    fun showInterstitial(interstitialAd: InterstitialAd) {
        if (interstitialAd.isAdLoaded) {
            DAnalytics.getInstance().sendLogs("Ads", "onSaveClicked", "FB InterstialAds")
            interstitialAd.show()
        } else {
            Logger.onlyDebug("fb:interstialAdNotLoaded")
        }
    }

    //**************************
    // Native Ads
    //**************************
    fun showNativeAd(tag: String) {
        val nativeAd = NativeAd(context, DastanAdsApp.INSTANCE.adsConfiguration.fbNativeAdId)
        nativeAd.setAdListener(object : AdListener {

            override fun onError(ad: Ad, error: AdError) {
                try {
                    // Ad error callback
                    // callAdError(error.errorMessage)
                    listnerHashMap[tag]?.adError(error.errorMessage)
                    Logger.onlyDebug("Error: " + error.errorMessage)
                    DAnalytics.getInstance().sendLogs("Ads", "FBNativeAd:$tag", "onError:${error.errorMessage}")
                } catch (e: Exception) {
                }
            }

            override fun onAdLoaded(ad: Ad) {
                Logger.onlyDebug("Ad Loaded!")
                listnerHashMap[tag]?.adLoaded(nativeAd)
                //callAdLoaded(nativeAd as NativeAd)
                DAnalytics.getInstance().sendLogs("Ads", "FBNativeAd:$tag", "onAdLoaded")
            }

            override fun onAdClicked(ad: Ad) {
                Logger.onlyDebug("Interstitial Ad clicked!")
                // Ad clicked callback
                DAnalytics.getInstance().sendLogs("Ads", "FBNativeAd:$tag", "clicked")
            }

            override fun onLoggingImpression(ad: Ad) {
                Logger.onlyDebug("Impression logged!")
                // On logging impression callback
                DAnalytics.getInstance().sendLogs("Ads", "FBNativeAd:$tag", "impression")
            }
        })

        // Request an ad
        if (!DastanAdsApp.INSTANCE.disableAds())
            nativeAd.loadAd()
    }

    fun showNativeAd(tag: String, adId: String) {
        val nativeAd = NativeAd(context, adId)
        nativeAd.setAdListener(object : AdListener {

            override fun onError(ad: Ad, error: AdError) {
                // Ad error callback
                // callAdError(error.errorMessage)
                listnerHashMap[tag]?.adError(error.errorMessage)
                Logger.onlyDebug("Error: " + error.errorMessage)
                DAnalytics.getInstance().sendLogs("Ads", "FBNativeAd:$tag", "onError:${error.errorMessage}")
            }

            override fun onAdLoaded(ad: Ad) {
                Logger.onlyDebug("Ad Loaded!")
                listnerHashMap[tag]?.adLoaded(nativeAd)
                //callAdLoaded(nativeAd as NativeAd)
                DAnalytics.getInstance().sendLogs("Ads", "FBNativeAd:$tag", "onAdLoaded")
            }

            override fun onAdClicked(ad: Ad) {
                Logger.onlyDebug("Interstitial Ad clicked!")
                // Ad clicked callback
                DAnalytics.getInstance().sendLogs("Ads", "FBNativeAd:$tag", "clicked")
            }

            override fun onLoggingImpression(ad: Ad) {
                Logger.onlyDebug("Impression logged!")
                // On logging impression callback
                DAnalytics.getInstance().sendLogs("Ads", "FBNativeAd:$tag", "impression")
            }
        })

        // Request an ad
        if (!DastanAdsApp.INSTANCE.disableAds())
            nativeAd.loadAd()
    }

    fun loadNativeAds(tag: String, howMany: Int) {
        mNativeAdsList.clear()
        val handler = Handler()
        val delay = 2 * 1000L
        var i = 0

        handler.post(object : Runnable {
            override fun run() {
                val tag = tag + i
                val nativeAd = if (i == 0) NativeAd(context, DastanAdsApp.INSTANCE.adsConfiguration.fbNativeAdId) else NativeAd(context, DastanAdsApp.INSTANCE.adsConfiguration.fbSaveVideo2Id)
                nativeAd.setAdListener(object : AdListener {

                    override fun onError(ad: Ad, error: AdError) {
                        // Ad error callback
                        // callAdError(error.errorMessage)
                        listnerHashMap[tag]?.adError(error.errorMessage)
                        Logger.onlyDebug("Error: " + error.errorMessage)
                        DAnalytics.getInstance().sendLogs("Ads", "FBNativeAd:$tag", "onError:${error.errorMessage}")
                    }

                    override fun onAdLoaded(ad: Ad) {
                        Logger.onlyDebug("Ad Loaded!")
                        listnerHashMap[tag]?.adLoaded(nativeAd)
                        mNativeAdsList.add(nativeAd)
                        //callAdLoaded(nativeAd as NativeAd)
                        DAnalytics.getInstance().sendLogs("Ads", "FBNativeAd:$tag", "onAdLoaded")
                    }

                    override fun onAdClicked(ad: Ad) {
                        Logger.onlyDebug("Interstitial Ad clicked!")
                        // Ad clicked callback
                        DAnalytics.getInstance().sendLogs("Ads", "FBNativeAd:$tag", "clicked")
                    }

                    override fun onLoggingImpression(ad: Ad) {
                        Logger.onlyDebug("Impression logged!")
                        // On logging impression callback
                        DAnalytics.getInstance().sendLogs("Ads", "FBNativeAd:$tag", "impression")
                    }
                })

                // Request an ad
                if (!DastanAdsApp.INSTANCE.disableAds())
                    nativeAd.loadAd()

                i++

                if (i < howMany) handler.postDelayed(this, delay)
                else handler.removeCallbacks(this)
            }
        })
    }

    fun loadNativeAds(fbNativeId:String,tag: String, howMany: Int) {
        mNativeAdsList.clear()
        val handler = Handler()
        val delay = 2 * 1000L
        var i = 0

        handler.post(object : Runnable {
            override fun run() {
                val tag = tag + i
                val nativeAd = if (i == 0) NativeAd(context, fbNativeId) else NativeAd(context, DastanAdsApp.INSTANCE.adsConfiguration.fbSaveVideo2Id)
                nativeAd.setAdListener(object : AdListener {

                    override fun onError(ad: Ad, error: AdError) {
                        // Ad error callback
                        // callAdError(error.errorMessage)
                        listnerHashMap[tag]?.adError(error.errorMessage)
                        Logger.onlyDebug("Error: " + error.errorMessage)
                        DAnalytics.getInstance().sendLogs("Ads", "FBNativeAd:$tag", "onError:${error.errorMessage}")
                    }

                    override fun onAdLoaded(ad: Ad) {
                        Logger.onlyDebug("Ad Loaded!")
                        listnerHashMap[tag]?.adLoaded(nativeAd)
                        mNativeAdsList.add(nativeAd)
                        //callAdLoaded(nativeAd as NativeAd)
                        DAnalytics.getInstance().sendLogs("Ads", "FBNativeAd:$tag", "onAdLoaded")
                    }

                    override fun onAdClicked(ad: Ad) {
                        Logger.onlyDebug("Interstitial Ad clicked!")
                        // Ad clicked callback
                        DAnalytics.getInstance().sendLogs("Ads", "FBNativeAd:$tag", "clicked")
                    }

                    override fun onLoggingImpression(ad: Ad) {
                        Logger.onlyDebug("Impression logged!")
                        // On logging impression callback
                        DAnalytics.getInstance().sendLogs("Ads", "FBNativeAd:$tag", "impression")
                    }
                })

                // Request an ad
                if (!DastanAdsApp.INSTANCE.disableAds())
                    nativeAd.loadAd()

                i++

                if (i < howMany) handler.postDelayed(this, delay)
                else handler.removeCallbacks(this)
            }
        })
    }

    fun showAllNativeAds(): ArrayList<NativeAd> {
        return mNativeAdsList
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
        val manager = NativeAdsManager(context, DastanAdsApp.INSTANCE.adsConfiguration.fbNativeAdId, num)
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
        if (!DastanAdsApp.INSTANCE.disableAds())
            manager.loadAds(NativeAd.MediaCacheFlag.ALL)
    }
}
