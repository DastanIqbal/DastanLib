package com.dastanapps.dastanlib

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color

/**
 * Created by dastaniqbal on 07/11/2018.
 * 07/11/2018 3:12
 */
class NotificationB {
    private val TAG = this::class.java.simpleName
    var id: Int = 0
    var channelId: String? = null
    var channelName: String? = null
    var title: String? = null
    var desc: String? = null
    var color: Int = Color.WHITE
    var smallIcon: Int = -1
    var largeBmp: Bitmap? = null
    var bigBmp: Bitmap? = null
    var cancelable: Boolean = false
    var pendingIntent: Intent? = null

}