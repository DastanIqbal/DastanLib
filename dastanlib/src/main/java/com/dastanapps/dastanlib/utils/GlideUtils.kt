package com.dastanapps.dastanlib.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.dastanapps.dastanlib.DastanApp
import com.dastanapps.dastanlib.image.GlideApp
import com.dastanapps.dastanlib.image.GlideRequest
import java.io.File

/**
 * Created by dastaniqbal on 20/10/2017.
 * 20/10/2017 11:09
 */
object GlideUtils {
    fun setThumbnail(path: String, imv: ImageView) {
        var path = path
        if (!path.startsWith("file:///"))
            path = "file:///$path"
        Glide.with(DastanApp.getInstance()).load(path).into(imv)
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
}