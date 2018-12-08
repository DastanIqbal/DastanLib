package com.dastanapps.dastanlib.ads.network.mopub

/**
 * Created by dastaniqbal on 19/12/2017.
 * dastanIqbal@marvelmedia.com
 * 19/12/2017 11:15
 */
interface IMoPubListener {
    fun error(string: String)
    fun displayed()
    fun close()
    fun completed()
    fun clicked()
    fun loaded()
}