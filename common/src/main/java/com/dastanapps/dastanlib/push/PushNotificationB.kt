package com.dastanapps.dastanlib.push

/**
 * Created by dastaniqbal on 12/02/2019.
 * 12/02/2019 10:05
 */
data class PushNotificationB(
        var title: String,
        var content: String,
        var banner_url: String,
        var push_type: String
) {
    private val TAG = this::class.java.simpleName

    constructor() : this("", "", "", "")
}