package com.dastanapps.dastanlib.chat.beans;

/**
 * Created by Dastan Iqbal on 12/21/2016.
 * author Iqbal Ahmed
 * emailId: ask2iqbal@gmail.com
 */

public class MediaMsg {
    private String mediaType;
    private String mediaPath;

    public MediaMsg(String mediaPath, String mediaType) {
        this.mediaPath = mediaPath;
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }
}
