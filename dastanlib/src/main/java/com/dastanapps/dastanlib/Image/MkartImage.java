package com.dastanapps.dastanlib.Image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mebelkart.app.MkartApp;
import com.mebelkart.app.R;

import java.util.concurrent.ExecutionException;

/**
 * Created by IQBAL-MEBELKART on 10/23/2015.
 */
public class MkartImage {

    public static void loadImage(Context ctxt, String url, ImageView imv) {
        Glide.with(ctxt)
                .load(url)
                        //.asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.common_placeholder)
                .into(imv);
    }

    public static void loadImage(Context ctxt, String url, ImageView imv, int defImg) {
        Glide.with(ctxt)
                .load(url)
                        //.asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(defImg)
                .into(imv);
    }

    public static Bitmap loadBitmap(Context ctxt, String url) {
        try {
            return Glide.with(ctxt).load(url).asBitmap().into(-1, -1).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void loadVolleyImage(String url, ImageView imv) {
        ImageLoader imageLoader = MkartApp.getInstance().getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(
                imv, R.drawable.common_placeholder, R.drawable.common_placeholder),600,600);
    }


    public static void clear(ImageView itemImg) {
        Glide.clear(itemImg);
    }

    public static void loadResizeImage(Context ctxt, String img_url, ImageView view) {
        Glide.with(ctxt)
                .load(img_url)
                .asBitmap()
                .override(400,163)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.common_placeholder)
                .into(view);
    }

    public static void loadResizeProdImage(Context ctxt, String img_url, ImageView view) {
        Glide.with(ctxt)
                .load(img_url)
                .asBitmap()
                .override(400,400)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.common_placeholder)
                .into(view);
    }
}

