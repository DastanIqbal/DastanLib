package com.dastanapps.dastanlib.ads.network

import com.dastanapps.dastanlib.DastanAdsApp
import com.dastanapps.dastanlib.ads.interfaces.IVungleListener
import com.dastanapps.dastanlib.log.Logger
import com.vungle.publisher.VungleAdEventListener
import com.vungle.publisher.VungleInitListener
import com.vungle.publisher.VunglePub


/**
 * Created by dastaniqbal on 04/12/2017.
 * ask2iqbal@gmail.com
 * 04/12/2017 10:25
 */
class VungleAds {
    val vunglePub = VunglePub.getInstance()
    // get a reference to the global AdConfig object
    private val globalAdConfig = vunglePub.globalAdConfig
    private val vungleAppId = "5a1faa999bcbcea80a000186"
    private val placementIds = arrayOf("UNTICK258562")
    var vungleListener: IVungleListener? = null

    private val vungleEventListener: VungleAdEventListener = object : VungleAdEventListener {
        override fun onUnableToPlayAd(placementReferenceId: String, reason: String?) {
            Logger.onlyDebug("$placementReferenceId:$reason")
            vungleListener?.error("$placementReferenceId:$reason")
        }

        override fun onAdAvailabilityUpdate(placementReferenceId: String, isAdAvailable: Boolean) {
            Logger.onlyDebug("$placementReferenceId:$isAdAvailable")
            vungleListener?.loaded()
        }

        override fun onAdEnd(placementReferenceId: String, wasSuccessfulView: Boolean, wasCallToActionClicked: Boolean) {
            Logger.onlyDebug("$placementReferenceId:$wasSuccessfulView:$wasCallToActionClicked")
            vungleListener?.completed(wasSuccessfulView)
        }

        override fun onAdStart(placementReferenceId: String) {
            Logger.onlyDebug(placementReferenceId)
            vungleListener?.started()
        }
    }

    init {
        // initialize the Publisher SDK
        vunglePub.init(DastanAdsApp.INSTANCE, vungleAppId, placementIds, object : VungleInitListener {
            override fun onSuccess() {
                // set any configuration options you like.
                // For a full description of available options, see the
                // 'Configuration Options' section.
                globalAdConfig?.isSoundEnabled = true
//                globalAdConfig?.orientation = Orientation.autoRotate
//                globalAdConfig?.isImmersiveMode=true
                Logger.onlyDebug("Vungle Initialized")
            }

            override fun onFailure(p0: Throwable?) {
                Logger.onlyDebug("Vungle Initialization Failed ${p0?.message}")
            }
        })

        vunglePub.clearAndSetEventListeners(vungleEventListener)
    }

    fun playAd() {
        if (vunglePub.isAdPlayable(placementIds[0])) {
            vunglePub.playAd(placementIds[0], globalAdConfig)
        }
    }

    fun isAdAvailable(): Boolean {
        return vunglePub.isAdPlayable(placementIds[0])
    }

    fun onResume() {
        vunglePub.onResume()
    }

    fun onPause() {
        vunglePub.onPause()
    }

    fun onDestroy() {
        vunglePub.clearEventListeners()
    }
}