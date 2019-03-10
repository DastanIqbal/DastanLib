package com.dastanapps.dastanlib.utils

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.support.annotation.RequiresPermission
import android.telephony.TelephonyManager
import java.text.DecimalFormat

/**
 * Created by Iqbal Ahmed on 10/8/2015.
 */
object NetworkUtils {
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isConnectingToInternet(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null)
                for (i in info.indices)
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
        }
        return false
    }

    fun bytes2String(sizeInBytes: Long): String {
        val SPACE_KB = 1024.0
        val SPACE_MB = 1024 * SPACE_KB
        val SPACE_GB = 1024 * SPACE_MB
        val SPACE_TB = 1024 * SPACE_GB
        val nf = DecimalFormat()
        nf.maximumFractionDigits = 2

        try {
            return if (sizeInBytes < SPACE_KB) {
                nf.format(sizeInBytes) + " Byte(s)"
            } else if (sizeInBytes < SPACE_MB) {
                nf.format(sizeInBytes / SPACE_KB) + " KB"
            } else if (sizeInBytes < SPACE_GB) {
                nf.format(sizeInBytes / SPACE_MB) + " MB"
            } else if (sizeInBytes < SPACE_TB) {
                nf.format(sizeInBytes / SPACE_GB) + " GB"
            } else {
                nf.format(sizeInBytes / SPACE_TB) + " TB"
            }
        } catch (e: Exception) {
            return sizeInBytes.toString() + " Byte(s)"
        }

    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun checkMobileNetworkAvailable(context: Context): Boolean {
        var isMobileNetworkAvailable = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networks = cm.allNetworkInfo
        for (i in networks.indices) {
            if (networks[i].type == ConnectivityManager.TYPE_MOBILE) {
                if (networks[i].isConnected) {
                    isMobileNetworkAvailable = true
                }
            }
        }

        return isMobileNetworkAvailable
    }

    fun isMobileAvailable(context: Context): Boolean {
        val tel = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (tel.networkOperator != null && tel.networkOperator == "") false else true
    }

    fun canSendSMS(context: Context): Boolean {
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            manager.isSmsCapable
        } else {
            true
        }
    }
}
