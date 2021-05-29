package com.dastanapps.dastanlib.ads.network.mopub

import android.app.Activity
import com.dastanapps.dastanlib.DastanAdsApp
import com.dastanapps.dastanlib.log.Logger
import com.mopub.common.MoPub
import com.mopub.common.MoPubReward
import com.mopub.common.SdkConfiguration
import com.mopub.mobileads.MoPubErrorCode
import com.mopub.mobileads.MoPubRewardedVideoListener
import com.mopub.mobileads.MoPubRewardedVideos


/**
 * Created by dastaniqbal on 30/11/2017.
 * 30/11/2017 4:03
 */
open class MoPubRewardVideoAd(mActivity: Activity) {

    companion object {
        var REWARDED_VIDEO: String? = DastanAdsApp.INSTANCE.adsConfiguration.mopubRewardedVideo
    }

    var mopubListener: IMoPubListener? = null

    init {
        //TODO: Need to Fix, MoPub Reward Ad crashing when calling Mediation Adapter.
        // A list of rewarded video adapters to initialize
        val networksToInit = ArrayList<String>()
        networksToInit.add("FacebookRewardedVideo.java")
        networksToInit.add("GooglePlayServicesRewardedVideo.java")

        val sdkConfiguration = SdkConfiguration.Builder(DastanAdsApp.INSTANCE.adsConfiguration.mopubRewardedVideo!!)
                // .withMediationSettings()
                .withNetworksToInit(networksToInit)
                .build()

        MoPub.initializeSdk(mActivity, sdkConfiguration) { Logger.onlyDebug("MoPub Reward Video Initialized") }
        //MoPubRewardedVideos.initializeRewardedVideo(mActivity)
        AdLifeCycle.onCreate(mActivity)
        MoPubRewardedVideos.setRewardedVideoListener(object : MoPubRewardedVideoListener {
            override fun onRewardedVideoClosed(adUnitId: String) {
                mopubListener?.close()
                Logger.onlyDebug("MoPub:onRewardedVideoClosed")
            }

            override fun onRewardedVideoCompleted(adUnitIds: MutableSet<String>, reward: MoPubReward) {
                mopubListener?.completed()
                Logger.onlyDebug("MoPub:onRewardedVideoCompleted")
            }

            override fun onRewardedVideoPlaybackError(adUnitId: String, errorCode: MoPubErrorCode) {
                mopubListener?.error(errorCode.toString())
                Logger.onlyDebug("MoPub" + adUnitId + ":" + errorCode.toString())
            }

            override fun onRewardedVideoLoadFailure(adUnitId: String, errorCode: MoPubErrorCode) {
                mopubListener?.error(errorCode.toString())
                Logger.onlyDebug("MoPub" + adUnitId + ":" + errorCode.toString())
            }

            override fun onRewardedVideoClicked(adUnitId: String) {
                mopubListener?.clicked()
                Logger.onlyDebug("MoPub:onRewardedVideoClicked")
            }

            override fun onRewardedVideoStarted(adUnitId: String) {
                mopubListener?.displayed()
                Logger.onlyDebug("MoPub:onRewardedVideoStarted")
            }

            override fun onRewardedVideoLoadSuccess(adUnitId: String) {
                mopubListener?.completed()
                Logger.onlyDebug("MoPub:onRewardedVideoLoadSuccess")
            }

        })
        if (DastanAdsApp.INSTANCE.isRelease() &&
                !DastanAdsApp.INSTANCE.disableAds())
            loadRewardedVideo()
    }

    fun loadRewardedVideo() {
        REWARDED_VIDEO?.let { MoPubRewardedVideos.loadRewardedVideo(it) }
    }

    fun hasRewardVideo(): Boolean? = REWARDED_VIDEO?.let { MoPubRewardedVideos.hasRewardedVideo(it) }

    fun showRewardedVideo() {
        if (this.hasRewardVideo()!!) {
            REWARDED_VIDEO?.let { MoPubRewardedVideos.showRewardedVideo(it) }
        }
    }

}