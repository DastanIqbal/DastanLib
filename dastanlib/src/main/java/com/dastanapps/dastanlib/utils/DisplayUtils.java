package com.dastanapps.dastanlib.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.dastanapps.dastanlib.DastanApp;

/**
 * Created by devarajahe on 16/2/16.
 */
public class DisplayUtils {

    public static int getDeviceWidth(Activity ctx) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getDeviceHegiht(Activity ctx) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    public static String getDeviceResolution(Context mContext) {
        int density = mContext.getResources().getDisplayMetrics().densityDpi;

        switch (density) {
            case DisplayMetrics.DENSITY_MEDIUM:
                return "MDPI";
            case DisplayMetrics.DENSITY_HIGH:
                return "HDPI";
            case DisplayMetrics.DENSITY_LOW:
                return "LDPI";
            case DisplayMetrics.DENSITY_XHIGH:
                return "XHDPI";
            case DisplayMetrics.DENSITY_TV:
                return "TV";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "XXHDPI";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "XXXHDPI";
            default:
                return "Unknown";
        }
    }

    public static float convertSpToPx(float scaledPixels) {
        float scaledDensity = DastanApp.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return scaledPixels * scaledDensity;
    }

    public static int convertToPx(float scaledPixels) {
        float scaledDensity = DastanApp.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (scaledPixels * scaledDensity);
    }

    public static float convertPxToSp(float scaledPixels) {
        float scaledDensity = DastanApp.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return scaledPixels / scaledDensity;
    }

    public static float convertToDp(float scaledPixels) {
        float scaledDensity = DastanApp.getInstance().getResources().getDisplayMetrics().density;
        return scaledPixels * scaledDensity;
    }

    public static float getDIP(float value) {
        DisplayMetrics metrics = DastanApp.getInstance().getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                metrics);
    }
}