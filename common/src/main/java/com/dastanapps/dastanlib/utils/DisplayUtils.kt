package com.dastanapps.dastanlib.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue

/**
 * Created by Iqbal Ahmed on 16/2/16.
 */
object DisplayUtils {

    fun getDeviceWidth(ctx: Activity): Int {
        val displayMetrics = DisplayMetrics()
        ctx.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun getDeviceHegiht(ctx: Activity): Int {
        val displayMetrics = DisplayMetrics()
        ctx.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }


    fun getDeviceResolution(mContext: Context): String {
        val density = mContext.resources.displayMetrics.densityDpi

        return when (density) {
            DisplayMetrics.DENSITY_MEDIUM -> "MDPI"
            DisplayMetrics.DENSITY_HIGH -> "HDPI"
            DisplayMetrics.DENSITY_LOW -> "LDPI"
            DisplayMetrics.DENSITY_XHIGH -> "XHDPI"
            DisplayMetrics.DENSITY_TV -> "TV"
            DisplayMetrics.DENSITY_XXHIGH -> "XXHDPI"
            DisplayMetrics.DENSITY_XXXHIGH -> "XXXHDPI"
            else -> "Unknown"
        }
    }

    fun convertSpToPx(context: Context, scaledPixels: Float): Float {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return scaledPixels * scaledDensity
    }

    fun convertToPx(context: Context, scaledPixels: Float): Int {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return (scaledPixels * scaledDensity).toInt()
    }

    fun convertPxToSp(context: Context, scaledPixels: Float): Float {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return scaledPixels / scaledDensity
    }

    fun convertToDp(context: Context, scaledPixels: Float): Float {
        val scaledDensity = context.resources.displayMetrics.density
        return scaledPixels * scaledDensity
    }

    fun getDIP(context: Context, value: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                metrics)
    }
}