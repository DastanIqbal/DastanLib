package com.dastanapps.dastanlib.utils

import android.util.Log
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Iqbal Ahmed on 12/31/2015.
 */
object DateTimeUtils {
    fun timeStampToDate(s: String): String {
        val timeInLong = java.lang.Long.parseLong(s)
        val stamp = Timestamp(timeInLong * 1000) //epoch to millisec converter
        val dt = Date(stamp.time)
        Log.i("format", DateFormat.getDateInstance().format(dt))
        return DateFormat.getDateInstance().format(dt).toString()
    }

    fun currentTimeStampInDateTime(format: String, locale: Locale = Locale.ENGLISH): String {
        val sdf = SimpleDateFormat(format, locale)
        val date = Date(System.currentTimeMillis())
        return sdf.format(date)
    }

    fun currentDate(locale: Locale = Locale.ENGLISH): String {
        val sdf = SimpleDateFormat("dd MMM yyyy",locale)
        val date = Date(System.currentTimeMillis())
        return sdf.format(date)
    }

    fun currentTime(locale: Locale = Locale.ENGLISH): String {
        val sdf = SimpleDateFormat("hh:mm aaa",locale)
        val date = Date(System.currentTimeMillis())
        return sdf.format(date)
    }

    fun getTime(timestamp: Long,locale: Locale = Locale.ENGLISH): String {
        val sdf = SimpleDateFormat("hh:mm aaa",locale)
        val date = Date(timestamp)
        return sdf.format(date)
    }

    fun getDate(timestamp: Long,locale: Locale = Locale.ENGLISH): String {
        val sdf = SimpleDateFormat("dd MMM yyyy",locale)
        val date = Date(timestamp)
        return sdf.format(date)
    }

    fun getDate(fmt: String, timestamp: Long,locale: Locale = Locale.ENGLISH): String {
        val sdf = SimpleDateFormat(fmt,locale)
        val date = Date(timestamp)
        return sdf.format(date)
    }

    fun getDateTime(timestamp: Long, format: String,locale: Locale = Locale.ENGLISH): String {
        val sdf = SimpleDateFormat(format,locale)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val date = Date(timestamp)
        return sdf.format(date)
    }

    fun convertSecToMinSecs(seconds: Long,locale: Locale = Locale.ENGLISH): String {
        if (seconds == 0L) return ""
        val m: Int
        val sec: Int
        m = seconds.toInt() / 60
        sec = seconds.toInt() % 60
        if (m > 0 && sec > 0)
            return m.toString() + " min " + sec + " sec"
        if (m > 0 && sec == 0)
            return m.toString() + " min "
        return if (m == 0 && sec > 0) sec.toString() + " sec" else ""
    }

    fun convertMilliSecToMinSecs(millis: Long): String {
        return if (millis == 0L) "" else convertSecToMinSecs(millis / 1000)
    }

    fun findDaysDiff(unixStartTime: Long, unixEndTime: Long): Int {
        val calendar1 = Calendar.getInstance()
        calendar1.timeInMillis = unixStartTime
        calendar1.set(Calendar.HOUR_OF_DAY, 0)
        calendar1.set(Calendar.MINUTE, 0)
        calendar1.set(Calendar.SECOND, 0)
        calendar1.set(Calendar.MILLISECOND, 0)

        val calendar2 = Calendar.getInstance()
        calendar2.timeInMillis = unixEndTime
        calendar2.set(Calendar.HOUR_OF_DAY, 0)
        calendar2.set(Calendar.MINUTE, 0)
        calendar2.set(Calendar.SECOND, 0)
        calendar2.set(Calendar.MILLISECOND, 0)

        return ((calendar2.timeInMillis - calendar1.timeInMillis) / (24 * 60 * 60 * 1000)).toInt()

    }
}
