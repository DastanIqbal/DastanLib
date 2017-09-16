package com.videoeditor.kruso.lib.firebase

import android.text.TextUtils
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.videoeditor.kruso.lib.BuildConfig
import com.videoeditor.kruso.lib.MarvelApp
import com.videoeditor.kruso.lib.analytics.MarvelAnalytics
import com.videoeditor.kruso.lib.log.Logger
import com.videoeditor.kruso.lib.services.MarvelService
import com.videoeditor.kruso.lib.utils.SPUtils

/**
 * Created by dastaniqbal on 17/08/2017.
 * dastanIqbal@marvelmedia.com
 * 17/08/2017 10:41
 */
class RemoteConfigFB {
    companion object {
        val TAG: String = "RemoteConfigFB"
        fun setPushUrlRemoteConfig(key: String) {
            val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
            val configSettingsBuilder = FirebaseRemoteConfigSettings.Builder()
            if (BuildConfig.DEBUG) {
                configSettingsBuilder.setDeveloperModeEnabled(BuildConfig.DEBUG)
            }
            configSettingsBuilder.build()
            mFirebaseRemoteConfig.setConfigSettings(configSettingsBuilder.build())
            //mFirebaseRemoteConfig.setDefaults(R.xml.default_config)
            mFirebaseRemoteConfig.fetch(0).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Logger.d(TAG, "Fetch Succeeded")
                    mFirebaseRemoteConfig.activateFetched()
                    val url = mFirebaseRemoteConfig.getString(key)
                    if (!TextUtils.isEmpty(url)) {
                        MarvelApp.getAppInstance().setPushURL(url)
                        MarvelService.getInstance().sendtoken()
                    }
                    Logger.d(TAG, url)
                    MarvelAnalytics.getInstance().sendLogs("config", "success")
                } else {
                    Logger.d(TAG, "Fetch Failed")
                    MarvelAnalytics.getInstance().sendLogs("config", "failed")
                }
            }.addOnFailureListener {
                Logger.d(TAG, "Fetch failure")
                MarvelAnalytics.getInstance().sendLogs("config", "failure")
            }
        }

        fun setRemoteConfigInSP(key: String) {
            val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
            val configSettingsBuilder = FirebaseRemoteConfigSettings.Builder()
            if (BuildConfig.DEBUG) {
                configSettingsBuilder.setDeveloperModeEnabled(BuildConfig.DEBUG)
            }
            configSettingsBuilder.build()
            mFirebaseRemoteConfig.setConfigSettings(configSettingsBuilder.build())
            //mFirebaseRemoteConfig.setDefaults(R.xml.default_config)
            mFirebaseRemoteConfig.fetch(0).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Logger.d(TAG, "Fetch Succeeded")
                    mFirebaseRemoteConfig.activateFetched()
                    val value = mFirebaseRemoteConfig.getString(key)
                    if (!TextUtils.isEmpty(value)) {
                        SPUtils.writeString(key, value)
                    }
                    Logger.d(TAG, value)
                    MarvelAnalytics.getInstance().sendLogs("config", "success")
                } else {
                    Logger.d(TAG, "Fetch Failed")
                    MarvelAnalytics.getInstance().sendLogs("config", "failed")
                }
            }.addOnFailureListener {
                Logger.d(TAG, "Fetch failure")
                MarvelAnalytics.getInstance().sendLogs("config", "failure")
            }
        }
    }
}