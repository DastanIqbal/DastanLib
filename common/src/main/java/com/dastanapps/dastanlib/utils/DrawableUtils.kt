package com.dastanapps.dastanlib.utils

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable

object DrawableUtils {

    fun createSelector(normalIconDrawable: Drawable, selectedIconDrawable: Drawable): StateListDrawable {
        val res = StateListDrawable()
        res.addState(intArrayOf(android.R.attr.state_selected), selectedIconDrawable)
        res.addState(intArrayOf(), normalIconDrawable)
        return res
    }

    fun createGradientDrawable(colors: IntArray): GradientDrawable {
        val shape = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
        shape.shape = GradientDrawable.RECTANGLE
        return shape
    }
}
