package com.dastanapps.dastanlib.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Admin on 7/10/2017.
 */

public class BitmapUtils {

    public static Bitmap createScaledBitmap(Bitmap inBitmap, int maxSize) {
        int inWidth = inBitmap.getWidth();
        int inHeight = inBitmap.getHeight();
        int targetSize = inWidth > inHeight ? inWidth : inHeight;
        float divider = (float) maxSize / targetSize;
        int outWidth = (int) (inWidth * divider);
        int outHeight = (int) (inHeight * divider);
        Bitmap outBitmap = Bitmap.createScaledBitmap(inBitmap,
                outWidth, outHeight, false);
        return outBitmap;
    }

    public static Bitmap addShadow(final Bitmap bm, int color, int size, float dx, float dy) {
        int dstWidth = bm.getWidth();
        int dstHeight = bm.getHeight();

        final Bitmap mask = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ALPHA_8);

        final Matrix scaleToFit = new Matrix();
        final RectF src = new RectF(0, 0, bm.getWidth(), bm.getHeight());
        final RectF dst = new RectF(0, 0, dstWidth - dx, dstHeight - dy);
        scaleToFit.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);

        final Matrix dropShadow = new Matrix(scaleToFit);
        dropShadow.postTranslate(dx, dy);

        final Canvas maskCanvas = new Canvas(mask);
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskCanvas.drawBitmap(bm, scaleToFit, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        maskCanvas.drawBitmap(bm, dropShadow, paint);

        final BlurMaskFilter filter = new BlurMaskFilter(size, BlurMaskFilter.Blur.SOLID);
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setMaskFilter(filter);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        final Bitmap ret = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888);
        final Canvas retCanvas = new Canvas(ret);
        retCanvas.drawBitmap(mask, 0, 0, paint);
        retCanvas.drawBitmap(bm, scaleToFit, null);
        mask.recycle();
        return ret;
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int maxSize) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        final int oriSize = width > height ? width : height;

        if (oriSize > maxSize) {
            while ((oriSize / inSampleSize) >= maxSize) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap scaleDownAndRotateBitmap(String path, int maxSize) {//you can provide file path here
        int orientation;
        try {
            if (path == null) {
                return null;
            }
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // Find the correct scale value. It should be the power of 2.
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = calculateInSampleSize(o, maxSize);
            Bitmap bm = BitmapFactory.decodeFile(path, o2);
            Bitmap bitmap = bm;

            ExifInterface exif = new ExifInterface(path);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            Matrix m = new Matrix();
            if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
                m.postRotate(180);
                //m.postScale((float) bm.getWidth(), (float) bm.getHeight());
                // if(m.preRotate(90)){
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                m.postRotate(90);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                m.postRotate(270);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
                return bitmap;
            }
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap createBitmap(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bitmap.eraseColor(color);
        return bitmap;
    }

    public static Point getImageSize(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        return new Point(imageWidth, imageHeight);
    }

    public static boolean createPatternImage(int w, int h, String patternPath, String savePath) {
        Bitmap bmp = BitmapFactory.decodeFile(patternPath);
        BitmapShader bitmapShader = new BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);
        canvas.drawPaint(paint);

        bitmap.setDensity(w / h);
        try {
            OutputStream stream = new FileOutputStream(savePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        bitmap.recycle();
        bmp.recycle();
        return true;
    }

    public static Bitmap createPatternBitmap(String patternPath, Point screenSize) {
        Bitmap bmp = BitmapFactory.decodeFile(patternPath);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
        paint.setAntiAlias(true);

        BitmapShader bitmapShader = new BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        paint.setShader(bitmapShader);

        Rect mDstRect = new Rect();
        //copyBound
        mDstRect.set(0, 0, screenSize.x, screenSize.y);

        Bitmap bitmap = Bitmap.createBitmap(screenSize.x, screenSize.y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(mDstRect, paint);

//        try {
//            OutputStream stream = new FileOutputStream(savePath);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        }
        bmp.recycle();
        return bitmap;
    }

    public static Bitmap createGradientBitmap(int[] colors, Point screenSize) {
        GradientDrawable dynamicDrawable = new GradientDrawable();
        dynamicDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        dynamicDrawable.setUseLevel(false);
        dynamicDrawable.setColors(colors);
        dynamicDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);

        //copyBound
        Rect mDstRect = new Rect();
        mDstRect.set(0, 0, screenSize.x, screenSize.y);
        dynamicDrawable.setBounds(mDstRect);

        Bitmap bitmap = Bitmap.createBitmap(screenSize.x, screenSize.y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        dynamicDrawable.draw(canvas);

        return bitmap;
    }

    public static boolean resizeBitmap(int w, int h, String resizePath, Bitmap bmpToScale) {
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);

        Bitmap scaledBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        float scaleX = w / (float) bmpToScale.getWidth();
        float scaleY = h / (float) bmpToScale.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas cvas = new Canvas(scaledBitmap);
        cvas.setMatrix(scaleMatrix);
        cvas.drawBitmap(bmpToScale, 0, 0, paint);

        File file = new File(resizePath);
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            scaledBitmap.recycle();
            bmpToScale.recycle();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean resizeBitmap(String resizePath, Bitmap bmpToScale) {
        File file = new File(resizePath);
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bmpToScale.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            bmpToScale.recycle();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // We ask for the bounds if they have been set as they would be most
        // correct, then we check we are  > 0
        final int width = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().height() : drawable.getIntrinsicHeight();

        // Now we check we are > 0
        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width, height <= 0 ? 1 : height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Drawable gradientDrawable(int colors[]) {
        GradientDrawable dynamicDrawable = new GradientDrawable();
        dynamicDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        dynamicDrawable.setUseLevel(false);
        dynamicDrawable.setColors(colors);
        dynamicDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        return dynamicDrawable;
    }

    public static Drawable gradientCircleDrawable(int colors[]) {
        GradientDrawable dynamicDrawable = new GradientDrawable();
        dynamicDrawable.setShape(GradientDrawable.OVAL);
        dynamicDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        dynamicDrawable.setUseLevel(false);
        dynamicDrawable.setColors(colors);
        dynamicDrawable.setCornerRadius(10f);
        dynamicDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        return dynamicDrawable;
    }

    public static GradientDrawable gradientCircleDrawable(int colors) {
        GradientDrawable dynamicDrawable = new GradientDrawable();
        dynamicDrawable.setShape(GradientDrawable.OVAL);
        dynamicDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        dynamicDrawable.setUseLevel(false);
        dynamicDrawable.setColor(colors);
        dynamicDrawable.setCornerRadius(10f);
        dynamicDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        return dynamicDrawable;
    }

    public static GradientDrawable gradientRoundedDrawable(int colors) {
        GradientDrawable dynamicDrawable = new GradientDrawable();
        dynamicDrawable.setShape(GradientDrawable.RECTANGLE);
        dynamicDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        dynamicDrawable.setUseLevel(false);
        dynamicDrawable.setColor(colors);
        dynamicDrawable.setCornerRadius(16f);
        dynamicDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        return dynamicDrawable;
    }

    public static GradientDrawable gradientRoundedDrawable(int colors[]) {
        return gradientRoundedDrawable(16f, colors);
    }

    public static GradientDrawable gradientRoundedDrawable(float radius, int colors[]) {
        GradientDrawable dynamicDrawable = new GradientDrawable();
        dynamicDrawable.setShape(GradientDrawable.RECTANGLE);
        dynamicDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        dynamicDrawable.setUseLevel(false);
        dynamicDrawable.setColors(colors);
        dynamicDrawable.setCornerRadius(16f);
        dynamicDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        return dynamicDrawable;
    }

    public static int[] convertColors(String colorString) {
        int colors[] = new int[0];
        try {
            JSONArray colorArray = new JSONArray(colorString);
            colors = new int[colorArray.length()];
            for (int i = 0; i < colorArray.length(); i++) {
                colors[i] = Color.parseColor(colorArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return colors;
    }
}
