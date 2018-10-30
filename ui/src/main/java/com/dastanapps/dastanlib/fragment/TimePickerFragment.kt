package com.dastanapps.dastanlib.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat
import android.widget.TimePicker
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var itimepickerresult: ITimePickerResult? = null

    interface ITimePickerResult {
        fun onTimeSet(time: String)
    }

    fun setOnTimeSet(iTimePickerResult: ITimePickerResult) {
        this.itimepickerresult = iTimePickerResult
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, hour, minute,
                DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val mCalendar = Calendar.getInstance()
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        mCalendar.set(Calendar.MINUTE, minute)
        val mSDF = SimpleDateFormat("hh:mm a")
        val time = mSDF.format(mCalendar.time)
        itimepickerresult!!.onTimeSet(time)
    }

    fun checkBefore(startTime: String, endTime: String): Boolean {
        val sdf = SimpleDateFormat("hh:mm a")
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