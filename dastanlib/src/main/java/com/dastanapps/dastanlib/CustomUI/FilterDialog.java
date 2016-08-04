package com.dastanapps.dastanlib.CustomUI;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mebelkart.app.MkartApp;

public class FilterDialog {
    public static View getLayoutView(int id) {
        LayoutInflater inflater = LayoutInflater.from(MkartApp.getInstance());
        return inflater.inflate(id, null);
    }

    public static Dialog getMKDialog(Context context, int layoutid) {
        Display display;
        int DisplayWidth, DisplayHeight, DialogWidth, DialogHeight;
        display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayWidth = display.getWidth();
        DisplayHeight = display.getHeight();
        if (DisplayHeight > DisplayWidth) {
            DialogWidth = (6 * DisplayWidth) / 7;
            DialogHeight = (4 * DisplayHeight) / 5;
        } else {
            DialogWidth = (6 * DisplayWidth) / 9;
            DialogHeight = (4 * DisplayHeight) / 5;
        }
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final Dialog d = new Dialog(MkartApp.getInstance(), 0);
        //d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getLayoutView(layoutid);
        d.setContentView(view, llp);
        d.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        return d;
    }
}
