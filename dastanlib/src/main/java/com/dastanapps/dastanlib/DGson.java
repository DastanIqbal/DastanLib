package com.dastanapps.dastanlib;

import com.dastanapps.dastanlib.log.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by ask2iqbal@gmail.com on 18/11/16.
 */

public class DGson {
    private static String TAG = DGson.class.getSimpleName();

    public static <T> T getClass(String json, Class<T> clz) {
        try {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(json, clz);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
            return null;
        }
    }

//    public static  <T> T getClassArray(String json, Class<T> clz) {
//        Gson gson = new GsonBuilder().create();
//        Type listType = new TypeToken<List<DocIn>>(){}.getType();
//        return gson.fromJson(json, listType);
//    }

    public static String getJson(Object clz) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(clz);
    }
}
