package com.dastanapps.dastanlib.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Matrix
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.view.TextureView

import com.dastanapps.dastanlib.log.Logger

import java.io.File

/**
 * Created by dastaniqbal on 27/04/2017.
 * 27/04/2017 11:32
 */

object VideoUtils {
    fun adjustAspectRatio(vidTxv: TextureView, videoWidth: Int, videoHeight: Int) {
        val viewWidth = vidTxv.width
        val viewHeight = vidTxv.height
        val aspectRatio = videoHeight.toDouble() / videoWidth

        val newWidth: Int
        val newHeight: Int
        if (viewHeight > (viewWidth * aspectRatio).toInt()) {
            // limited by narrow width; restrict height
            newWidth = viewWidth
            newHeight = (viewWidth * aspectRatio).toInt()
        } else {
            // limited by short height; restrict width
            newWidth = (viewHeight / aspectRatio).toInt()
            newHeight = viewHeight
        }
        val xoff = (viewWidth - newWidth) / 2
        val yoff = (viewHeight - newHeight) / 2
        Logger.onlyDebug("video=" + videoWidth + "x" + videoHeight +
                " view=" + viewWidth + "x" + viewHeight +
                " newView=" + newWidth + "x" + newHeight +
                " off=" + xoff + "," + yoff)

        val txform = Matrix()
        vidTxv.getTransform(txform)
        txform.setScale(newWidth.toFloat() / viewWidth, newHeight.toFloat() / viewHeight)
        txform.postTranslate(xoff.toFloat(), yoff.toFloat())
        vidTxv.setTransform(txform)
    }

    fun getWidthNHeight(vidTxv: TextureView, videoWidth: Int, videoHeight: Int): FloatArray {
        val viewWidth = vidTxv.width
        val viewHeight = vidTxv.height
        val aspectRatio = videoHeight.toDouble() / videoWidth

        val newWidth: Int
        val newHeight: Int
        if (viewHeight > (viewWidth * aspectRatio).toInt()) {
            // limited by narrow width; restrict height
            newWidth = viewWidth
            newHeight = (viewWidth * aspectRatio).toInt()
        } else {
            // limited by short height; restrict width
            newWidth = (viewHeight / aspectRatio).toInt()
            newHeight = viewHeight
        }
        val xoff = (viewWidth - newWidth) / 2
        val yoff = (viewHeight - newHeight) / 2
        Logger.onlyDebug("video=" + videoWidth + "x" + videoHeight +
                " view=" + viewWidth + "x" + viewHeight +
                " newView=" + newWidth + "x" + newHeight +
                " off=" + xoff + "," + yoff)
        val xy = FloatArray(2)
        xy[0] = newWidth.toFloat()
        xy[1] = newHeight.toFloat()
        return xy
    }

    fun getVideoDimension(path: String): IntArray {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        val dimen = IntArray(2)
        dimen[0] = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH))
        dimen[1] = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT))
        return dimen
    }

    fun getWidthNHeight(videoWidth: Int, videoHeight: Int, rotation: Int): FloatArray {
        val xy = FloatArray(2)

        if (videoWidth <= 1280 || videoHeight <= 720) {
            if (rotation == 0 || rotation == 270 || rotation == -90) {
                xy[0] = 640f
                xy[1] = 360f
            } else {
                xy[0] = 360f
                xy[1] = 640f
            }
        } else {
            if (rotation == 0 || rotation == 270 || rotation == -90) {
                xy[0] = 1280f
                xy[1] = 720f
            } else {
                xy[0] = 720f
                xy[1] = 1280f
            }
        }

        return xy
    }

    fun addVideo(context: Context, videoFile: File): Uri? {
        val values = ContentValues(3)
        values.put(MediaStore.Video.Media.TITLE, videoFile.name)
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
        values.put(MediaStore.Video.Media.DATA, videoFile.absolutePath)
        return context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
    }
}
