package com.dastanapps.dastanlib.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.dastanapps.dastanlib.R;


/**
 * Created by IQBAL-MEBELKART on 12/23/2015.
 */
public class FontUtils {

    private static Typeface tf;

    /**
     * Setting Robot Light Font
     * @param tv
     */
    public static void setRobotoLight(Context ctxt,TextView...tv) {
        tf = Typeface.createFromAsset(ctxt.getAssets(),
                ctxt.getResources().getString(R.string.robotoLight));
        for(TextView tview:tv) {
            tview.setTypeface(tf);
        }
    }

    /**
     * Setting Robot Medium Font
     * @param tv
     */
    public static void setRobotoMedium(Context ctxt,TextView...tv) {
        tf = Typeface.createFromAsset(ctxt.getAssets(),
                ctxt.getResources().getString(R.string.robotoMedium));
        for(TextView tview:tv) {
            tview.setTypeface(tf);
        }
    }

    /**
     * Setting Robot Regular Font
     * @param tv
     */
    public static void setRobotoRegular(Context ctxt,TextView...tv) {
        tf = Typeface.createFromAsset(ctxt.getAssets(),
                ctxt.getResources().getString(R.string.robotoRegular));
        for(TextView tview:tv) {
            tview.setTypeface(tf);
        }
    }

    /**
     * Setting Robot Thin Font
     * @param tv
     */
    public static void setRobotoThin(Context ctxt,TextView...tv) {
        tf = Typeface.createFromAsset(ctxt.getAssets(),
                ctxt.getResources().getString(R.string.robotoThin));
        for(TextView tview:tv) {
            tview.setTypeface(tf);
        }
    }

    public static int getDimensFromRes(Context ctxt, int dimenId){
        int size=(int)(ctxt.getResources().getDimension(dimenId)/ctxt.getResources().getDisplayMetrics().density);
        return size;
    }
}
