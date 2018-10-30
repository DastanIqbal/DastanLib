package com.dastanapps.dastanlib.customUI

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

internal class ProgressDrawable(private val mOffset: Int) : Drawable() {
    private val mPaint: Paint

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = -0x111112
        mPaint.textAlign = Paint.Align.CENTER
    }

    override fun draw(canvas: Canvas) {
        canvas.drawColor(-0xffff56)
        val b = bounds
        val left = b.left + mOffset
        val right = b.right - mOffset
        for (i in 0..10) {
            val x = left + i * (right - left) / 10f
            canvas.drawText(Integer.toString(i * 10), x, b.bottom - mPaint.descent(), mPaint)
        }
    }

    override fun setAlpha(alpha: Int) {}

    override fun setColorFilter(cf: ColorFilter?) {}

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun getIntrinsicHeight(): Int {
        return (3 * mPaint.textSize).toInt()
    }
}