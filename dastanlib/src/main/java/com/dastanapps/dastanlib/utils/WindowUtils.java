package com.dastanapps.dastanlib.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;

public class WindowUtils {

    public static void setStatusBarColor(Activity activity, int color, boolean whiteIcon) {
        if (activity == null) {
            return;
        }
        setStatusIconColor(activity, whiteIcon);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }
    }

    public static void setStatusIconColor(Activity activity, boolean white) {
        if (activity == null) {
            return;
        }
        Window window = activity.getWindow();
        window.getDecorView().setSystemUiVisibility(white ? 0 : View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
