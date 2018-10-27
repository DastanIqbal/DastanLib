package com.dastanapps.dastanlib.utils

import android.content.Context
import com.dastanapps.dastanlib.DastanLibApp

/**
 * Created by Iqbal Ahmed on 10/10/2015.
 */

object SPUtils {

    private val SP by lazy {
        DastanLibApp.INSTANCE.getSharedPreferences("dastanlib", Context.MODE_PRIVATE)
    }

    fun writeSP(key: String, value: Any) {
        when (value) {
            is String -> {
                SP.edit().putString(key, value).apply()
            }
            is Float -> {
                SP.edit().putFloat(key, value).apply()
            }
            is Int -> {
                SP.edit().putInt(key, value).apply()
            }
            is Long -> {
                SP.edit().putLong(key, value).apply()
            }
            is Boolean -> {
                SP.edit().putBoolean(key, value).apply()
            }
        }
    }

    fun readSP(key: String, def: Any): Any? {
        return when (def) {
            is String -> SP.getString(key, def)
            is Float -> SP.getFloat(key, def)
            is Int -> SP.getInt(key, def)
            is Long -> SP.getLong(key, def)
            is Boolean -> SP.getBoolean(key, def)
            else -> null
        }
    }
}


