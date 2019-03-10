package com.dastanapps.dastanlib.ads.interfaces

/**
 * Created by dastaniqbal on 17/03/2017.
 * 17/03/2017 5:09
 */

interface IMarvelAds{
    fun adLoaded(adLoaded: Any)

    fun adError(error: String)

    fun adDismissed(tag:String)

    fun addDisplayed()
}
