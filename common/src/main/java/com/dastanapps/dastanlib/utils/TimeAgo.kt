package com.dastanapps.dastanlib.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Iqbal Ahmed on 1/29/16.
 */
object TimeAgo {
    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS
    private val WEEKS_MILLIS = 7 * DAY_MILLIS
    private val MONTHS_MILLIS = 4 * WEEKS_MILLIS
    private var simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/M/yyyy HH:mm:ss", Locale.ENGLISH)
    private var dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    private var timeFormat: DateFormat = SimpleDateFormat("h:mm aa", Locale.ENGLISH)
    private lateinit var dateTimeNow: Date
    private lateinit var timeFromData: String
    private lateinit var pastDate: String
    private var sDateTimeNow: String
    //private static final int YEARS_MILLIS = 12 * MONTHS_MILLIS;

    init {

        val now = Date()
        sDateTimeNow = simpleDateFormat.format(now)

        try {
            dateTimeNow = simpleDateFormat.parse(sDateTimeNow)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

    }

    fun getTimeAgo(startDate: Date): String {

        //  date counting is done till todays date
        val endDate = dateTimeNow

        //  time difference in milli seconds
        val different = endDate.time - startDate.time

        when {
            different < MINUTE_MILLIS -> return "just now"
            different < 2 * MINUTE_MILLIS -> return "a min ago"
            different < 50 * MINUTE_MILLIS -> return (different / MINUTE_MILLIS).toString() + " mins ago"
            different < 90 * MINUTE_MILLIS -> return "a hour ago"
            different < 24 * HOUR_MILLIS -> {
                timeFromData = timeFormat.format(startDate)
                return timeFromData
            }
            different < 48 * HOUR_MILLIS -> return "Yesterday"
            different < 7 * DAY_MILLIS -> return (different / DAY_MILLIS).toString() + " days ago"
            different < 2 * WEEKS_MILLIS -> return (different / WEEKS_MILLIS).toString() + " week ago"
            different < 3.5 * WEEKS_MILLIS -> return (different / WEEKS_MILLIS).toString() + " weeks ago"
            else -> {
                pastDate = dateFormat.format(startDate)
                return pastDate
            }
        }
    }

    fun getTimeAgo(millis: Long): String {
        val c = Calendar.getInstance()
        c.timeInMillis = millis
        val date = c.time
        return getTimeAgo(date)
    }
}
