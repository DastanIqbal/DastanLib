package com.dastanapps.dastanlib.utils;

import android.content.pm.PackageManager;

import com.dastanapps.dastanlib.DastanApp;


/**
 * Created by dastaniqbal on 15/12/2016.
 * 15/12/2016 4:20
 */

public class AppUtils {
    public static int getVersionCode() throws PackageManager.NameNotFoundException {
        return DastanApp.getInstance().getPackageManager().getPackageInfo(DastanApp.getInstance().getPackageName(), 0).versionCode;
    }
}
