package com.dastanapps.dastanlib.utils;

import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by dastaniqbal on 19/07/2017.
 * dastanIqbal@marvelmedia.com
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
}
