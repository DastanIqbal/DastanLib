package com.dastanapps.dastanapps.ads.admob

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dastanapps.dastanapps.R
import com.dastanapps.dastanlib.DastanAdsApp
import com.dastanapps.dastanlib.ads.interfaces.IAdMobAds
import com.dastanapps.dastanlib.ads.interfaces.IMarvelAds
import com.dastanapps.dastanlib.log.Logger
import com.dastanapps.dastanlib.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_banner_ads_test.*

class AdmobAdsTestActivity : AppCompatActivity() {

    private val admobads = DastanAdsApp.adsInstance().getAdMobAds()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner_ads_test)

        bannerAds()
        tvInterstitial.setOnClickListener { interstialAds() }
        tvReward.setOnClickListener { rewardAds() }
        nativeAds()
    }

    private fun nativeAds() {
//        admobads.loadNativeAds(nativead,"nativeads")
//        admobads.setAdsListener(object : IMarvelAds {
//
//            override fun adLoaded(adLoaded: Any) {
//                Logger.onlyDebug("adMob:NaitveAds:adLoaded")
//                ViewUtils.showToast(this@AdmobAdsTestActivity, "NaitveAds Ads adLoaded")
//                admobads.showInterstialAd("NaitveAds", adLoaded)
//            }
//
//            override fun adError(error: String) {
//                Logger.onlyDebug("adMob:NaitveAds:adError")
//                ViewUtils.showToast(this@AdmobAdsTestActivity, "NaitveAds Ads adError")
//            }
//
//            override fun adDismissed(tag: String) {
//                Logger.onlyDebug("adMob:NaitveAds:adDismissed")
//                ViewUtils.showToast(this@AdmobAdsTestActivity, "NaitveAds Ads adDismissed")
//            }
//
//            override fun adDisplayed() {
//                Logger.onlyDebug("adMob:NaitveAds:adDisplayed")
//                ViewUtils.showToast(this@AdmobAdsTestActivity, "NaitveAds Ads adDisplayed")
//            }
//
//        }, "nativeads")
    }

    private fun bannerAds() {
        val tag = "banner"
        admobads.loadBannerAds(this, tag, getString(R.string.admob_ads_banner))
        admobads.setAdsListener(object : IMarvelAds {
            override fun adLoaded(adLoaded: Any) {
                Logger.onlyDebug("adMob:banner:adLoaded")
                ViewUtils.showToast(this@AdmobAdsTestActivity, "Banner Ads adLoaded")
                fl_banner.removeAllViews()
                fl_banner.addView(adLoaded as View)

            }

            override fun adError(error: String) {
                Logger.onlyDebug("adMob:banner:adError")
                ViewUtils.showToast(this@AdmobAdsTestActivity, "Banner Ads adError")
            }

            override fun adDismissed(tag: String) {
                Logger.onlyDebug("adMob:banner:adDismissed")
                ViewUtils.showToast(this@AdmobAdsTestActivity, "Banner Ads adDismissed")
            }

            override fun adDisplayed() {
                Logger.onlyDebug("adMob:banner:adDisplayed")
                ViewUtils.showToast(this@AdmobAdsTestActivity, "Banner Ads adDisplayed")
            }

        }, tag)
    }

    private fun interstialAds() {
        val tag = "Interstitial"
        admobads.loadInterstitialAd(tag)
        admobads.setAdsListener(object : IMarvelAds {

            override fun adLoaded(adLoaded: Any) {
                Logger.onlyDebug("adMob:Interstitial:adLoaded")
                ViewUtils.showToast(this@AdmobAdsTestActivity, "Interstitial Ads adLoaded")

                admobads.showInterstitialAd(this@AdmobAdsTestActivity, tag)
            }

            override fun adError(error: String) {
                Logger.onlyDebug("adMob:Interstitial:adError")
                ViewUtils.showToast(this@AdmobAdsTestActivity, "Interstitial Ads adError")
            }

            override fun adDismissed(tag: String) {
                Logger.onlyDebug("adMob:Interstitial:adDismissed")
                ViewUtils.showToast(this@AdmobAdsTestActivity, "Interstitial Ads adDismissed")
            }

            override fun adDisplayed() {
                Logger.onlyDebug("adMob:Interstitial:adDisplayed")
                ViewUtils.showToast(this@AdmobAdsTestActivity, "Interstitial Ads adDisplayed")
            }

        }, tag)
    }

    /**
     * Rewarded Ads
     */
    private fun rewardAds() {
        val tag = "reward"
        admobads.loadRewardedVideoAd(tag)
        admobads.setAdsListener(object : IAdMobAds {
            override fun rewarded() {
                Logger.onlyDebug("adMob:reward:rewarded")
                ViewUtils.showToast(this@AdmobAdsTestActivity, "Rewarded Ads rewarded")
            }

            override fun clicked() {
                Logger.onlyDebug("adMob:reward:clicked")
                ViewUtils.showToast(this@AdmobAdsTestActivity, "Rewarded Ads clicked")
            }

            override fun completed() {
                Logger.onlyDebug("adMob:reward:completed")
                ViewUtils.showToast(this@AdmobAdsTestActivity, "Rewarded Ads completed")
            }

            override fun adLoaded(adLoaded: Any) {
                Logger.onlyDebug("adMob:reward:adLoaded")

                admobads.showRewardedVideo(this@AdmobAdsTestActivity, tag)

                ViewUtils.showToast(this@AdmobAdsTestActivity, "Rewarded Ads adLoaded")
            }

            override fun adError(error: String) {
                Logger.onlyDebug("adMob:reward:adError")
                ViewUtils.showToast(this@AdmobAdsTestActivity, "Rewarded Ads adError")
            }

            override fun adDismissed(tag: String) {
                Logger.onlyDebug("adMob:reward:adDismissed")
                ViewUtils.showToast(this@AdmobAdsTestActivity, "Rewarded Ads adDismissed")
            }

            override fun adDisplayed() {
                Logger.onlyDebug("adMob:reward:adDisplayed")
                ViewUtils.showToast(this@AdmobAdsTestActivity, "Rewarded Ads adDisplayed")
            }

        }, tag)
    }

    override fun onPause() {
        super.onPause()
        admobads.onPause()
    }

    override fun onResume() {
        super.onResume()
        admobads.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        admobads.onDestroy("reward")
    }
}
