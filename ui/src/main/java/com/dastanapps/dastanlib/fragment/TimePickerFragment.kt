package com.dastanapps.dastanlib.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    var itimepickerresult: ITimePickerResult? = null
    var defaultTime = "hh:mm a"
    var locale = Locale.getDefault()
    var is24hr = true

    interface ITimePickerResult {
        fun onTimeSet(time: String, calendar: Calendar, locale: Locale)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = if (is24hr)
            c.get(Calendar.HOUR_OF_DAY)
        else c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, hour, minute,
                DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val mCalendar = Calendar.getInstance()
        if (is24hr)
            mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        else mCalendar.set(Calendar.HOUR, hourOfDay)
        mCalendar.set(Calendar.MINUTE, minute)
        val mSDF = SimpleDateFormat(defaultTime, locale)
        val time = mSDF.format(mCalendar.time)
        itimepickerresult?.onTimeSet(time, mCalendar, locale)
    }

    fun checkBefore(startTime: String, endTime: String): Boolean {
        val sdf = SimpleDateFormat(defaultTime, locale)
        try {
            val date1 = sdf.parse(startTime)
            val date2 = sdf.parse(endTime)

            return date1.before(date2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return false
    }
}