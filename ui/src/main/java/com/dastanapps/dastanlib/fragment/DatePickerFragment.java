package com.dastanapps.dastanlib.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private IDatePickerResult idatepickerresult;

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat mSDF = new SimpleDateFormat("dd MMM, yyyy");
        String date = mSDF.format(mCalendar.getTime());
        idatepickerresult.onDateSet(date);
    }

    public interface IDatePickerResult {
        void onDateSet(String date);
    }

    public void setOnDateSet(IDatePickerResult iTimePickerResult) {
        this.idatepickerresult = iTimePickerResult;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this,year, month, day );
    }

    public boolean checkBefore(String startDate, String endDate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
        try {
            Date date1 = sdf.parse(startDate);
            Date date2 = sdf.parse(endDate);

            return date1.before(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}