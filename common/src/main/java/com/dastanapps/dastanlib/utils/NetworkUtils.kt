package com.dastanapps.dastanlib.utils

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresPermission
import android.telephony.TelephonyManager
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat

/**
 * Created by Iqbal Ahmed on 10/8/2015.
 */
object NetworkUtils {
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isConnectingToInternet(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.allNetworkInfo
        if (info != null)
            for (i in info.indices)
                if (info[i].state == NetworkInfo.State.CONNECTED) {
                    return true
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

        return try {
            when {
                sizeInBytes < SPACE_KB -> nf.format(sizeInBytes) + " Byte(s)"
                sizeInBytes < SPACE_MB -> nf.format(sizeInBytes / SPACE_KB) + " KB"
                sizeInBytes < SPACE_GB -> nf.format(sizeInBytes / SPACE_MB) + " MB"
                sizeInBytes < SPACE_TB -> nf.format(sizeInBytes / SPACE_GB) + " GB"
                else -> nf.format(sizeInBytes / SPACE_TB) + " TB"
            }
        } catch (e: Exception) {
            "$sizeInBytes Byte(s)"
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
        return !(tel.networkOperator != null && tel.networkOperator == "")
    }

    fun canSendSMS(context: Context): Boolean {
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            manager.isSmsCapable
        } else {
            true
        }
    }

    @Throws(IOException::class)
    fun getFinalURL(url: String): String {
        val con = URL(url).openConnection() as HttpURLConnection
        con.instanceFollowRedirects = false
        con.connect()
        con.inputStream

        if (con.responseCode == HttpURLConnection.HTTP_MOVED_PERM || con.responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
            val redirectUrl = con.getHeaderField("Location")
            return getFinalURL(redirectUrl)
        }
        return url
    }
}
