package com.dastanapps.dastanlib.ads

interface IFBNativeAds : IMarvelAds {
    fun adLoaded(adLoaded: Any)

    fun adError(error: String)

    fun adDismissed()

    fun addDisplayed()
}