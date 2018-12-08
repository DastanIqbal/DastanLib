package com.dastanapps.dastanlib.ads.interfaces

/**
 * Created by dastaniqbal on 17/03/2017.
 * 17/03/2017 5:09
 */

interface IAdsLifecycle {
    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onDestroy(tag:String)
}
