package com.dastanapps.dastanlib.ads

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.dastanapps.dastanlib.DastanAdsApp
import com.dastanapps.dastanlib.ads.DastanAds.MarvelAdsId.FB_ADNETWORK
import com.dastanapps.dastanlib.ads.DastanAds.MarvelAdsId.STARTAPP_ADNETWORK
import com.dastanapps.dastanlib.ads.interfaces.*
import com.dastanapps.dastanlib.ads.network.*
import com.dastanapps.dastanlib.ads.network.mopub.MoPubInterstialAd
import com.dastanapps.dastanlib.ads.network.mopub.MoPubRewardVideoAd
import com.dastanapps.dastanlib.log.Logger
import com.dastanapps.dastanlib.utils.SPUtils
import com.facebook.ads.InterstitialAd
import com.facebook.ads.MediaView
import com.facebook.ads.NativeAd
import com.startapp.android.publish.ads.nativead.NativeAdDetails
import com.startapp.android.publish.adsCommon.StartAppAd
import java.util.*

/**
 * Created by dastaniqbal on 17/03/2017.
 * dastanIqbal@marvelmedia.com
 * 17/03/2017 4:45
 */

class DastanAds {
    private var appLovin: AppLovin? = null
    private var startAppAd: StartAppAds? = null
    private val facebookAudience = FacebookAudience()
    private var hscrollContatiner: ViewGroup? = null
    private var interstialAd: Any? = null
    private var adMobAds: AdMobAds? = null
    private var vungleAds: VungleAds? = null
    private var moPubInterstialAd: MoPubInterstialAd? = null
    private var moPubRewardVideoAd: MoPubRewardVideoAd? = null
    private var isRelease:Boolean = DastanAdsApp.INSTANCE.isRelease
    val startAppAds: StartAppAds
        get() {
            if (startAppAd == null) {
                startAppAd = StartAppAds()
            }
            if (DastanAdsApp.INSTANCE.isRelease)
                startAppAd!!.init()
            return startAppAd as StartAppAds
        }

    init {
        getAdMobAds()
        if(isRelease) {
            startAppAds
        }
    }

    fun getAdMobAds(): AdMobAds {
        if (adMobAds == null) {
            adMobAds = AdMobAds()
        }
        return adMobAds as AdMobAds
    }

    fun getMoPubInterstialAds(activity: Activity): MoPubInterstialAd {
        if (moPubInterstialAd == null) {
            moPubInterstialAd = MoPubInterstialAd(activity)
        }
        return moPubInterstialAd as MoPubInterstialAd
    }

    fun getMoPubRewardVideoAd(activity: Activity): MoPubRewardVideoAd {
        if (moPubRewardVideoAd == null) {
            moPubRewardVideoAd = MoPubRewardVideoAd(activity)
        }
        return moPubRewardVideoAd as MoPubRewardVideoAd
    }

    fun getVungleAds(): VungleAds {
        if (vungleAds == null) {
            vungleAds = VungleAds()
        }
        return vungleAds as VungleAds
    }


    private fun getAppLovin(): AppLovin {
        if (appLovin == null)
            appLovin = AppLovin()
        appLovin!!.init()
        return appLovin as AppLovin
    }

    fun showBannerAds(bannerContainer: ViewGroup, tag: String): DastanAds {
        facebookAudience.showBanner(bannerContainer, tag)
        return this
    }

    fun showBannerAds(tag: String) {
        if(isRelease) {
            facebookAudience.showBanner(tag)
            adMobAds?.showBanner(tag)
        }
    }

    fun loadInterstialAds(activity: Context, tag: String): DastanAds {
//        val adNetwork = SPUtils.readString("ads")
//        when (adNetwork) {
//            //"startapp" -> startAppAds.setInterstialAds(StartAppAd(DastanLibApp.INSTANCE))
//            "applovin" -> {
//                // getAppLovin().initInterstialAds(activity);
//            }
//            "fb" -> //Don't forget to setInterstialFBListener to call showInterstialAds method later
//                // call before loadInterstialAds
//                facebookAudience.loadInterstitial(tag)
//        }
        if(isRelease) {
            facebookAudience.loadInterstitial(tag)
            adMobAds?.loadInterstialAd(tag)
        }
        return this
    }

    fun setInterstialFBListener(tag: String): DastanAds {
        setAdsListener(object : IFBNativeAds {
            var isFbDisplayed = false;
            override fun addDisplayed() {
                Logger.onlyDebug("marvelads:adDisplayed")
                isFbDisplayed = true
            }

            override fun adDismissed(tag: String) {
                Logger.onlyDebug("marvelads:adDismissed")
                isFbDisplayed = false
            }

            override fun adError(error: String) {
                Logger.onlyDebug("marvelads:adError")
                isFbDisplayed = false
            }

            override fun adLoaded(adLoaded: Any) {
                Logger.onlyDebug("marvelads:adLoaded")
                when (adLoaded) {
                    is InterstitialAd -> {
                        interstialAd = adLoaded
                        isFbDisplayed = true
                    }
                    is com.google.android.gms.ads.InterstitialAd -> {
                        if (!isFbDisplayed) interstialAd = adLoaded
                    }
                    else -> Logger.onlyDebug("Unknown adLoaded")
                }
            }
        }, tag)
        return this
    }

    fun showInterstialAds(tag: String) {
//        val adNetwork = SPUtils.readString("ads")
//        if (adNetwork == "fb" && interstialAd != null) {
//            interstialAd!!.show()
//        }
        if(isRelease) {
            when (interstialAd) {
                is InterstitialAd ->
                    facebookAudience.showInterstitial(interstialAd as InterstitialAd)
                is com.google.android.gms.ads.InterstitialAd -> adMobAds?.showInterstialAd(tag, interstialAd as com.google.android.gms.ads.InterstitialAd)
                else -> loadInterstialAds(DastanAdsApp.INSTANCE, tag)
            }
        }
    }

    fun showExitsAds(activity: Activity, tag: String) {
        if(isRelease) {
            val adNetwork = SPUtils.readSP("ads","")
            when (adNetwork) {
                "startapp" -> startAppAds.backPressed(StartAppAd(activity))
                "applovin" -> getAppLovin().showInterstialAds(activity)
                "fb" -> //Don't forget to setInterstialFBListener to call showInterstialAds method later
                    // call before loadInterstialAds
                    facebookAudience.loadInterstitial(tag)
            }
        }
    }

    fun setNativeAds(tag: String) {
        if(isRelease) {
            val adNetwork = SPUtils.readSP("ads","")
            when (adNetwork) {
                "startapp" -> startAppAds.setupNativeAds()
                "applovin" -> //getAppLovin().;
                    startAppAds.setupNativeAds()
                "fb" -> facebookAudience.showNativeAd(tag)
            }
        }
    }

    fun setNativeAds(tag: String, adId: String) {
        if(isRelease) {
            val adNetwork = SPUtils.readSP("ads","")
            when (adNetwork) {
                "startapp" -> startAppAds.setupNativeAds()
                "applovin" -> //getAppLovin().;
                    startAppAds.setupNativeAds()
                "fb" -> facebookAudience.showNativeAd(tag, adId)
            }
        }
    }

    fun setNativeAdsWithNetwork(tag: String, adNetwork: String) {
        if(isRelease) {
            when (adNetwork) {
                STARTAPP_ADNETWORK -> startAppAds.setupSingleNativeAds(tag)
                "applovin" -> //getAppLovin().;
                    startAppAds.setupNativeAds()
                FB_ADNETWORK -> facebookAudience.showNativeAd(tag)
            }
        }
    }

    fun setNativeAds(tag: String, adId: String, adNetwork: String) {
        if(isRelease) {
            when (adNetwork) {
                STARTAPP_ADNETWORK -> startAppAds.setupSingleNativeAds(tag)
                "applovin" -> //getAppLovin().;
                    startAppAds.setupNativeAds()
                FB_ADNETWORK -> facebookAudience.showNativeAd(tag, adId)
            }
        }
    }

    fun setupNativeAds(tag: String, num: Int) {
        if(isRelease) {
            val adNetwork = SPUtils.readSP("ads","")
            when (adNetwork) {
                "startapp" -> startAppAds.setupNativeAds(tag, num)
                "applovin" -> startAppAds.setupNativeAds(tag, num)
                "fb" -> {
                    if (hscrollContatiner == null)
                        throw RuntimeException("You have to set hScrollContainer")
                    facebookAudience.showNativeAdInHScroll(num, hscrollContatiner!!)
                }
            }
        }
    }

    fun setAdsListener(marvelAdsListener: IMarvelAds, tag: String) {
        when (marvelAdsListener) {
            is IStartApp ->
                startAppAds.setIStartApp(marvelAdsListener, tag)
            is IAppLovin ->
                getAppLovin().setIAppLovin(marvelAdsListener, tag)
            is IFBNativeAds -> {
                facebookAudience.setNativeAdsListener(marvelAdsListener, tag)
                adMobAds?.setAdsListener(marvelAdsListener, tag)
            }
            is IAdMobAds ->
                adMobAds?.setAdsListener(marvelAdsListener, tag)
        }
    }

    fun setNativeAdsListener(marvelAdsListener: IMarvelAds, tag: String) {
        startAppAds.setIStartApp(marvelAdsListener, tag)
        facebookAudience.setNativeAdsListener(marvelAdsListener, tag)
    }

    fun removeCachedFB(tag: String) {
        MarvelAdsId.removeFBCachedAds(facebookAudience, tag)
        facebookAudience.removeListener(tag)
    }

    fun removeCachedAdmob(tag: String) {
        MarvelAdsId.removeAdMobCachedAds(adMobAds, tag)
        adMobAds?.removeListener(tag)
    }

    fun onDestory(tag: String) {
        //        getStartAppAds().onDestroy(tag);
        //        getAppLovin().onDestroy(tag);
        facebookAudience.removeListener(tag)
        adMobAds?.removeListener(tag)
    }

    fun onDestory(vararg tags: String) {
        tags.forEach {
            facebookAudience.removeListener(it)
        }
    }

    fun setHscrollContatiner(hscrollContatiner: ViewGroup) {
        this.hscrollContatiner = hscrollContatiner
    }

    fun setFBAdIcon(nativeAd: NativeAd, imageView: ImageView) {
        facebookAudience.setAdIcon(nativeAd, imageView)
    }

    fun setFBMedia(adLoaded: NativeAd, nativeAdMedia: MediaView?) {
        facebookAudience.setFBMedia(adLoaded, nativeAdMedia!!)
    }

    fun setFBAdChoices(adLoaded: NativeAd, adChoicesContaincer: ViewGroup) {
        facebookAudience.enableAdChoiceIcon(adLoaded, adChoicesContaincer)
    }

    fun fbCallToAction(nativeAd: NativeAd, clickableLists: List<View>, clickContainer: ViewGroup) {
        facebookAudience.setCallToAction(nativeAd, clickableLists, clickContainer)
    }

    fun fbCallToAction(nativeAd: NativeAd, clickContainer: View) {
        facebookAudience.setCallToAction(nativeAd, clickContainer)
    }

    fun destroyMoPub() {
        if (moPubInterstialAd != null) moPubInterstialAd!!.destroy()
        moPubInterstialAd = null
    }

    fun loadNativeAds(tag: String, howMany: Int) {
        if(isRelease) {
            facebookAudience.loadNativeAds(tag, howMany)
            startAppAds.setupNativeAds(tag, howMany)
        }
    }

    fun loadNativeAds(adId:String,tag: String, howMany: Int) {
        if(isRelease) {
            facebookAudience.loadNativeAds(adId,tag, howMany)
            startAppAds.setupNativeAds(tag, howMany)
        }
    }

    fun showAllFBNativeAds(): ArrayList<NativeAd> {
        return facebookAudience.showAllNativeAds()
    }

    fun showAllStartAppNativeAds(): ArrayList<NativeAdDetails> {
        return (startAppAds.mNativeAdsList as ArrayList<NativeAdDetails>)
    }

    fun loadBanner(bannerTag: String) {
        if(isRelease) {
            val fbId = MarvelAdsId.getFBId(bannerTag)
            if (fbId.isNotEmpty())
                facebookAudience.loadBanner(bannerTag, fbId)

            val adMobId = MarvelAdsId.getAdmobId(bannerTag)
            if (adMobId.isNotEmpty())
                adMobAds?.loadBannerAds(bannerTag, adMobId)
        }
    }


    object MarvelAdsId {
        val FB_ADNETWORK = "fb"
        val STARTAPP_ADNETWORK = "startapp"

        val BANNER_ID_1 = "gallery_banner"
        val BANNER_ID_2 = "draft_banner"
        val BANNER_ID_3 = "image_banner"

        fun getFBId(adId: String): String {
            return when (adId) {
                BANNER_ID_1 -> DastanAdsApp.INSTANCE.adsConfiguration.fbBannerAdId!!
                BANNER_ID_2 -> DastanAdsApp.INSTANCE.adsConfiguration.fbDraftBannerId!!
                else -> ""
            }
        }

        fun getAdmobId(adId: String): String {
            return when (adId) {
                BANNER_ID_1 -> DastanAdsApp.INSTANCE.adsConfiguration.adMobBannerId!!
                BANNER_ID_2 -> DastanAdsApp.INSTANCE.adsConfiguration.adMobDraftBannerId!!
                else -> ""
            }
        }

        fun removeFBCachedAds(facebookAudience: FacebookAudience, tag: String) {
            when (tag) {
                BANNER_ID_1, BANNER_ID_2 -> facebookAudience.removeCachedBanner(tag)
            }
        }

        fun removeAdMobCachedAds(adMobAds: AdMobAds?, tag: String) {
            when (tag) {
                BANNER_ID_1, BANNER_ID_2 -> adMobAds?.removeCachedBanner(tag)
            }
        }
    }
}
