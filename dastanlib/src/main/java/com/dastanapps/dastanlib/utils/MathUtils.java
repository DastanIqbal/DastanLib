package com.dastanapps.dastanlib.utils;

import android.graphics.Matrix;
import android.graphics.RectF;

import java.math.BigDecimal;

/**
 * Created by dastaniqbal on 09/05/2017.
 * 09/05/2017 11:54
 */

public class MathUtils {
    public enum ScaleType {
        CENTER_CROP,
        CENTER_INSIDE
    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public static float truncateDigitsFromLast(float value, float truncateDigit) {
        if (truncateDigit == 0) return 0f;
        if (String.valueOf(value).length() >= truncateDigit) {
            float digit = 10f;
            for (int i = 1; i < truncateDigit; i++) digit *= 10f;
            return (value - value % digit) / digit;
        }
        return 0f;
    }

    public static Matrix getScaleToFitMatrix(int srcWidth, int srcHeight,
                                             int targetWidth, int targetHeight,
                                             Matrix.ScaleToFit scaleToFit) {
        return getScaleToFitMatrix(
                0,0, srcWidth, srcHeight,
                0,0, targetWidth, targetHeight,
                scaleToFit
        );
    }

    public static Matrix getScaleToFitMatrix(int srcX, int srcY, int srcWidth, int srcHeight,
                                             int targetX, int targetY, int targetWidth, int targetHeight,
                                             Matrix.ScaleToFit scaleToFit) {
        RectF srcRect = new RectF(srcX, srcY, srcWidth + srcX, srcHeight + srcY);
        RectF targetRect = new RectF(targetX, targetY, targetWidth + targetX, targetHeight + targetY);
        Matrix resultMatrix = new Matrix();
        resultMatrix.setRectToRect(srcRect, targetRect, scaleToFit);
        return resultMatrix;
    }

    public static Matrix getScaleMatrix(float srcWidth, float srcHeight, float aspectRatio, ScaleType scaleType) {
        if (scaleType == null) {
            scaleType = ScaleType.CENTER_INSIDE;
        }
        Matrix resultMat = new Matrix();
        RectF newRectF = new RectF();
        switch (scaleType) {
            case CENTER_INSIDE:
                newRectF.set(getCenterInsideSize(srcWidth, srcHeight, aspectRatio, scaleType));
                break;
            case CENTER_CROP:
                newRectF.set(getCenterCropSize(srcWidth, srcHeight, aspectRatio, scaleType));
                break;
        }

        float xoff = (srcWidth - newRectF.width()) / 2;
        float yoff = (srcHeight - newRectF.height()) / 2;

        resultMat.setScale(newRectF.width() / srcWidth, newRectF.height() / srcHeight);
        resultMat.postTranslate(xoff, yoff);
        return resultMat;
    }

    protected static RectF getCenterInsideSize(float srcWidth, float srcHeight, float aspectRatio, ScaleType scaleType) {
        RectF result = new RectF();
        if (srcHeight > (int) (srcWidth * aspectRatio)) {
            // limited by narrow width; restrict height
            result.set(
                    0 , 0,
                    srcWidth,
                    srcWidth * aspectRatio
            );
        } else {
            // limited by short height; restrict width
            result.set(
                    0 , 0,
                    srcHeight / aspectRatio,
                    srcHeight
            );
        }
        return result;
    }

    protected static RectF getCenterCropSize(float srcWidth, float srcHeight, float aspectRatio, ScaleType scaleType) {
        RectF result = new RectF();
        if (srcHeight > (int) (srcWidth * aspectRatio)) {
            result.set(
                    0 , 0,
                    srcHeight / aspectRatio,
                    srcHeight
            );

        } else {
            result.set(
                    0 , 0,
                    srcWidth,
                    srcWidth * aspectRatio
            );
        }
        return result;
    }
}
