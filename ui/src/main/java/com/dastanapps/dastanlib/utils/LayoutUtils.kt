package com.dastanapps.dastanlib.utils

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import android.view.View
import android.widget.RelativeLayout

/**
 * Created by dastaniqbal on 19/07/2017.
 * 19/07/2017 6:30
 */


object LayoutUtils {
    fun setRLParams(w: Int, h: Int, v: Int, rlView: View) {
        val params = RelativeLayout.LayoutParams(w, h)
        params.addRule(v)
        rlView.layoutParams = params
    }

    fun setRLParams(w: Int, h: Int, rlView: View, vararg verb: Int) {
        val params = RelativeLayout.LayoutParams(w, h)
        for (aVerb in verb) {
            params.addRule(aVerb)
        }
        rlView.layoutParams = params
    }

    fun setConstraintRatio(layout: ConstraintLayout, viewId: Int, ratio: String) {
        val set = ConstraintSet()
        set.clone(layout)
        set.setDimensionRatio(viewId, ratio)
        set.applyTo(layout)
    }
}
