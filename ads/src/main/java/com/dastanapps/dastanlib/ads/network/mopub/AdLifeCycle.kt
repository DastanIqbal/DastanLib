package com.dastanapps.dastanlib.ads.network.mopub

import android.app.Activity
import com.mopub.common.MoPub

/**
 * Created by dastaniqbal on 19/12/2017.
 * dastanIqbal@marvelmedia.com
 * 19/12/2017 11:12
 */
object AdLifeCycle {
    fun onCreate(activity: Activity) {
        MoPub.onCreate(activity)
    }

    fun onStart(activity: Activity) {
        MoPub.onStart(activity)
    }

    fun onResume(activity: Activity) {
        MoPub.onResume(activity)
    }

    fun onPause(activity: Activity) {
        MoPub.onPause(activity)
    }

    fun onStop(activity: Activity) {
        MoPub.onStop(activity)
    }

    fun onDestroy(activity: Activity) {
        MoPub.onDestroy(activity)
    }
}