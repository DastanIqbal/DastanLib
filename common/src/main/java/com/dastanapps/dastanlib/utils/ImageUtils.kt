package com.dastanapps.dastanlib.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.view.View

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Iqbal Ahmed on 10/13/2015.
 */
object ImageUtils {
    val PICK_IMAGE = 700

    /**
     * Turn drawable resource into byte array.
     *
     * @param id drawable resource id
     * @return byte array
     */
    fun getFileDataFromDrawable(context: Context, id: Int): ByteArray {
        val drawable = ContextCompat.getDrawable(context, id)
        val bitmap = (drawable as BitmapDrawable).bitmap
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    /**
     * Turn drawable into byte array.
     *
     * @param drawable data
     * @return byte array
     */
    fun getFileDataFromDrawable(drawable: Drawable): ByteArray {
        val bitmap = (drawable as BitmapDrawable).bitmap
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun pickImage(ctxt: Context) {
        val intent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        //        intent.putExtra("crop", "true");
        intent.putExtra("scale", true)
        intent.putExtra("outputX", 256)
        intent.putExtra("outputY", 256)
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        //intent.putExtra("return-data", true);
        (ctxt as Activity).startActivityForResult(intent, PICK_IMAGE)
    }

    fun openImage(context: Context, imagePath: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val uri = Uri.fromFile(File(imagePath))
        intent.setDataAndType(uri, "image/*")
        context.startActivity(intent)
    }

    fun takeViewScreenshot(fileName: String, location: String, view: View, needMillis: Boolean): String {
        // create bitmap screen capture
        view.isDrawingCacheEnabled = true
        view.drawingCacheBackgroundColor = Color.WHITE
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false

        var mPath = Environment.getExternalStorageDirectory().toString() + "/" + location + "/"
        val imageFile = File(mPath)
        if (!imageFile.exists()) {
            imageFile.mkdirs()
        }
        if (needMillis)
            mPath += fileName + "_" + System.currentTimeMillis() + ".jpg"
        else
            mPath += "$fileName.jpg"
        var outputStream: FileOutputStream? = null
        try {
            outputStream = FileOutputStream(mPath)
            val quality = 50
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return mPath
    }

    fun saveImageFile(fileName: String, realPath: String, savePath: String, needMillis: Boolean): String {
        var mPath = Environment.getExternalStorageDirectory().toString() + "/" + savePath + "/"
        val imageFile = File(mPath)
        if (!imageFile.exists()) {
            imageFile.mkdirs()
        }
        if (needMillis)
            mPath += fileName + "_" + System.currentTimeMillis() + ".jpg"
        else
            mPath += "$fileName.jpg"
        val bitmap = BitmapFactory.decodeFile(realPath)
        val nh = (bitmap.height * (512.0 / bitmap.width)).toInt()
        val scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true)
        var outputStream: FileOutputStream? = null
        try {
            outputStream = FileOutputStream(mPath)
            val quality = 20
            scaled.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return mPath
    }
}
