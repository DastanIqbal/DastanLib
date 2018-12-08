package com.dastanapps.dastanapps.ads.admob

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dastanapps.dastanapps.R
import com.dastanapps.dastanlib.DastanAdsApp
import com.dastanapps.dastanlib.ads.interfaces.IAdMobAds
import com.dastanapps.dastanlib.log.Logger

class AdmobAdsTestActivity : AppCompatActivity() {

    private val rewardAds = DastanAdsApp.adsInstance().getAdMobAds()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner_ads_test)
        rewardAds.loadRewardedVideoAd("reward")
        rewardAds.setAdsListener(object :IAdMobAds{
            override fun rewarded() {
                Logger.onlyDebug("adMob::rewarded")
            }

            override fun clicked() {
                Logger.onlyDebug("adMob::clicked")
            }

            override fun completed() {
                Logger.onlyDebug("adMob::completed")
            }

            override fun adLoaded(adLoaded: Any) {
                Logger.onlyDebug("adMob::adLoaded")
                rewardAds.showRewardedVideo()
            }

            override fun adError(error: String) {
                Logger.onlyDebug("adMob::adError")
            }

            override fun adDismissed(tag: String) {
                Logger.onlyDebug("adMob::adDismissed")
            }

            override fun addDisplayed() {
                Logger.onlyDebug("adMob::addDisplayed")
            }

        },"reward");
    }

    override fun onPause() {
        super.onPause()
        rewardAds.onPause()
    }

    override fun onResume() {
        super.onResume()
        rewardAds.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        rewardAds.onDestroy("reward")
    }
}
