package com.dastanapps.dastanlib.network

import com.dastanapps.dastanlib.log.Logger
import com.google.gson.GsonBuilder

/**
 * Created by ask2iqbal@gmail.com on 18/11/16.
 */

object DGson {
    private val TAG = DGson::class.java.simpleName

    fun <T> getClass(json: String, clz: Class<T>): T? {
        return try {
            val gson = GsonBuilder().create()
            gson.fromJson(json, clz)
        } catch (e: Exception) {
            Logger.e(TAG, e.message!!)
            null
        }

    }

    //    public static  <T> T getClassArray(String json, Class<T> clz) {
    //        Gson gson = new GsonBuilder().create();
    //        Type listType = new TypeToken<List<DocIn>>(){}.getType();
    //        return gson.fromJson(json, listType);
    //    }

    fun getJson(clz: Any): String {
        val gson = GsonBuilder().create()
        return gson.toJson(clz)
    }
}
