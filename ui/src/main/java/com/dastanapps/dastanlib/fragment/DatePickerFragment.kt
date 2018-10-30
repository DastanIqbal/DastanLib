package com.dastanapps.dastanlib.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var idatepickerresult: IDatePickerResult? = null

    override fun onDateSet(datePicker: DatePicker, year: Int, month: Int, day: Int) {
        val mCalendar = Calendar.getInstance()
        mCalendar.set(Calendar.YEAR, year)
        mCalendar.set(Calendar.MONTH, month)
        mCalendar.set(Calendar.DAY_OF_MONTH, day)
        val mSDF = SimpleDateFormat("dd MMM, yyyy")
        val date = mSDF.format(mCalendar.time)
        idatepickerresult!!.onDateSet(date)
    }

    interface IDatePickerResult {
        fun onDateSet(date: String)
    }

    fun setOnDateSet(iTimePickerResult: IDatePickerResult) {
        this.idatepickerresult = iTimePickerResult
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of TimePickerDialog and return it
        return DatePickerDialog(activity!!, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day)
    }

    fun checkBefore(startDate: String, endDate: String): Boolean {
        val sdf = SimpleDateFormat("dd MMM, yyyy")
        try {
            val date1 = sdf.parse(startDate)
            val date2 = sdf.parse(endDate)

            return date1.before(date2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return false
    }
}