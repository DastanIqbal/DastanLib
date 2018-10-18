package com.dastanapps.dastanlib.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class DrawableUtils {

    public static StateListDrawable createSelector(Drawable normalIconDrawable, Drawable selectedIconDrawable) {
        StateListDrawable res = new StateListDrawable();
        res.addState(new int[]{ android.R.attr.state_selected }, selectedIconDrawable);
        res.addState(new int[]{}, normalIconDrawable);
        return res;
    }

    public static GradientDrawable createGradientDrawable(int[] colors) {
        GradientDrawable shape = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        shape.setShape(GradientDrawable.RECTANGLE);
        return shape;
    }
}
