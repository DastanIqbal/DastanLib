package com.videoeditor.kruso.lib.firebase

import android.text.TextUtils
import com.dastanapps.dastanlib.BuildConfig
import com.dastanapps.dastanlib.DastanApp
import com.dastanapps.dastanlib.analytics.DAnalytics
import com.dastanapps.dastanlib.log.Logger
import com.dastanapps.dastanlib.services.MarvelService
import com.dastanapps.dastanlib.utils.SPUtils
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
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
                        DastanApp.getAppInstance().setPushURL(url)
                        MarvelService.getInstance().sendtoken()
                    }
                    Logger.d(TAG, url)
                    DAnalytics.getInstance().sendLogs("config", "success")
                } else {
                    Logger.d(TAG, "Fetch Failed")
                    DAnalytics.getInstance().sendLogs("config", "failed")
                }
            }.addOnFailureListener {
                Logger.d(TAG, "Fetch failure")
                DAnalytics.getInstance().sendLogs("config", "failure")
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
                    DAnalytics.getInstance().sendLogs("config", "success")
                } else {
                    Logger.d(TAG, "Fetch Failed")
                    DAnalytics.getInstance().sendLogs("config", "failed")
                }
            }.addOnFailureListener {
                Logger.d(TAG, "Fetch failure")
                DAnalytics.getInstance().sendLogs("config", "failure")
            }
        }
    }
}