package com.dastanapps.dastanlib.utils

import android.graphics.Matrix
import android.graphics.RectF

import java.math.BigDecimal

/**
 * Created by dastaniqbal on 09/05/2017.
 * 09/05/2017 11:54
 */

object MathUtils {
    enum class ScaleType {
        CENTER_CROP,
        CENTER_INSIDE
    }

    fun round(d: Float, decimalPlace: Int): BigDecimal {
        var bd = BigDecimal(java.lang.Float.toString(d))
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP)
        return bd
    }

    fun truncateDigitsFromLast(value: Float, truncateDigit: Float): Float {
        if (truncateDigit == 0f) return 0f
        if (value.toString().length >= truncateDigit) {
            var digit = 10f
            var i = 1
            while (i < truncateDigit) {
                digit *= 10f
                i++
            }
            return (value - value % digit) / digit
        }
        return 0f
    }

    fun getScaleToFitMatrix(srcWidth: Int, srcHeight: Int,
                            targetWidth: Int, targetHeight: Int,
                            scaleToFit: Matrix.ScaleToFit): Matrix {
        return getScaleToFitMatrix(
                0, 0, srcWidth, srcHeight,
                0, 0, targetWidth, targetHeight,
                scaleToFit
        )
    }

    fun getScaleToFitMatrix(srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int,
                            targetX: Int, targetY: Int, targetWidth: Int, targetHeight: Int,
                            scaleToFit: Matrix.ScaleToFit): Matrix {
        val srcRect = RectF(srcX.toFloat(), srcY.toFloat(), (srcWidth + srcX).toFloat(), (srcHeight + srcY).toFloat())
        val targetRect = RectF(targetX.toFloat(), targetY.toFloat(), (targetWidth + targetX).toFloat(), (targetHeight + targetY).toFloat())
        val resultMatrix = Matrix()
        resultMatrix.setRectToRect(srcRect, targetRect, scaleToFit)
        return resultMatrix
    }

    fun getScaleMatrix(srcWidth: Float, srcHeight: Float, aspectRatio: Float, scaleType: ScaleType?): Matrix {
        var scaleType = scaleType
        if (scaleType == null) {
            scaleType = ScaleType.CENTER_INSIDE
        }
        val resultMat = Matrix()
        val newRectF = RectF()
        when (scaleType) {
            MathUtils.ScaleType.CENTER_INSIDE -> newRectF.set(getCenterInsideSize(srcWidth, srcHeight, aspectRatio, scaleType))
            MathUtils.ScaleType.CENTER_CROP -> newRectF.set(getCenterCropSize(srcWidth, srcHeight, aspectRatio, scaleType))
        }

        val xoff = (srcWidth - newRectF.width()) / 2
        val yoff = (srcHeight - newRectF.height()) / 2

        resultMat.setScale(newRectF.width() / srcWidth, newRectF.height() / srcHeight)
        resultMat.postTranslate(xoff, yoff)
        return resultMat
    }

    internal fun getCenterInsideSize(srcWidth: Float, srcHeight: Float, aspectRatio: Float, scaleType: ScaleType?): RectF {
        val result = RectF()
        if (srcHeight > (srcWidth * aspectRatio).toInt()) {
            // limited by narrow width; restrict height
            result.set(
                    0f, 0f,
                    srcWidth,
                    srcWidth * aspectRatio
            )
        } else {
            // limited by short height; restrict width
            result.set(
                    0f, 0f,
                    srcHeight / aspectRatio,
                    srcHeight
            )
        }
        return result
    }

    internal fun getCenterCropSize(srcWidth: Float, srcHeight: Float, aspectRatio: Float, scaleType: ScaleType): RectF {
        val result = RectF()
        if (srcHeight > (srcWidth * aspectRatio).toInt()) {
            result.set(
                    0f, 0f,
                    srcHeight / aspectRatio,
                    srcHeight
            )

        } else {
            result.set(
                    0f, 0f,
                    srcWidth,
                    srcWidth * aspectRatio
            )
        }
        return result
    }
}
