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
    protected val cahcedBannerAdMap = HashMap<String, Any>()

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
                .forEach { it.addDisplayed() }
    }

    fun removeListener(tag: String) {
        listnerHashMap.remove(tag)
    }

    fun removeCachedBanner(tag: String) {
        cahcedBannerAdMap.remove(tag)
    }
}