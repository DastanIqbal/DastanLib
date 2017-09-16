package com.dastanapps.dastanlib.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dastanapps.dastanlib.DastanApp;

/**
 * Created by Iqbal Ahmed on 10/10/2015.
 */
public class SPUtils<T> {

    private static SharedPreferences sharedPreferences;
    private static SPUtils spUtils;

    public static SPUtils getSPInstance() {
        if (spUtils == null)
            spUtils = new SPUtils();
        return spUtils;
    }

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

    public void writeSP(String key, T v) {
        if (v instanceof String) {
            String value = (String) v;
            getSP().edit().putString(key, value).apply();
        } else if (v instanceof Float) {
            Float value = (Float) v;
            getSP().edit().putFloat(key, value).apply();
        } else if (v instanceof Integer) {
            Integer value = (Integer) v;
            getSP().edit().putInt(key, value).apply();
        } else if (v instanceof Long) {
            Long value = (Long) v;
            getSP().edit().putLong(key, value).apply();
        } else if (v instanceof Boolean) {
            Boolean value = (Boolean) v;
            getSP().edit().putBoolean(key, value).apply();
        }
    }

    public T readSP(String key, Class<T> v, T def) {
        if (v == String.class) {
            return v.cast(getSP().getString(key, (String) def));
        } else if (v == Float.class) {
            return v.cast(getSP().getFloat(key, (Float) def));
        } else if (v == Integer.class) {
            return v.cast(getSP().getInt(key, (Integer) def));
        } else if (v == Long.class) {
            return v.cast(getSP().getLong(key, (Long) def));
        } else if (v == Boolean.class) {
            return v.cast(getSP().getBoolean(key, (Boolean) def));
        }
        return null;
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

