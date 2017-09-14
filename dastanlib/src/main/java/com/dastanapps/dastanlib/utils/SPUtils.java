package com.dastanapps.dastanlib.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dastanapps.dastanlib.DastanApp;

/**
 * Created by Iqbal Ahmed on 10/10/2015.
 */
public class SPUtils {

    private static SharedPreferences sharedPreferences;

    /**
     * Get Shared preference instance
     *
     * @return instance if available otherwise create new
     */
    private static SharedPreferences getSP() {
        if (sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DastanApp.getInstance());
        return sharedPreferences;
    }

    /**
     * Write string with App key
     *
     * @param key
     * @param value
     */
    public static void writeString(String key, String value) {
        getSP().edit().putString(key, value).commit();
    }

    /**
     * read string with App constant key
     *
     * @param key
     * @return value
     */
    public static String readString(String key) {
        return getSP().getString(key, "");
    }

    /**
     * read string with App constant key
     *
     * @param key
     * @param defaultValue
     * @return key value
     */
    public static String readString(String key, String defaultValue) {
        return getSP().getString(key, defaultValue);
    }

    /**
     * Write boolean with App key
     *
     * @param key
     * @param value
     */
    public static void writeBoolean(String key, boolean value) {
        getSP().edit().putBoolean(key, value).commit();
    }

    /**
     * read boolean with App constant key
     *
     * @param key
     * @return value
     */
    public static boolean readBoolean(String key) {
        return getSP().getBoolean(key, false);
    }

    /**
     * read boolean with App constant key
     *
     * @param key
     * @param defaultValue
     * @return key value
     */
    public static boolean readBoolean(String key, boolean defaultValue) {
        return getSP().getBoolean(key, defaultValue);
    }
}

