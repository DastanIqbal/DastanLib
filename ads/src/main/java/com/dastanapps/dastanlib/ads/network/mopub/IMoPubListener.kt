package com.dastanapps.dastanlib.ads.network.mopub

/**
 * Created by dastaniqbal on 19/12/2017.
 * ask2iqbal@gmail.com
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