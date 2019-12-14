package com.dastanapps.dastanlib.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    interface IDatePickerResult {
        fun onDateSet(date: String, mCalendar: Calendar)
    }

    var idatepickerresult: IDatePickerResult? = null
    var defaultFmt = "dd MMM, yyyy";
    var locale = Locale.getDefault()

    override fun onDateSet(datePicker: DatePicker, year: Int, month: Int, day: Int) {
        val mCalendar = Calendar.getInstance()
        mCalendar.set(Calendar.YEAR, year)
        mCalendar.set(Calendar.MONTH, month)
        mCalendar.set(Calendar.DAY_OF_MONTH, day)
        val mSDF = SimpleDateFormat(defaultFmt, locale)
        val date = mSDF.format(mCalendar.time)
        idatepickerresult?.onDateSet(date,mCalendar)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of TimePickerDialog and return it
        return DatePickerDialog(requireActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day)
    }

    fun checkBefore(startDate: String, endDate: String): Boolean {
        val sdf = SimpleDateFormat(defaultFmt, locale)
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