package com.dastanapps.dastanlib.utils

import android.content.Context

/**
 * Created by Iqbal Ahmed on 17-Aug-17.
 */

object ResourceUtils {

    fun getDrawableResId(context: Context, filename: String): Int {
        return context.resources.getIdentifier(filename,
                "drawable", context.packageName)
    }
}
