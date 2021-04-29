package com.dastanapps.dastanlib.utils

import android.app.*
import android.content.Context
import android.media.AudioAttributes
import android.os.Build
import androidx.core.app.NotificationCompat
import com.dastanapps.dastanlib.ChannelB
import com.dastanapps.dastanlib.NotificationB

/**
 *
 * "Iqbal Ahmed" created on 07/08/2020
 */

object NotificationUtils {

    private fun createChannel(
            nm: NotificationManager,
            channel: ChannelB
    ): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                    channel.channelId,
                    channel.channelName,
                    NotificationManager.IMPORTANCE_LOW
            )
            channel.sound?.run {
                chan.setSound(
                        channel.sound,
                        AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .build()
                )
            } ?: chan.setSound(null, null)
            chan.enableLights(channel.light)
            chan.enableVibration(channel.vibration)
            if (channel.public)
                chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            nm.createNotificationChannel(chan)
        }
        return channel.channelId
    }

    private fun NotificationManager.showNotification(
            context: Context,
            notificationB: NotificationB
    ): Notification {
        val channelId = createChannel(this, notificationB.channel)
        val mBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(notificationB.smallIcon)
                .setContentTitle(notificationB.title)
                .setContentText(notificationB.desc)
                .setAutoCancel(notificationB.cancelable)
                .setOngoing(!notificationB.cancelable)
                .setColor(notificationB.color)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(notificationB.channel.sound)

        notificationB.pendingIntent?.run {
            val resultPendingIntent = PendingIntent.getActivity(context,
                    0, notificationB.pendingIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            mBuilder.setContentIntent(resultPendingIntent)
        }

        notificationB.largeBmp?.run {
            mBuilder.setLargeIcon(notificationB.largeBmp)
        }

        notificationB.bigBmp?.run {
            mBuilder.setStyle(NotificationCompat.BigPictureStyle()
                    .bigPicture(notificationB.bigBmp)
                    .setBigContentTitle(notificationB.title)
                    .setSummaryText(notificationB.desc))
        }

        notificationB.smallRemoteView?.run {
            mBuilder.setContent(this)
            mBuilder.setCustomContentView(this)
        }

        notificationB.bigRemoteView?.run {
            mBuilder.setCustomBigContentView(this)
        }

        return mBuilder.build()
    }

    private fun getService(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun notifyNotification(context: Context, notificationB: NotificationB) {
        val notifService = getService(context)
        notifService.notify(notificationB.id, notifService.showNotification(context,
                notificationB))
    }

    fun Service.startForegroundNotification(context: Context, notificationB: NotificationB) {
        startForeground(
                notificationB.id,
                getService(context)
                        .showNotification(context, notificationB)
        )
    }
}