package com.dastanapps.dastanlib.ads.network.mopub

import android.app.Activity
import com.dastanapps.dastanlib.DastanAdsApp
import com.dastanapps.dastanlib.analytics.DAnalytics
import com.mopub.mobileads.MoPubErrorCode
import com.mopub.mobileads.MoPubInterstitial


/**
 * Created by dastaniqbal on 19/12/2017.
 * ask2iqbal@gmail.com
 * 19/12/2017 11:12
 */
class MoPubInterstialAd(val mActivity: Activity) {
    companion object {
        var MOPUB_INTERSTIAL: String = DastanAdsApp.INSTANCE.adsConfiguration.mopubInterstial!!
    }

    var mInterstitial: MoPubInterstitial? = null
    var mopubListener: IMoPubListener? = null

    init {
        setUp(mActivity)
    }

    private fun setUp(mActivity: Activity) {
        mInterstitial = MoPubInterstitial(mActivity, MOPUB_INTERSTIAL)
        mInterstitial?.interstitialAdListener = object : MoPubInterstitial.InterstitialAdListener {
            override fun onInterstitialLoaded(interstitial: MoPubInterstitial?) {
                mopubListener?.loaded()
            }

            override fun onInterstitialShown(interstitial: MoPubInterstitial?) {
                mopubListener?.displayed()
            }

            override fun onInterstitialFailed(interstitial: MoPubInterstitial?, errorCode: MoPubErrorCode?) {
                mopubListener?.error(errorCode.toString())
            }

            override fun onInterstitialDismissed(interstitial: MoPubInterstitial?) {
                mopubListener?.close()
            }

            override fun onInterstitialClicked(interstitial: MoPubInterstitial?) {
                mopubListener?.clicked()
            }

        }
        if (DastanAdsApp.INSTANCE.isRelease() &&
                !DastanAdsApp.INSTANCE.disableAds())
            mInterstitial?.load()
    }

    fun reload() {
        setUp(mActivity)
    }

    fun hasInterstialAd(): Boolean? = mInterstitial?.isReady

    fun show() {
        if (this.hasInterstialAd()!!) {
            DAnalytics.getInstance().sendLogs("Ads", "onSaveClicked", "MoPub InterstialAds")
            mInterstitial?.show()
        }
    }

    fun destroy() {
        mInterstitial?.destroy()
    }
}