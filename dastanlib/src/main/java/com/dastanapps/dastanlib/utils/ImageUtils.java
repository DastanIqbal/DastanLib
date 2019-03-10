package com.dastanapps.dastanlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.dastanapps.dastanlib.DastanApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by IQBAL-MEBELKART on 10/13/2015.
 */
public class ImageUtils {
    public static final int PICK_IMAGE = 700;

    /**
     * Turn drawable resource into byte array.
     *
     * @param id drawable resource id
     * @return byte array
     */
    public static byte[] getFileDataFromDrawable(int id) {
        Drawable drawable = ContextCompat.getDrawable(DastanApp.getInstance(), id);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Turn drawable into byte array.
     *
     * @param drawable data
     * @return byte array
     */
    public static byte[] getFileDataFromDrawable(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static void pickImage(Context ctxt) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
//        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //intent.putExtra("return-data", true);
        ((Activity) ctxt).startActivityForResult(intent, PICK_IMAGE);
    }

    public static void openImage(String imagePath) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(imagePath));
        intent.setDataAndType(uri, "image/*");
        DastanApp.getInstance().startActivity(intent);
    }

    public static String takeViewScreenshot(String fileName, String location, View view, boolean needMillis) {
        // create bitmap screen capture
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheBackgroundColor(Color.WHITE);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + location + "/";
        File imageFile = new File(mPath);
        if (!imageFile.exists()) {
            imageFile.mkdirs();
        }
        if (needMillis)
            mPath += fileName + "_" + System.currentTimeMillis() + ".jpg";
        else
            mPath += fileName + ".jpg";
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(mPath);
            int quality = 50;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mPath;
    }

    public static String saveImageFile(String fileName, String realPath, String savePath, boolean needMillis) {
        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + savePath + "/";
        File imageFile = new File(mPath);
        if (!imageFile.exists()) {
            imageFile.mkdirs();
        }
        if (needMillis)
            mPath += fileName + "_" + System.currentTimeMillis() + ".jpg";
        else
            mPath += fileName + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(realPath);
        int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(mPath);
            int quality = 20;
            scaled.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mPath;
    }
}
