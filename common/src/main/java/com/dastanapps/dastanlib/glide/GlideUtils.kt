package com.dastanapps.dastanlib.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.dastanapps.dastanlib.DastanLibApp
import java.io.File
import java.util.concurrent.ExecutionException

/**
 * Created by dastaniqbal on 20/10/2017.
 * 20/10/2017 11:09
 */
object GlideUtils {
    fun setThumbnail(path: String, imv: ImageView) {
        var path = path
        if (!path.startsWith("file:///"))
            path = "file:///$path"
        Glide.with(DastanLibApp.INSTANCE).load(path).into(imv)
    }

    fun decodeVideo(context: Context, videoPath: String, duration: Int): GlideRequest<Drawable> {
        var durationMicro = duration * 1000 / 2
        if (durationMicro < 0) {
            durationMicro = 0
        }
        return GlideApp.with(context)
                .load(videoPath)
                .frame(durationMicro.toLong())
    }

    fun setImageViewFilePath(path: String, imageView: ImageView) {
        GlideApp.with(imageView.context)
                .load(File(path))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .fitCenter()
                .into(imageView)
    }

    fun urlWithHeader(url: String, headers: Map<String, String>?): GlideUrl {
        val headersBuilder: LazyHeaders.Builder = LazyHeaders.Builder()
        if (headers != null) {
            for ((key, value) in headers) {
                headersBuilder.addHeader(key, value)
            }
        }
        return GlideUrl(url, headersBuilder.build())
    }

    fun loadImage(ctxt: Context, url: String, imv: ImageView) {
        GlideApp.with(ctxt)
                .load(url)
                //.asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imv)
    }

    fun loadImage(ctxt: Context, url: String, imv: ImageView, defImg: Int) {
        GlideApp.with(ctxt)
                .load(url)
                //.asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(defImg)
                .into(imv)
    }

    fun loadBitmap(ctxt: Context, url: String): Bitmap? {
        try {
            return GlideApp.with(ctxt).asBitmap().load(url).into(-1, -1).get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

        return null
    }

    fun clear(itemImg: ImageView) {
        GlideApp.with(itemImg.context).clear(itemImg)
    }

    fun loadResizeImage(ctxt: Context, img_url: String, view: ImageView) {
        GlideApp.with(ctxt)
                .asBitmap()
                .load(img_url)
                .override(400, 163)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view)
    }

    fun loadResizeProdImage(ctxt: Context, img_url: String, view: ImageView) {
        GlideApp.with(ctxt)
                .asBitmap()
                .load(img_url)
                .override(400, 400)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(view)
    }
}