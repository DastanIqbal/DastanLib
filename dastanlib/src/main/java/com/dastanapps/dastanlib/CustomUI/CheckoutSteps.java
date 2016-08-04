package com.dastanapps.dastanlib.CustomUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mebelkart.app.R;
import com.mebelkart.app.Utils.FontUtils;

public class CheckoutSteps extends LinearLayout {
    public TextView tv;
    public ImageView imv;
    private Context ctxt;


    public CheckoutSteps(Context context) {
        super(context);
    }

    public CheckoutSteps(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CheckoutSteps(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        ctxt = context;
        View view = LayoutInflater.from(context).inflate(R.layout.checkout_custom_view_layout, this);
        tv = (TextView) view.findViewById(R.id.tv_item);
        imv = (ImageView) view.findViewById(R.id.imv_item);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckoutSteps, 0, 0);
        Drawable drawable = typedArray.getDrawable(R.styleable.CheckoutSteps_cs_src);
        String strtxt = typedArray.getString(R.styleable.CheckoutSteps_cs_txt);
        int strsize = typedArray.getDimensionPixelSize(R.styleable.CheckoutSteps_cs_size, 14);

        if (drawable != null)
            imv.setImageDrawable(drawable);

        if (strtxt != null) {
            tv.setText(strtxt);
        }
        if (strsize != 0) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,strsize);
        }
        if(!isInEditMode()) {
            FontUtils.setRobotoRegular(tv);
        }
    }

    public void setImageDrawable(Drawable imgDrawable) {
        imv.setImageDrawable(imgDrawable);
    }

    public void setImageResource(int imgRes) {
        imv.setImageResource(imgRes);
    }

    public void setImageText(String mnuText) {
        tv.setText(mnuText);
    }

    public void setTextColor(int id) {
        tv.setTextColor(id);
    }

    public void setTextSize(float txtSize){
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,txtSize);
    }

}
