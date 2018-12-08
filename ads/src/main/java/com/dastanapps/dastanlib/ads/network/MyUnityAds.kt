package com.dastanapps.dastanlib.ads.network

import android.app.Activity
import com.dastanapps.dastanlib.ads.interfaces.IAdMobAds
import com.unity3d.ads.UnityAds

/**
 * Created by dastaniqbal on 19/03/2018.
 * dastanIqbal@marvelmedia.com
 * 19/03/2018 2:40
 */
class MyUnityAds(val activity: Activity) {

    var rewardedVideoListener: IAdMobAds? = null

    init {
//        UnityAds.initialize(activity, DastanLibApp.INSTANCE.unityGameId, object : IUnityAdsListener {
//            override fun onUnityAdsStart(p0: String?) {
//                if (rewardedVideoListener != null) rewardedVideoListener?.addDisplayed()
//            }
//
//            override fun onUnityAdsFinish(p0: String?, p1: UnityAds.FinishState?) {
//                if (rewardedVideoListener != null) rewardedVideoListener?.completed()
//                if (rewardedVideoListener != null) rewardedVideoListener?.adDismissed(p0!!)
//            }
//
//            override fun onUnityAdsError(p0: UnityAds.UnityAdsError?, p1: String?) {
//                if (rewardedVideoListener != null) rewardedVideoListener?.adError(p1!!)
//            }
//
//            override fun onUnityAdsReady(p0: String?) {
//                if (rewardedVideoListener != null) rewardedVideoListener?.adLoaded(p0!!)
//            }
//        })
//
//        UnityAds.getListener()
    }

    fun showRewardedAds(): Boolean {
        if (isRewardedVideoAvailable()) {
            UnityAds.show(activity, "rewardedVideo")
            return true
        }
        return false
    }

    fun isRewardedVideoAvailable(): Boolean {
        return UnityAds.isReady("rewardedVideo")
    }
}