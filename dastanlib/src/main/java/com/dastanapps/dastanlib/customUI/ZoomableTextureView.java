package com.dastanapps.dastanlib.customUI;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.TextureView;
import android.view.View;

import com.almeros.android.multitouch.MoveGestureDetector;
import com.videoeditor.kruso.lib.log.Logger;
import com.videoeditor.kruso.lib.utils.VideoUtils;

/**
 * Created by shuhrat on 3/1/16.
 * Edited by Iqbal Ahmed
 */
public class ZoomableTextureView extends TextureView implements View.OnTouchListener {

    private Matrix mMatrix;
    private ScaleGestureDetector mScaleDetector;
    private MoveGestureDetector mMoveDetector;

    private float mScaleFactor = 1.f;
    private float mFocusX = 0.f;
    private float mFocusY = 0.f;
    private int mOrigW;
    private int mOrigH;

    SurfaceTexture surfaceTexture;
    private float aspectRatioW;
    private float aspectRatioH;

    public void setSSurfaceTexture(SurfaceTexture surfaceTexture) {
        this.surfaceTexture = surfaceTexture;
    }

    public ZoomableTextureView(Context context) {
        super(context);
        init(context);
    }

    public ZoomableTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomableTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ZoomableTextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mMatrix = new Matrix();
        // Setup Gesture Detectors
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mMoveDetector = new MoveGestureDetector(context, new MoveListener());
    }

    public float getX() {
        if (aspectRatioW == 0) aspectRatioW = (float) getWidth() / (float) mOrigW;
        Logger.onlyDebug("focusX=" + mFocusX + " ratioW=" + aspectRatioW);

        float x = (((getWidth() * mScaleFactor) / 2 - mFocusX) / aspectRatioW) / mScaleFactor;
        Logger.onlyDebug("calX=" + x);

        return x;
    }

    public float getY() {
        if (aspectRatioH == 0) aspectRatioH = (float) getHeight() / (float) mOrigH;
        Logger.onlyDebug("focusY=" + mFocusY + " ratioH=" + aspectRatioH);

        float y = (((getHeight() * mScaleFactor) / 2 - mFocusY) / aspectRatioH) / mScaleFactor;
        Logger.onlyDebug("calY=" + y);

        return y;
    }

    public int getOrigW() {
        return mOrigW;
    }

    public int getOrigH() {
        return mOrigH;
    }


    public float getScaleFactor() {
        return mScaleFactor;
    }


    public void setDisplayMetrics(int width, int height, int rotation) {
        // Determine the center of the screen to center 'earth'
        mFocusX = getWidth() / 2; // width / 2;
        mFocusY = getHeight() / 2;//height / 2;

        float xy[] = VideoUtils.getWidthNHeight(width, height, rotation);
        mOrigW = (int) xy[0];
        mOrigH = (int) xy[1];
        Logger.onlyDebug(" OrigWidth=" + mOrigW + " OrigHeight=" + mOrigH);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        // reLayout();

        Logger.onlyDebug(" Width=" + getWidth() + " Height=" + getHeight());

        // Setup Gesture Detectors
        mScaleDetector.onTouchEvent(motionEvent);
        mMoveDetector.onTouchEvent(motionEvent);

        // View is scaled and translated by matrix, so scale and translate initially
        float scaledImageCenterX = (getWidth() * mScaleFactor) / 2;
        float scaledImageCenterY = (getHeight() * mScaleFactor) / 2;

        mMatrix.reset();
        mMatrix.postScale(mScaleFactor, mScaleFactor);

        float dx = mFocusX - scaledImageCenterX;
        float dy = mFocusY - scaledImageCenterY;

        if (dx < ((1 - mScaleFactor) * getWidth())) {
            dx = (1 - mScaleFactor) * getWidth();
            mFocusX = dx + scaledImageCenterX;
            Logger.onlyDebug("Top -X");
        }

        if (dy < ((1 - mScaleFactor) * getHeight())) {
            dy = (1 - mScaleFactor) * getHeight();
            mFocusY = dy + scaledImageCenterY;
            Logger.onlyDebug("Bottom -Y");
        }
        if (dx > 0) {
            dx = 0;
            mFocusX = dx + scaledImageCenterX;
            Logger.onlyDebug("Top X");
        }

        if (dy > 0) {
            dy = 0;
            mFocusY = dy + scaledImageCenterY;
            Logger.onlyDebug("Top Y");
        }

        Logger.onlyDebug("X=" + mFocusX + "::::Y=" + mFocusY + ":::::ScaleFactor=" + mScaleFactor);

        mMatrix.postTranslate(dx, dy);
        setTransform(mMatrix);
        setAlpha(1);

        invalidate();
        getOrigW();
        getOrigH();
        return true; // indicate event was handled
    }

    public void enableTouch() {
        setOnTouchListener(this);
    }

    public void disableTouch() {
        setOnTouchListener(null);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor(); // scale change since previous event
            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(1.f, Math.min(mScaleFactor, 4.0f));
            return true;
        }
    }

    private class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
        @Override
        public boolean onMove(MoveGestureDetector detector) {

            PointF d = detector.getFocusDelta();
            mFocusX += d.x;
            mFocusY += d.y;
            Logger.onlyDebug("dx=" + d.x + "::::dy=" + d.y);
            return true;
        }
    }
}
