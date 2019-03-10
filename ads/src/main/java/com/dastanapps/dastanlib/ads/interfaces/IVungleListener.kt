package com.dastanapps.dastanlib.ads.interfaces

interface IVungleListener {
    fun error(reason:String)
    fun started()
    fun close()
    fun completed(successfulView: Boolean)
    fun clicked()
    fun loaded()
}