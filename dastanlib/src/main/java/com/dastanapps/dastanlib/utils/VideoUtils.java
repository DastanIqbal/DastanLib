package com.dastanapps.dastanlib.utils;

import android.content.ContentValues;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.TextureView;

import com.dastanapps.dastanlib.DastanApp;
import com.dastanapps.dastanlib.log.Logger;

import java.io.File;

/**
 * Created by dastaniqbal on 27/04/2017.
 * 27/04/2017 11:32
 */

public class VideoUtils {
    public static void adjustAspectRatio(TextureView vidTxv, int videoWidth, int videoHeight) {
        int viewWidth = vidTxv.getWidth();
        int viewHeight = vidTxv.getHeight();
        double aspectRatio = (double) videoHeight / videoWidth;

        int newWidth, newHeight;
        if (viewHeight > (int) (viewWidth * aspectRatio)) {
            // limited by narrow width; restrict height
            newWidth = viewWidth;
            newHeight = (int) (viewWidth * aspectRatio);
        } else {
            // limited by short height; restrict width
            newWidth = (int) (viewHeight / aspectRatio);
            newHeight = viewHeight;
        }
        int xoff = (viewWidth - newWidth) / 2;
        int yoff = (viewHeight - newHeight) / 2;
        Logger.onlyDebug("video=" + videoWidth + "x" + videoHeight +
                " view=" + viewWidth + "x" + viewHeight +
                " newView=" + newWidth + "x" + newHeight +
                " off=" + xoff + "," + yoff);

        Matrix txform = new Matrix();
        vidTxv.getTransform(txform);
        txform.setScale((float) newWidth / viewWidth, (float) newHeight / viewHeight);
        txform.postTranslate(xoff, yoff);
        vidTxv.setTransform(txform);
    }

    public static float[] getWidthNHeight(TextureView vidTxv, int videoWidth, int videoHeight) {
        int viewWidth = vidTxv.getWidth();
        int viewHeight = vidTxv.getHeight();
        double aspectRatio = (double) videoHeight / videoWidth;

        int newWidth, newHeight;
        if (viewHeight > (int) (viewWidth * aspectRatio)) {
            // limited by narrow width; restrict height
            newWidth = viewWidth;
            newHeight = (int) (viewWidth * aspectRatio);
        } else {
            // limited by short height; restrict width
            newWidth = (int) (viewHeight / aspectRatio);
            newHeight = viewHeight;
        }
        int xoff = (viewWidth - newWidth) / 2;
        int yoff = (viewHeight - newHeight) / 2;
        Logger.onlyDebug("video=" + videoWidth + "x" + videoHeight +
                " view=" + viewWidth + "x" + viewHeight +
                " newView=" + newWidth + "x" + newHeight +
                " off=" + xoff + "," + yoff);
        float xy[] = new float[2];
        xy[0] = newWidth;
        xy[1] = newHeight;
        return xy;
    }

    public static int[] getVideoDimension(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        int[] dimen = new int[2];
        dimen[0] = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        dimen[1] = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        return dimen;
    }

    public static float[] getWidthNHeight(int videoWidth, int videoHeight, int rotation) {
        float xy[] = new float[2];

        if (videoWidth <= 1280 || videoHeight <= 720) {
            if (rotation == 0 || rotation == 270 || rotation == -90) {
                xy[0] = 640;
                xy[1] = 360;
            } else {
                xy[0] = 360;
                xy[1] = 640;
            }
        } else {
            if (rotation == 0 || rotation == 270 || rotation == -90) {
                xy[0] = 1280;
                xy[1] = 720;
            } else {
                xy[0] = 720;
                xy[1] = 1280;
            }
        }

        return xy;
    }

    public static Uri addVideo(File videoFile) {
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Video.Media.TITLE, videoFile.getName());
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, videoFile.getAbsolutePath());
        return DastanApp.getInstance().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }
}
