package com.dastanapps.dastanlib;

import android.content.Context;

import com.dastanapps.dastanlib.utils.SPUtils;

/**
 * Created by IQBAL-MEBELKART on 8/16/2016.
 */

public class DastanApp {

    public static final String GCM_MESSAGE = "gcm_message";
    private static Context mContext;

    public static Context getInstance() {
        return mContext;
    }

    public static void setInstance(Context context) {
        mContext = context;
    }

    public static String getChatUser() {
        return SPUtils.readString("chat_user","");
    }
    public static void setChatUser(String chatUser) {
        SPUtils.writeString("chat_user",chatUser);
    }

    public static String getChatPwd() {
        return SPUtils.readString("chat_pwd","");
    }
    public static void setChatPwd(String chatPwd) {
        SPUtils.writeString("chat_pwd",chatPwd);
    }
}
