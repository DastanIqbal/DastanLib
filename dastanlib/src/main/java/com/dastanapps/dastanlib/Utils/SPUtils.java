package com.dastanapps.dastanlib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Iqbal Ahmed on 10/10/2015.
 */
public class SPUtils {
    private static final int MAX_ENTRIES = 10;
    private static SharedPreferences sharedPreferences;

    private static SharedPreferences getSP(Context ctxt) {
        if (sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctxt);
        return sharedPreferences;
    }

    //TODO: create hashmap and access value using key and store
}

