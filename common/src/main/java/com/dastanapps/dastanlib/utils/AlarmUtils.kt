package com.dastanapps.dastanlib.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.SystemClock
import java.util.*

/**
 * Created by dastaniqbal on 25/12/2018.
 * 25/12/2018 10:25
 */
object AlarmUtils {
    private val TAG = this::class.java.simpleName

    fun scheduleExactEvent(
            context: Context,
            calendar: Calendar,
            pendingIntent: PendingIntent?
    ) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> alarmMgr.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
            )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> alarmMgr.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
            )
            else -> alarmMgr.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
            )
        }
    }

    fun scheduleExactRepeatingEvent(
            context: Context,
            calendar: Calendar,
            pendingIntent: PendingIntent
    ) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        )
    }

    fun scheduleInExactRepeatingEvent(
            context: Context,
            calendar: Calendar,
            pendingIntent: PendingIntent
    ) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        )
    }

    fun scheduleExactEvent(
            context: Context,
            seconds: Long,
            pendingIntent: PendingIntent
    ) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> alarmMgr.setExactAndAllowWhileIdle(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + seconds * 1000,
                    pendingIntent
            )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> alarmMgr.setExact(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + seconds * 1000,
                    pendingIntent
            )
            else -> alarmMgr.set(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + seconds * 1000,
                    pendingIntent
            )
        }

    }

    fun scheduleExactRepeatingEvent(
            context: Context,
            seconds: Long,
            pendingIntent: PendingIntent
    ) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + seconds * 1000,
                seconds * 1000,
                pendingIntent
        )
    }

    fun cancelEvent(
            context: Context,
            pendingIntent: PendingIntent
    ) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr.cancel(pendingIntent)
    }
}