package com.dastanapps.dastanlib

import com.dastanapps.dastanlib.ads.DastanAds

/**
 * Created by dastaniqbal on 08/12/2018.
 * 08/12/2018 2:06
 */
open class DastanAdsApp : DastanLibApp() {
    private val TAG = this::class.java.simpleName
    val adsConfiguration = AdsConfiguration()

    companion object {
        lateinit var INSTANCE: DastanAdsApp
        fun adsInstance(): DastanAds {
            return DastanAds()
        }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    fun disableAds() = adsConfiguration.disableAds
}