package com.dastanapps.dastanlib.Utils;

import android.util.Log;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;

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

}
