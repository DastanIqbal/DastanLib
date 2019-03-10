package com.dastanapps.dastanlib.customUI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * An ImageView that draws its contents as a circle.
 * <p>
 * Normally you should probably make a custom Drawable, but this is just for explanatory purposes.
 */
public class CircleView extends View {

    private Paint mPaint;
    private Paint mStrokePaint;

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(2);
        mStrokePaint.setColor(Color.BLACK);
        mStrokePaint.setStrokeCap(Paint.Cap.BUTT);

    }

    public void setStrokeWidth(float width) {
        mStrokePaint.setStrokeWidth(width);
    }

    public void setStrokeColor(String color) {
        mStrokePaint.setColor(Color.parseColor(color));
    }

    public void setColor(String color) {
        mPaint.setColor(Color.parseColor(color));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        canvas.drawCircle(width / 2, height / 2, (width - 10) / 2, mPaint);
        canvas.drawCircle(width / 2, height / 2, (width - 10) / 2, mStrokePaint);
    }
}
