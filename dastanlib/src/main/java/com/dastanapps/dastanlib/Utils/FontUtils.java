package com.dastanapps.dastanlib.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.mebelkart.app.MkartApp;
import com.mebelkart.app.R;

/**
 * Created by IQBAL-MEBELKART on 12/23/2015.
 */
public class FontUtils {

    private static Typeface tf;

    /**
     * Setting Robot Light Font
     * @param tv
     */
    public static void setRobotoLight(TextView...tv) {
        tf = Typeface.createFromAsset(MkartApp.getInstance().getAssets(),
                MkartApp.getInstance().getResources().getString(R.string.robotoLight));
        for(TextView tview:tv) {
            tview.setTypeface(tf);
        }
    }

    /**
     * Setting Robot Medium Font
     * @param tv
     */
    public static void setRobotoMedium(TextView...tv) {
        tf = Typeface.createFromAsset(MkartApp.getInstance().getAssets(),
                MkartApp.getInstance().getResources().getString(R.string.robotoMedium));
        for(TextView tview:tv) {
            tview.setTypeface(tf);
        }
    }

    /**
     * Setting Robot Regular Font
     * @param tv
     */
    public static void setRobotoRegular(TextView...tv) {
        tf = Typeface.createFromAsset(MkartApp.getInstance().getAssets(),
                MkartApp.getInstance().getResources().getString(R.string.robotoRegular));
        for(TextView tview:tv) {
            tview.setTypeface(tf);
        }
    }

    /**
     * Setting Robot Thin Font
     * @param tv
     */
    public static void setRobotoThin(TextView...tv) {
        tf = Typeface.createFromAsset(MkartApp.getInstance().getAssets(),
                MkartApp.getInstance().getResources().getString(R.string.robotoThin));
        for(TextView tview:tv) {
            tview.setTypeface(tf);
        }
    }

    public static int getDimensFromRes(Context ctxt, int dimenId){
        int size=(int)(ctxt.getResources().getDimension(dimenId)/ctxt.getResources().getDisplayMetrics().density);
        return size;
    }
}
