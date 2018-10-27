package com.dastanapps.dastanlib.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View

object WindowUtils {

    fun setStatusBarColor(activity: Activity?, color: Int, whiteIcon: Boolean) {
        if (activity == null) {
            return
        }
        setStatusIconColor(activity, whiteIcon)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = color
        }
    }

    fun setStatusIconColor(activity: Activity?, white: Boolean) {
        if (activity == null) {
            return
        }
        val window = activity.window
        window.decorView.systemUiVisibility = if (white) 0 else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}
