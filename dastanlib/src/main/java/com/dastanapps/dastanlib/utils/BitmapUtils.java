package com.dastanapps.dastanlib.utils;

import android.graphics.Bitmap;

/**
 * Created by Admin on 7/10/2017.
 */

public class BitmapUtils {

    public static Bitmap createScaledBitmap(Bitmap inBitmap, int maxSize) {
        int inWidth = inBitmap.getWidth();
        int inHeight = inBitmap.getHeight();
        int targetSize = inWidth > inHeight ? inWidth : inHeight;
        float divider = (float)maxSize / targetSize;
        int outWidth = (int) (inWidth * divider);
        int outHeight = (int) (inHeight * divider);
        Bitmap outBitmap = Bitmap.createScaledBitmap(inBitmap,
                outWidth, outHeight, false);
        return outBitmap;
    }

    public static Bitmap createBitmap(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(color);
        return bitmap;
    }
}
