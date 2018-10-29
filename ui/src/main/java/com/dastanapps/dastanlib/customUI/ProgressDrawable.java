package com.dastanapps.dastanlib.customUI;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

class ProgressDrawable extends Drawable {
    private Paint mPaint;
    private int mOffset;

    public ProgressDrawable(int offset) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xffeeeeee);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mOffset = offset;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(0xff0000aa);
        Rect b = getBounds();
        int left = b.left + mOffset;
        int right = b.right - mOffset;
        for (int i = 0; i <= 10; i++) {
            float x = left + i * (right - left) / 10f;
            canvas.drawText(Integer.toString(i * 10), x, b.bottom - mPaint.descent(), mPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (3 * mPaint.getTextSize());
    }
}