package com.dastanapps.dastanlib.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dastanapps.dastanlib.R;

import java.util.concurrent.ExecutionException;

import static com.dastanapps.dastanlib.network.VolleyRequest.getRequestQueue;

/*
 *
 * Created by IQBAL-MEBELKART on 10/23/2015.

 */

public class MkartImage {

    private static final String TAG = MkartImage.class.getSimpleName();
    private static ImageLoader mImageLoader;

    public static void loadImage(Context ctxt, String url, ImageView imv) {
        GlideApp.with(ctxt)
                .load(url)
                //.asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.common_placeholder)
                .into(imv);
    }

    public static void loadImage(Context ctxt, String url, ImageView imv, int defImg) {
        GlideApp.with(ctxt)
                .load(url)
                //.asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(defImg)
                .into(imv);
    }

    public static Bitmap loadBitmap(Context ctxt, String url) {
        try {
            return GlideApp.with(ctxt).asBitmap().load(url).into(-1, -1).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(getRequestQueue(),
                    new LruBitmapCache());
        }
        return mImageLoader;
    }


    public static void loadVolleyImage(String url, ImageView imv) {
        ImageLoader imageLoader = getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(
                imv, R.drawable.common_placeholder, R.drawable.common_placeholder), 600, 600);
    }


    public static void clear(ImageView itemImg) {
        GlideApp.with(itemImg.getContext()).clear(itemImg);
    }

    public static void loadResizeImage(Context ctxt, String img_url, ImageView view) {
        GlideApp.with(ctxt)
                .asBitmap()
                .load(img_url)
                .override(400, 163)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.common_placeholder)
                .into(view);
    }

    public static void loadResizeProdImage(Context ctxt, String img_url, ImageView view) {
        GlideApp.with(ctxt)
                .asBitmap()
                .load(img_url)
                .override(400, 400)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.common_placeholder)
                .into(view);
    }
}

