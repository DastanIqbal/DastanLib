package com.videoeditor.kruso.lib.ads

import com.dastanapps.dastanlib.ads.IMarvelAds

interface IFBNativeAds : IMarvelAds {
    fun adLoaded(adLoaded: Any)

    fun adError(error: String)

    fun adDismissed()

    fun addDisplayed()
}