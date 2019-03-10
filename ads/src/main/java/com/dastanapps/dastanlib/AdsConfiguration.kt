package com.dastanapps.dastanlib

import com.dastanapps.dastanlib.ads.BuildConfig

/**
 * Created by dastaniqbal on 16/04/2018.
 * dastanIqbal@marvelmedia.com
 * 16/04/2018 1:15
 */
class AdsConfiguration {
    var versionCode: Long = 0
    var devMode: String = BuildConfig.BUILD_TYPE
    var fbBannerAdId: String? = null
    var fbDraftBannerId: String? = null
    var fbNativeAdId: String? = null
    var fbSaveVideo2Id: String? = null
    var fbMyMusicId: String? = null
    var fbInterstialAdId: String? = null
    var fbExitNativeAdId: String? = null
    var fbScreenRecoder:String?=null
    var giphyAPIKey: String? = null
    var tenorAPIKey: String? = null
    var adMobAppId: String? = null
    var adMobRewardId: String? = null
    var adMobBannerId: String? = null
    var adMobDraftBannerId: String? = null
    var adMobInterstialAd: String? = null
    var mopubInterstial: String? = null
    var mopubRewardedVideo: String? = null
    var moPubBanner: String? = null
    var moPubDraftBanner: String? = null
    var moPubImageBanner: String? = null
    var unityAdGameId: String? = null
    var startAppId: String? = null
}