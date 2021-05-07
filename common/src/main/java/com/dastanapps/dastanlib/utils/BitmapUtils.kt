package com.dastanapps.dastanlib.utils

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.media.ExifInterface
import org.json.JSONArray
import org.json.JSONException
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

/**
 * Created by Iqbal Ahmed on 7/10/2017.
 */

object BitmapUtils {

    fun createScaledBitmap(inBitmap: Bitmap, maxSize: Int): Bitmap {
        val inWidth = inBitmap.width
        val inHeight = inBitmap.height
        val targetSize = if (inWidth > inHeight) inWidth else inHeight
        val divider = maxSize.toFloat() / targetSize
        val outWidth = (inWidth * divider).toInt()
        val outHeight = (inHeight * divider).toInt()
        return Bitmap.createScaledBitmap(inBitmap,
                outWidth, outHeight, false)
    }

    fun addShadow(bm: Bitmap, color: Int, size: Int, dx: Float, dy: Float): Bitmap {
        val dstWidth = bm.width
        val dstHeight = bm.height

        val mask = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ALPHA_8)

        val scaleToFit = Matrix()
        val src = RectF(0f, 0f, bm.width.toFloat(), bm.height.toFloat())
        val dst = RectF(0f, 0f, dstWidth - dx, dstHeight - dy)
        scaleToFit.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER)

        val dropShadow = Matrix(scaleToFit)
        dropShadow.postTranslate(dx, dy)

        val maskCanvas = Canvas(mask)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        maskCanvas.drawBitmap(bm, scaleToFit, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        maskCanvas.drawBitmap(bm, dropShadow, paint)

        val filter = BlurMaskFilter(size.toFloat(), BlurMaskFilter.Blur.SOLID)
        paint.reset()
        paint.isAntiAlias = true
        paint.color = color
        paint.maskFilter = filter
        paint.isFilterBitmap = true
        paint.isDither = true

        val ret = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888)
        val retCanvas = Canvas(ret)
        retCanvas.drawBitmap(mask, 0f, 0f, paint)
        retCanvas.drawBitmap(bm, scaleToFit, null)
        mask.recycle()
        return ret
    }

    private fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private fun calculateInSampleSize(
            options: BitmapFactory.Options, maxSize: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        val oriSize = if (width > height) width else height

        if (oriSize > maxSize) {
            while (oriSize / inSampleSize >= maxSize) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    fun scaleDownAndRotateBitmap(path: String?, maxSize: Int): Bitmap? {//you can provide file path here
        val orientation: Int
        try {
            if (path == null) {
                return null
            }
            // decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, o)
            // Find the correct scale value. It should be the power of 2.
            // decode with inSampleSize
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = calculateInSampleSize(o, maxSize)
            val bm = BitmapFactory.decodeFile(path, o2)
            var bitmap = bm

            val exif = ExifInterface(path)
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)

            val m = Matrix()
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                m.postRotate(180f)
                //m.postScale((float) bm.getWidth(), (float) bm.getHeight());
                // if(m.preRotate(90)){
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, m, true)
                return bitmap
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                m.postRotate(90f)
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, m, true)
                return bitmap
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                m.postRotate(270f)
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, m, true)
                return bitmap
            }
            return bitmap
        } catch (e: Exception) {
            return null
        }

    }

    fun createBitmap(width: Int, height: Int, color: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        bitmap.eraseColor(color)
        return bitmap
    }

    fun getImageSize(path: String): Point {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth
        return Point(imageWidth, imageHeight)
    }

    fun createPatternImage(w: Int, h: Int, patternPath: String, savePath: String): Boolean {
        val bmp = BitmapFactory.decodeFile(patternPath)
        val bitmapShader = BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)

        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = bitmapShader
        canvas.drawPaint(paint)

        bitmap.density = w / h
        try {
            val stream = FileOutputStream(savePath)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return false
        }

        bitmap.recycle()
        bmp.recycle()
        return true
    }

    fun createPatternBitmap(patternPath: String, screenSize: Point): Bitmap {
        val bmp = BitmapFactory.decodeFile(patternPath)
        val paint = Paint(Paint.FILTER_BITMAP_FLAG or Paint.DITHER_FLAG)
        paint.isAntiAlias = true

        val bitmapShader = BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        paint.shader = bitmapShader

        val mDstRect = Rect()
        //copyBound
        mDstRect.set(0, 0, screenSize.x, screenSize.y)

        val bitmap = Bitmap.createBitmap(screenSize.x, screenSize.y, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawRect(mDstRect, paint)

        //        try {
        //            OutputStream stream = new FileOutputStream(savePath);
        //            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        //        } catch (FileNotFoundException e) {
        //            e.printStackTrace();
        //            return null;
        //        }
        bmp.recycle()
        return bitmap
    }

    fun createGradientBitmap(colors: IntArray, screenSize: Point): Bitmap {
        val dynamicDrawable = GradientDrawable()
        dynamicDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        dynamicDrawable.useLevel = false
        dynamicDrawable.colors = colors
        dynamicDrawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT

        //copyBound
        val mDstRect = Rect()
        mDstRect.set(0, 0, screenSize.x, screenSize.y)
        dynamicDrawable.bounds = mDstRect

        val bitmap = Bitmap.createBitmap(screenSize.x, screenSize.y, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        dynamicDrawable.draw(canvas)

        return bitmap
    }

    fun resizeBitmap(w: Int, h: Int, resizePath: String, bmpToScale: Bitmap): Boolean {
        val paint = Paint(Paint.FILTER_BITMAP_FLAG or Paint.DITHER_FLAG)
        paint.isAntiAlias = true
        paint.isDither = true
        paint.isFilterBitmap = true

        val scaledBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

        val scaleX = w / bmpToScale.width.toFloat()
        val scaleY = h / bmpToScale.height.toFloat()
        val pivotX = 0f
        val pivotY = 0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY)

        val cvas = Canvas(scaledBitmap)
        cvas.setMatrix(scaleMatrix)
        cvas.drawBitmap(bmpToScale, 0f, 0f, paint)

        val file = File(resizePath)
        val fOut: FileOutputStream
        try {
            fOut = FileOutputStream(file)
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()
            scaledBitmap.recycle()
            bmpToScale.recycle()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }

    fun resizeBitmap(resizePath: String, bmpToScale: Bitmap): Boolean {
        val file = File(resizePath)
        val fOut: FileOutputStream
        try {
            fOut = FileOutputStream(file)
            bmpToScale.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()
            bmpToScale.recycle()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        // We ask for the bounds if they have been set as they would be most
        // correct, then we check we are  > 0
        val width = if (!drawable.bounds.isEmpty)
            drawable.bounds.width()
        else
            drawable.intrinsicWidth

        val height = if (!drawable.bounds.isEmpty)
            drawable.bounds.height()
        else
            drawable.intrinsicHeight

        // Now we check we are > 0
        val bitmap = Bitmap.createBitmap(if (width <= 0) 1 else width, if (height <= 0) 1 else height,
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    fun gradientDrawable(colors: IntArray): Drawable {
        val dynamicDrawable = GradientDrawable()
        dynamicDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        dynamicDrawable.useLevel = false
        dynamicDrawable.colors = colors
        dynamicDrawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
        return dynamicDrawable
    }

    fun gradientCircleDrawable(colors: IntArray): Drawable {
        val dynamicDrawable = GradientDrawable()
        dynamicDrawable.shape = GradientDrawable.OVAL
        dynamicDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        dynamicDrawable.useLevel = false
        dynamicDrawable.colors = colors
        dynamicDrawable.cornerRadius = 10f
        dynamicDrawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
        return dynamicDrawable
    }

    fun gradientCircleDrawable(colors: Int): GradientDrawable {
        val dynamicDrawable = GradientDrawable()
        dynamicDrawable.shape = GradientDrawable.OVAL
        dynamicDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        dynamicDrawable.useLevel = false
        dynamicDrawable.setColor(colors)
        dynamicDrawable.cornerRadius = 10f
        dynamicDrawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
        return dynamicDrawable
    }

    fun gradientRoundedDrawable(colors: Int): GradientDrawable {
        val dynamicDrawable = GradientDrawable()
        dynamicDrawable.shape = GradientDrawable.RECTANGLE
        dynamicDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        dynamicDrawable.useLevel = false
        dynamicDrawable.setColor(colors)
        dynamicDrawable.cornerRadius = 16f
        dynamicDrawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
        return dynamicDrawable
    }

    fun gradientRoundedDrawable(colors: IntArray): GradientDrawable {
        return gradientRoundedDrawable(16f, colors)
    }

    fun gradientRoundedDrawable(radius: Float, colors: IntArray): GradientDrawable {
        val dynamicDrawable = GradientDrawable()
        dynamicDrawable.shape = GradientDrawable.RECTANGLE
        dynamicDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        dynamicDrawable.useLevel = false
        dynamicDrawable.colors = colors
        dynamicDrawable.cornerRadius = 16f
        dynamicDrawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
        return dynamicDrawable
    }

    fun convertColors(colorString: String): IntArray {
        var colors = IntArray(0)
        try {
            val colorArray = JSONArray(colorString)
            colors = IntArray(colorArray.length())
            for (i in 0 until colorArray.length()) {
                colors[i] = Color.parseColor(colorArray.getString(i))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return colors
    }
}
