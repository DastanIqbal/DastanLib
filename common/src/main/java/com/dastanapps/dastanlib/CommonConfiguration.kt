package com.dastanapps.dastanlib

import com.dastanapps.dastanlib.utils.BuildConfig

/**
 *
 * "Iqbal Ahmed" created on 12/14/19
 */
open class CommonConfiguration {
    var versionCode: Long = 0
    var devMode: String = BuildConfig.BUILD_TYPE
    var giphyAPIKey: String? = null
    var tenorAPIKey: String? = null
}