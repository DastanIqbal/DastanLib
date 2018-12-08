/*
 * Copyright (C) 2016 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dastanapps.dastanlib.ads.mediation

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.dastanapps.dastanlib.DastanAdsApp
import com.dastanapps.dastanlib.DastanLibApp
import com.dastanapps.dastanlib.ads.interfaces.IAdMobAds
import com.dastanapps.dastanlib.log.Logger
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.mediation.MediationAdRequest
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdAdapter
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdListener
import com.startapp.android.publish.adsCommon.Ad
import com.startapp.android.publish.adsCommon.StartAppAd
import com.startapp.android.publish.adsCommon.adListeners.AdDisplayListener
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener

/**
 * The [StartAppRewardedVideo] class is used to load and show rewarded video ads for the
 * Sample SDK.
 */
class StartAppRewardedVideo : MediationRewardedVideoAdAdapter {
    var isIntialized = false
    val startAppAd: StartAppAd = StartAppAd(DastanLibApp.INSTANCE)
    private var mMediationRewardedVideoAdListener: MediationRewardedVideoAdListener? = null
    var adMobAds: IAdMobAds? = null
    override fun onResume() {
        startAppAd.onResume()
    }

    override fun loadAd(p0: MediationAdRequest?, p1: Bundle?, p2: Bundle?) {
        startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, object : AdEventListener {
            override fun onFailedToReceiveAd(p0: Ad?) {
                if (adMobAds != null) adMobAds?.adError(p0?.errorMessage!!)
                mMediationRewardedVideoAdListener?.onAdFailedToLoad(this@StartAppRewardedVideo, AdRequest.ERROR_CODE_NO_FILL)
            }

            override fun onReceiveAd(p0: Ad?) {
                if (adMobAds != null) adMobAds?.adLoaded(p0!!)
                mMediationRewardedVideoAdListener?.onAdLoaded(this@StartAppRewardedVideo)
            }
        })
        startAppAd.setVideoListener {
            if (adMobAds != null) adMobAds?.completed()
            val rewardItem = StartAppRewardItem("USD", 1)
            mMediationRewardedVideoAdListener?.onRewarded(this@StartAppRewardedVideo, rewardItem)
        }
    }

    override fun onPause() {
        startAppAd.onPause()
    }

    override fun showVideo() {
        if (startAppAd.isReady) {
            startAppAd.showAd(object : AdDisplayListener {
                override fun adHidden(p0: Ad?) {
                    if (adMobAds != null) adMobAds?.adLoaded(p0!!)
                }

                override fun adDisplayed(p0: Ad?) {
                    if (adMobAds != null) adMobAds?.addDisplayed()
                }

                override fun adNotDisplayed(p0: Ad?) {
                    if (adMobAds != null) adMobAds?.adError(p0?.errorMessage!!)
                }

                override fun adClicked(p0: Ad?) {
                    if (adMobAds != null) adMobAds?.clicked()
                }
            })
        } else {
            Logger.onlyDebug("No Ads Found")
            mMediationRewardedVideoAdListener?.onAdFailedToLoad(this@StartAppRewardedVideo, AdRequest.ERROR_CODE_NO_FILL)
        }
    }

    override fun onDestroy() {
        isIntialized = false
        startAppAd.onBackPressed()
    }

    override fun isInitialized(): Boolean {
        return isIntialized;
    }

    override fun initialize(context: Context, mediationAdRequest: MediationAdRequest, unused: String,
                            listener: MediationRewardedVideoAdListener, serverParameters: Bundle, mediationExtras: Bundle) {

        // In this method you should initialize your SDK.

        // The sample SDK requires activity context to initialize, so check
        // that the context provided by the app is an activity context before
        // initializing.
        if (context !is Activity) {
            // Context not an Activity context, log the reason for failure and
            // fail the initialization.
            Logger.onlyDebug("Sample SDK requires an Activity context to initialize")
            listener.onInitializationFailed(this@StartAppRewardedVideo, AdRequest.ERROR_CODE_INVALID_REQUEST)
            return
        }

        mMediationRewardedVideoAdListener = listener;

//        // Get the Ad Unit ID for the Sample SDK from serverParameters bundle.
//        val adUnit = serverParameters.getString(
//                MediationRewardedVideoAdAdapter.CUSTOM_EVENT_SERVER_PARAMETER_FIELD)
//
//        if (TextUtils.isEmpty(adUnit)) {
//            listener.onAdFailedToLoad(this, AdRequest.ERROR_CODE_INVALID_REQUEST)
//            return
//        }
        DastanAdsApp.adsInstance().startAppAds
        isIntialized = true
    }
}