package com.dastanapps.dastanlib.ads.network.mopub

import android.view.View
import android.view.ViewGroup
import com.dastanapps.dastanlib.DastanAdsApp
import com.dastanapps.dastanlib.analytics.DAnalytics
import com.dastanapps.dastanlib.log.Logger
import com.mopub.mobileads.MoPubErrorCode
import com.mopub.mobileads.MoPubView


/**
 * Created by dastaniqbal on 19/12/2017.
 * dastanIqbal@marvelmedia.com
 * 19/12/2017 11:12
 */
class MoPubBannerAd(moPubBannerId: String, val tag: String) {
    var adContainer: ViewGroup? = null
    val cachedAds = HashMap<String, Any>();

    constructor(moPubBannerId: String, adContainer: ViewGroup, tag: String) : this(moPubBannerId, tag) {
        this.adContainer = adContainer
    }

    interface IMoPubBannerAd : IMoPubListener {
        fun loaded(any: Any)
    }

    var mBanner: MoPubView? = null
    var mopubListener: IMoPubBannerAd? = null

    init {
        mBanner = MoPubView(DastanAdsApp.INSTANCE)
        mBanner?.adUnitId = moPubBannerId
        mBanner?.bannerAdListener = object : MoPubView.BannerAdListener {
            override fun onBannerExpanded(banner: MoPubView?) {
                mopubListener?.displayed()
                Logger.onlyDebug("onBannerExpanded")
                showAnalytics("onBannerExpanded")
            }

            override fun onBannerLoaded(banner: MoPubView) {
                mopubListener?.loaded(banner)
                cachedAds[tag] = banner
                show(banner)
                Logger.onlyDebug("onBannerLoaded")
                showAnalytics("onBannerLoaded")
            }

            override fun onBannerCollapsed(banner: MoPubView?) {
                mopubListener?.close()
                Logger.onlyDebug("onBannerCollapsed")
                showAnalytics("onBannerCollapsed")
            }

            override fun onBannerFailed(banner: MoPubView?, errorCode: MoPubErrorCode?) {
                Logger.onlyDebug("onBannerFailed")
                mopubListener?.error(errorCode.toString())
                if (adContainer != null)
                    adContainer?.visibility = View.GONE
                showAnalytics("onBannerFailed")
            }

            override fun onBannerClicked(banner: MoPubView?) {
                mopubListener?.clicked()
                Logger.onlyDebug("onBannerClicked")
                showAnalytics("onBannerClicked")
            }
        }
        if (!DastanAdsApp.INSTANCE.disableAds)
            mBanner?.loadAd()
    }

    fun show(banner: MoPubView) {
        if (adContainer != null) {
            val adLoaded = banner as View
            if (adLoaded.parent != null)
                (adLoaded.parent as ViewGroup).removeAllViews()
            mBanner?.autorefreshEnabled = true
            mBanner?.autorefreshEnabled = false
            adContainer?.removeAllViews()
            adContainer?.addView(adLoaded)
            adContainer?.visibility = View.VISIBLE
            // cachedAds.remove(tag)
        }
    }

    fun refresh(isRefresh: Boolean) {
        mBanner?.autorefreshEnabled = isRefresh
        if (isRefresh) mBanner?.forceRefresh()
    }

    fun setCachedAds(tag: String, adContainer: ViewGroup) {
        if (cachedAds[tag] != null) {
            this.adContainer = adContainer
            show(cachedAds[tag] as MoPubView)
            mBanner?.autorefreshEnabled = true
        } else {
            this.adContainer = adContainer
        }
    }

    fun showAnalytics(value: String) {
        DAnalytics.getInstance().sendLogs("Ads", tag, value)
    }

    fun destroy() {
        mBanner?.destroy()
    }

    fun reset() {
        //destroy()
        adContainer = null
    }
}