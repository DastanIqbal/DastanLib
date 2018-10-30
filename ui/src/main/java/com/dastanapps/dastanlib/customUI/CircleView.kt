package com.dastanapps.dastanlib.customUI

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * An ImageView that draws its contents as a circle.
 *
 *
 * Normally you should probably make a custom Drawable, but this is just for explanatory purposes.
 */
class CircleView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val mPaint: Paint
    private val mStrokePaint: Paint

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mStrokePaint.isAntiAlias = true
        mStrokePaint.style = Paint.Style.STROKE
        mStrokePaint.strokeWidth = 2f
        mStrokePaint.color = Color.BLACK
        mStrokePaint.strokeCap = Paint.Cap.BUTT

    }

    fun setStrokeWidth(width: Float) {
        mStrokePaint.strokeWidth = width
    }

    fun setStrokeColor(color: String) {
        mStrokePaint.color = Color.parseColor(color)
    }

    fun setColor(color: String) {
        mPaint.color = Color.parseColor(color)
    }


    override fun onDraw(canvas: Canvas) {
        val width = width
        val height = height

        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), ((width - 10) / 2).toFloat(), mPaint)
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), ((width - 10) / 2).toFloat(), mStrokePaint)
    }
}
