package com.dastanapps.dastanlib.customUI

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Point
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

import com.dastanapps.dastanlib.log.Logger


/**
 * Created by DastanIqbal on 23/5/2017.
 */

class HorizontalSlideColorPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {

    /**
     * The width in dp of the hue panel.
     */
    private var HUE_PANEL_WIDTH = 0f
    /**
     * The distance in dp between the different
     * color panels.
     */
    private var PANEL_SPACING = 10f
    /**
     * The radius in dp of the color palette tracker circle.
     */
    private var PALETTE_CIRCLE_TRACKER_RADIUS = 5f
    /**
     * The dp which the tracker of the hue or alpha panel
     * will extend outside of its bounds.
     */
    private var RECTANGLE_TRACKER_OFFSET = 2f


    private var mDensity = 1f

    private var mListener: OnColorChangedListener? = null

    private var mHuePaint: Paint? = null
    private var mHueTrackerPaint: Paint? = null

    private var mBorderPaint: Paint? = null

    private var mHueShader: Shader? = null

    private var mAlpha = 0xff
    private var mHue = 360f
    private var mSat = 1f
    private var mVal = 1f

    var sliderTrackerColor = -0xe3e3e4
        set(color) {
            field = color

            mHueTrackerPaint!!.color = sliderTrackerColor

            invalidate()
        }
    /**
     * Get the color of the border surrounding all panels.
     */
    /**
     * Set the color of the border surrounding all panels.
     *
     * @param color
     */
    var borderColor = -0x919192
        set(color) {
            field = color
            invalidate()
        }

    /*
     * To remember which panel that has the "focus" when
     * processing hardware button data.
     */
    private var mLastTouchedPanel = PANEL_SAT_VAL

    /**
     * Offset from the edge we must have or else
     * the finger tracker will get clipped when
     * it is drawn outside of the view.
     */
    /**
     * Get the drawing offset of the color picker view.
     * The drawing offset is the distance from the side of
     * a panel to the side of the view minus the padding.
     * Useful if you want to have your own panel below showing
     * the currently selected color and want to align it perfectly.
     *
     * @return The offset in pixels.
     */
    var drawingOffset: Float = 0.toFloat()
        private set


    /*
     * Distance form the edges of the view
     * of where we are allowed to draw.
     */
    private var mDrawingRect: RectF? = null

    private var mHueRect: RectF? = null

    private var mStartTouchPoint: Point? = null

    /**
     * Get the current color this view is showing.
     *
     * @return the current color.
     */
    /**
     * Set the color the view should show.
     *
     * @param color The color that should be selected.
     */
    var color: Int
        get() = Color.HSVToColor(mAlpha, floatArrayOf(mHue, mSat, mVal))
        set(color) = setColor(color, false)

    interface OnColorChangedListener {
        fun onColorChanged(color: String)
    }

    init {
        init()
    }

    private fun init() {
        mDensity = context.resources.displayMetrics.density
        PALETTE_CIRCLE_TRACKER_RADIUS *= mDensity
        RECTANGLE_TRACKER_OFFSET *= mDensity
        HUE_PANEL_WIDTH *= mDensity
        PANEL_SPACING = PANEL_SPACING * mDensity

        drawingOffset = calculateRequiredOffset()

        initPaintTools()

        //Needed for receiving trackball motion events.
        isFocusable = true
        isFocusableInTouchMode = true
    }

    private fun initPaintTools() {

        mHuePaint = Paint()
        mHueTrackerPaint = Paint()
        mBorderPaint = Paint()

        mHueTrackerPaint!!.color = sliderTrackerColor
        mHueTrackerPaint!!.style = Paint.Style.STROKE
        mHueTrackerPaint!!.strokeWidth = 2f * mDensity
        mHueTrackerPaint!!.isAntiAlias = true
    }

    private fun calculateRequiredOffset(): Float {
        var offset = Math.max(PALETTE_CIRCLE_TRACKER_RADIUS, RECTANGLE_TRACKER_OFFSET)
        offset = Math.max(offset, BORDER_WIDTH_PX * mDensity)

        return offset * 1.5f
    }

    private fun buildHueColorArray(): IntArray {

        val hue = IntArray(361)

        var count = 0
        var i = hue.size - 1
        while (i >= 0) {
            hue[count] = Color.HSVToColor(floatArrayOf(i.toFloat(), 1f, 1f))
            i--
            count++
        }

        return hue
    }


    override fun onDraw(canvas: Canvas) {

        if (mDrawingRect!!.width() <= 0 || mDrawingRect!!.height() <= 0) return

        drawHuePanel(canvas)

    }

    private fun drawHuePanel(canvas: Canvas) {

        val rect = mHueRect


        mBorderPaint!!.color = borderColor
        canvas.drawRect(rect!!.left - BORDER_WIDTH_PX,
                rect.top - BORDER_WIDTH_PX,
                rect.right + BORDER_WIDTH_PX,
                rect.bottom + BORDER_WIDTH_PX,
                mBorderPaint!!)

        if (mHueShader == null) {
            mHueShader = LinearGradient(rect.left, rect.bottom, rect.right, rect.bottom, buildHueColorArray(), null, Shader.TileMode.CLAMP)
            mHuePaint!!.shader = mHueShader
        }

        canvas.drawRect(rect, mHuePaint!!)

        val rectHeight = 4 * mDensity / 2

        //        Point p = hueToPoint(mHue);
        //
        //        RectF r = new RectF();
        //        r.left = p.y - rectHeight;
        //        r.right = p.y + rectHeight;
        //        r.top = rect.left - RECTANGLE_TRACKER_OFFSET;
        //        r.bottom = rect.right + RECTANGLE_TRACKER_OFFSET;
        //
        //
        //        canvas.drawRoundRect(r, 2, 2, mHueTrackerPaint);

    }


    private fun hueToPoint(hue: Float): Point {
        val rect = mHueRect
        val width = rect!!.width()

        val p = Point()

        p.y = (width - hue * width / 360f + rect.top).toInt()
        p.x = rect.left.toInt()

        return p
    }


    private fun pointToHue(x: Float): Float {
        var x = x
        val rect = mHueRect
        val width = rect!!.width()

        if (x < rect.left) {
            x = 0f
        } else if (x > rect.right) {
            x = width
        } else {
            x = x - rect.left
        }

        return 360f - x * 360f / width
    }

    override fun onTrackballEvent(event: MotionEvent): Boolean {

        val x = event.x
        val y = event.y

        var update = false


        if (event.action == MotionEvent.ACTION_MOVE) {

            when (mLastTouchedPanel) {

                PANEL_SAT_VAL -> {

                    var sat: Float
                    var `val`: Float

                    sat = mSat + x / 50f
                    `val` = mVal - y / 50f

                    if (sat < 0f) {
                        sat = 0f
                    } else if (sat > 1f) {
                        sat = 1f
                    }

                    if (`val` < 0f) {
                        `val` = 0f
                    } else if (`val` > 1f) {
                        `val` = 1f
                    }

                    mSat = sat
                    mVal = `val`

                    update = true
                }

                PANEL_HUE -> {

                    var hue = mHue - y * 10f

                    if (hue < 0f) {
                        hue = 0f
                    } else if (hue > 360f) {
                        hue = 360f
                    }

                    mHue = hue

                    update = true
                }
            }


        }


        if (update) {

            if (mListener != null) {
                mListener!!.onColorChanged(convertToARGB(Color.HSVToColor(mAlpha, floatArrayOf(mHue, mSat, mVal))))
            }

            invalidate()
            return true
        }


        return super.onTrackballEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        var update = false

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {

                mStartTouchPoint = Point(event.x.toInt(), event.y.toInt())

                update = moveTrackersIfNeeded(event)
            }

            MotionEvent.ACTION_MOVE ->

                update = moveTrackersIfNeeded(event)

            MotionEvent.ACTION_UP -> {

                mStartTouchPoint = null

                update = moveTrackersIfNeeded(event)
            }
        }

        if (update) {

            if (mListener != null) {
                mListener!!.onColorChanged(convertToARGB(Color.HSVToColor(mAlpha, floatArrayOf(mHue, mSat, mVal))))
            }

            invalidate()
            return true
        }

        Logger.onlyDebug("Touched: " + event.action)

        return true
    }

    private fun moveTrackersIfNeeded(event: MotionEvent): Boolean {

        if (mStartTouchPoint == null) return false

        var update = false

        val startX = mStartTouchPoint!!.x
        val startY = mStartTouchPoint!!.y


        if (mHueRect!!.contains(startX.toFloat(), startY.toFloat())) {
            mLastTouchedPanel = PANEL_HUE

            mHue = pointToHue(event.x)

            update = true
        }


        return update
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val parentWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        this.setMeasuredDimension(parentWidth, parentHeight)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        HUE_PANEL_WIDTH = parentWidth.toFloat()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mDrawingRect = RectF()
        mDrawingRect!!.left = drawingOffset + paddingLeft
        mDrawingRect!!.right = w.toFloat() - drawingOffset - paddingRight.toFloat()
        mDrawingRect!!.top = drawingOffset + paddingTop
        mDrawingRect!!.bottom = (h - paddingBottom).toFloat()

        setUpHueRect()
    }

    private fun setUpHueRect() {
        val dRect = mDrawingRect

        val left = dRect!!.left + BORDER_WIDTH_PX
        val top = dRect.top + BORDER_WIDTH_PX
        val bottom = dRect.bottom - BORDER_WIDTH_PX
        val right = dRect.right - BORDER_WIDTH_PX

        mHueRect = RectF(left, top, right, bottom)
    }


    /**
     * Set a OnColorChangedListener to get notified when the color
     * selected by the user has changed.
     *
     * @param listener
     */
    fun setOnColorChangedListener(listener: OnColorChangedListener) {
        mListener = listener
    }

    /**
     * Set the color this view should show.
     *
     * @param color    The color that should be selected.
     * @param callback If you want to get a callback to
     * your OnColorChangedListener.
     */
    fun setColor(color: Int, callback: Boolean) {

        val alpha = Color.alpha(color)

        val hsv = FloatArray(3)

        Color.colorToHSV(color, hsv)

        mAlpha = alpha
        mHue = hsv[0]
        mSat = hsv[1]
        mVal = hsv[2]

        if (callback && mListener != null) {
            mListener!!.onColorChanged(convertToARGB(Color.HSVToColor(mAlpha, floatArrayOf(mHue, mSat, mVal))))
        }

        invalidate()
    }

    companion object {
        private val PANEL_SAT_VAL = 0
        private val PANEL_HUE = 1
        private val PANEL_ALPHA = 2

        /**
         * The width in pixels of the border
         * surrounding all color panels.
         */
        private val BORDER_WIDTH_PX = 1f

        private fun convertToARGB(color: Int): String {
            var alpha = Integer.toHexString(Color.alpha(color))
            var red = Integer.toHexString(Color.red(color))
            var green = Integer.toHexString(Color.green(color))
            var blue = Integer.toHexString(Color.blue(color))

            if (alpha.length == 1) {
                alpha = "0$alpha"
            }

            if (red.length == 1) {
                red = "0$red"
            }

            if (green.length == 1) {
                green = "0$green"
            }

            if (blue.length == 1) {
                blue = "0$blue"
            }

            return "#$alpha$red$green$blue"
        }
    }
}