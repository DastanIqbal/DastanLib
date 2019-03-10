package com.dastanapps.dastanlib;

import android.app.Application;
import android.content.Intent;

import com.dastanapps.dastanlib.ads.MarvelAds;
import com.dastanapps.dastanlib.utils.SPUtils;

/**
 * Created by IQBAL-MEBELKART on 8/16/2016.
 */

public class DastanApp extends Application {
    public static final String GCM_MESSAGE = "gcm_message";
    private static DastanApp instance;
    private String devMode;
    private int smallIcon;
    private Intent notficationIntent;
    private int notificationColor;
    private String fbBannerAdId, fbNativeAdId, fbInterstialAdId;
    private MarvelAds marvelAds;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static DastanApp getInstance() {
        return instance;
    }

    public static String getChatUser() {
        return SPUtils.readString("chat_user", "");
    }

    public static void setChatUser(String chatUser) {
        SPUtils.writeString("chat_user", chatUser);
    }

    public static String getChatPwd() {
        return SPUtils.readString("chat_pwd", "");
    }

    public static void setChatPwd(String chatPwd) {
        SPUtils.writeString("chat_pwd", chatPwd);
    }

    public boolean isRelease() {
        return devMode != null && devMode.equals("release");
    }

    public String getDevMode() {
        return devMode;
    }

    public void setDevMode(String devMode) {
        this.devMode = devMode;
    }

    public int getSmallIcon() {
        return smallIcon;
    }

    public Intent getNotficationIntent() {
        return notficationIntent;
    }

    public int getNotificationColor() {
        return notificationColor;
    }

    public void setNotification(int smallIcon, Intent intent, int notifColor) {
        this.smallIcon = smallIcon;
        this.notficationIntent = intent;
        this.notificationColor = notifColor;
    }

    public void setFBBannerAdId(String fbBannerAdId) {
        this.fbBannerAdId = fbBannerAdId;
    }

    public String getFbBannerAdId() {
        return fbBannerAdId;
    }

    public void setFBNativeAdId(String fbNativeAdId) {
        this.fbNativeAdId = fbNativeAdId;
    }

    public String getFBNativeAdId() {
        return fbNativeAdId;
    }

    public void setFBInterstialAdId(String fbInterstialAdId) {
        this.fbInterstialAdId = fbInterstialAdId;
    }

    public String getFBInterstialAdId() {
        return fbInterstialAdId;
    }

    public MarvelAds getMarveAds() {
        if (marvelAds == null)
            marvelAds = new MarvelAds();
        return marvelAds;
    }

    public void setPushURL(String pushURL) {
        SPUtils.writeString(SPConstant.PUSH_URL_CONSTANT, pushURL);
    }

}
