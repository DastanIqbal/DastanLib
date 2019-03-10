package com.dastanapps.dastanlib.utils;

import android.content.Context;

/**
 * Created by Admin on 17-Aug-17.
 */

public class ResourceUtils {

    public static int getDrawableResId(Context context, String filename) {
        return context.getResources().getIdentifier(filename,
                "drawable", context.getPackageName());
    }
}
