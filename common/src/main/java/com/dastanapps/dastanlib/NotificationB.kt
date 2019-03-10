package com.dastanapps.dastanlib

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import com.dastanapps.dastanlib.utils.R

/**
 * Created by dastaniqbal on 07/11/2018.
 * 07/11/2018 3:12
 */
class NotificationB {
    private val TAG = this::class.java.simpleName
    var id: Int = 0
        private set
    var channelId: String? = null
        private set
    var channelName: String? = null
        private set
    var title: String? = null
        private set
    var desc: String? = null
        private set
    var color: Int = Color.WHITE
        private set
    var smallIcon: Int = R.drawable.ic_stat_notify
        private set
    var largeBmp: Bitmap? = null
        private set
    var bigBmp: Bitmap? = null
        private set
    var cancelable: Boolean = true
        private set
    var pendingIntent: Intent? = null
        private set

    fun id(id: Int) = apply { this.id = id }

    fun channelId(channelId: String) = apply { this.channelId = channelId }

    fun channelName(channelId: String) = apply { this.channelName = channelId }

    fun title(title: String) = apply { this.title = title }

    fun desc(desc: String) = apply { this.desc = desc }

    fun color(color: Int) = apply { this.color = color }

    fun smallIcon(smallIcon: Int) = apply { this.smallIcon = smallIcon }

    fun largeBmp(largeBmp: Bitmap) = apply { this.largeBmp = largeBmp }

    fun bigBmp(bigBmp: Bitmap) = apply { this.bigBmp = bigBmp }

    fun cancelable(cancelable: Boolean) = apply { this.cancelable = cancelable }

    fun pendingIntent(pendingIntent: Intent) = apply { this.pendingIntent = pendingIntent }

}