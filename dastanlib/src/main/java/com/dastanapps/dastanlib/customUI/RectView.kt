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
class RectView : View {

    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mStrokePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var strokeWidth = 20f

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        mPaint.style = Paint.Style.FILL
//        mPaint.color = Color.WHITE
        mStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        //mStrokePaint.isAntiAlias = true
        mStrokePaint.style = Paint.Style.STROKE
//        mStrokePaint.strokeWidth = strokeWidth
//        mStrokePaint.color = Color.WHITE
        mStrokePaint.strokeCap = Paint.Cap.ROUND
    }

    fun setStrokeColor(color: String) {
        mStrokePaint.color = Color.parseColor(color)
    }

    fun setColor(color: String) {
        mPaint.color = Color.parseColor(color)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width
        val height = height

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), mPaint)
        if (strokeWidth != 0f) {
            mStrokePaint.strokeWidth = strokeWidth
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), mStrokePaint)
        }
    }
}
