package com.dastanapps.dastanlib.push

import com.google.gson.annotations.SerializedName

/**
 * Created by dastaniqbal on 12/02/2019.
 * 12/02/2019 10:05
 */
data class PushNotificationB(
        @SerializedName("title") var title: String,
        @SerializedName("content")var content: String,
        @SerializedName("banner_url")var bannerUrl: String,
        @SerializedName("push_type") var pushType: String
)