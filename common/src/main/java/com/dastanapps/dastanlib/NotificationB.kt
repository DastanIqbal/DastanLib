package com.dastanapps.dastanlib

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.widget.RemoteViews
import com.dastanapps.dastanlib.utils.R

/**
 * Created by dastaniqbal on 07/11/2018.
 * 07/11/2018 3:12
 */
data class NotificationB(
        var id: Int = 0,
        var title: String,
        var channel: ChannelB
) {
    private val TAG = this::class.java.simpleName
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
    var smallRemoteView:RemoteViews?=null
    var bigRemoteView:RemoteViews?=null

    fun id(id: Int) = apply { this.id = id }

    fun title(title: String) = apply { this.title = title }

    fun desc(desc: String) = apply { this.desc = desc }

    fun color(color: Int) = apply { this.color = color }

    fun smallIcon(smallIcon: Int) = apply { this.smallIcon = smallIcon }

    fun largeBmp(largeBmp: Bitmap) = apply { this.largeBmp = largeBmp }

    fun bigBmp(bigBmp: Bitmap) = apply { this.bigBmp = bigBmp }

    fun cancelable(cancelable: Boolean) = apply { this.cancelable = cancelable }

    fun pendingIntent(pendingIntent: Intent) = apply { this.pendingIntent = pendingIntent }
}

data class ChannelB(
        val channelId: String,
        val channelName: String,
        val sound: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
        val light: Boolean = true,
        val vibration: Boolean = true,
        val public: Boolean = true

)