package com.dastanapps.dastanlib.utils;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by dastaniqbal on 19/07/2017.
 * 19/07/2017 6:30
 */

public class LayoutUtils {
    public static void setRLParams(int w, int h, int v, View rlView) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
        params.addRule(v);
        rlView.setLayoutParams(params);
    }

    public static void setRLParams(int w, int h, View rlView, int... verb) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
        for (int aVerb : verb) {
            params.addRule(aVerb);
        }
        rlView.setLayoutParams(params);
    }

    public static void setConstraintRatio(ConstraintLayout layout, int viewId, String ratio) {
        ConstraintSet set = new ConstraintSet();
        set.clone(layout);
        set.setDimensionRatio(viewId, ratio);
        set.applyTo(layout);
    }
}
