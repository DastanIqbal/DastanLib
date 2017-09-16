package com.dastanapps.dastanlib.utils;

import android.util.Log;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by IQBAL-MEBELKART on 12/31/2015.
 */
public class DateTimeUtils {
    public static String timeStampToDate(String s) {
        long timeInLong = Long.parseLong(s);
        Timestamp stamp = new Timestamp(timeInLong * 1000); //epoch to millisec converter
        Date dt = new Date(stamp.getTime());
        Log.i("format", DateFormat.getDateInstance().format(dt));
        return DateFormat.getDateInstance().format(dt).toString();
    }

    public static String currentTimeStampInDateTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(System.currentTimeMillis());
        return sdf.format(date);
    }

    public static String currentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        Date date = new Date(System.currentTimeMillis());
        return sdf.format(date);
    }

    public static String currentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aaa");
        Date date = new Date(System.currentTimeMillis());
        return sdf.format(date);
    }

    public static String getTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aaa");
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    public static String getDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    public static String getDate(String fmt,long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    public static String getDateTime(long timestamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    public static String convertSecToMinSecs(long seconds) {
        if (seconds == 0) return "";
        int m, sec;
        m = (int) seconds / 60;
        sec = (int) seconds % 60;
        if (m > 0 && sec > 0)
            return m + " min " + sec + " sec";
        if (m > 0 && sec == 0)
            return m + " min ";
        if (m == 0 && sec > 0)
            return sec + " sec";
        return "";
    }

    public static String convertMilliSecToMinSecs(long millis) {
        if (millis == 0) return "";
        return convertSecToMinSecs(millis / 1000);
    }
}
