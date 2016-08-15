package com.dastanapps.dastanlib.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by MEBELKART on 11-12-2015.
 */
public class GridViewScrollOff extends GridView {
    boolean expanded=false;

    public GridViewScrollOff(Context context) {
        super(context);
    }

    public GridViewScrollOff(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewScrollOff(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GridViewScrollOff(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public boolean isExpanded()
    {
        return expanded;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if(!isExpanded()){
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        }
        else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}
