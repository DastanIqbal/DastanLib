package com.dastanapps.dastanlib.ads

import com.dastanapps.dastanlib.DastanAdsApp
import com.dastanapps.dastanlib.ads.interfaces.IMarvelAds

/**
 * Created by dastaniqbal on 12/12/2017.
 * ask2iqbal@gmail.com
 * 12/12/2017 2:24
 */
abstract class AdsBase {
    protected val context = DastanAdsApp.INSTANCE
    protected var listnerHashMap = HashMap<String, IMarvelAds>()
    protected val cachedBannerAdMap = HashMap<String, Any?>()
    protected val cachedInterstialAdMap = HashMap<String, Any?>()
    protected val cachedRewardedAdMap = HashMap<String, Any?>()

    fun setNativeAdsListener(fbNativeAds: IMarvelAds, tag: String) {
        listnerHashMap.put(tag, fbNativeAds)
    }

    protected fun notifyAdError(errorMessage: String) {
        val startAppKeys = listnerHashMap.keys
        startAppKeys
            .map { listnerHashMap[it] as IMarvelAds }
            .forEach { it.adError(errorMessage) }
    }

    protected fun notifyAdLoaded(nativeAd: Any) {
        val startAppKeys = listnerHashMap.keys
        startAppKeys
            .map { listnerHashMap[it] as IMarvelAds }
            .forEach {
                it.adLoaded(nativeAd)
            }
    }

    protected fun notifyAdDismissed(tag: String) {
        val startAppKeys = listnerHashMap.keys
        startAppKeys
            .map { listnerHashMap[it] as IMarvelAds }
            .forEach { it.adDismissed(tag) }
    }

    protected fun notifyAdDiplayed() {
        val startAppKeys = listnerHashMap.keys
        startAppKeys
            .map { listnerHashMap[it] as IMarvelAds }
            .forEach { it.adDisplayed() }
    }

    fun removeListener(tag: String) {
        listnerHashMap.remove(tag)
    }

    fun removeCachedBanner(tag: String) {
        cachedBannerAdMap.remove(tag)
    }

    fun removeCachedInterstialAd(tag: String) {
        cachedInterstialAdMap.remove(tag)
    }

    fun removeCachedRewardedAd(tag: String) {
        cachedRewardedAdMap.remove(tag)
    }

    fun isRewardedAvailable(tag: String): Boolean {
        return cachedRewardedAdMap[tag] != null
    }

    fun isInterstitialAvailable(tag: String): Boolean {
        return cachedInterstialAdMap[tag] != null
    }

    fun isBannerAvailable(tag: String): Boolean {
        return cachedBannerAdMap[tag] != null
    }
}