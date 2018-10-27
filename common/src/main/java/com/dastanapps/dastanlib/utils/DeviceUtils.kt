package com.dastanapps.dastanlib.utils

import android.Manifest
import android.accounts.AccountManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.RequiresPermission
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.dastanapps.dastanlib.log.Logger
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by dastaniqbal on 15/12/2016.
 * ask2iqbal@gmail.com
 * 15/12/2016 11:05
 */

object DeviceUtils {
    private val TAG = DeviceUtils::class.java.simpleName

    /* String serial = android.os.Build.SERIAL;
        if (TextUtils.isEmpty(serial)) {
            serial = Settings.Secure.getString(MarvelApp.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        if (TextUtils.isEmpty(serial)) {*///}
    val deviceSerial: String
        get() = "34" +
                Build.BOARD.length % 10 + Build.BRAND.length % 10 +
                Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 +
                Build.DISPLAY.length % 10 + Build.HOST.length % 10 +
                Build.ID.length % 10 + Build.MANUFACTURER.length % 10 +
                Build.MODEL.length % 10 + Build.PRODUCT.length % 10 +
                Build.TAGS.length % 10 + Build.TYPE.length % 10 +
                Build.USER.length % 10

    val basicDeviceInfo: String
        get() = Build.MANUFACTURER + Build.MODEL +
                Build.DEVICE + Build.VERSION.RELEASE + Build.VERSION.SDK_INT

    @RequiresPermission(allOf = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.GET_ACCOUNTS))
    fun getDeviceInfo(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val jsonObject = JSONObject()
        try {
            var mPhoneNumber: String? = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val hasWriteContactsPermission = context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                    mPhoneNumber = telephonyManager.line1Number
                    val mDeviceId = telephonyManager.deviceId //imei or MEID
                    jsonObject.put("deviceId", mDeviceId)
                    val imsi = telephonyManager.subscriberId
                    jsonObject.put("imsi", imsi)
                    jsonObject.put("imei", mDeviceId)

                }
            } else {
                mPhoneNumber = telephonyManager.line1Number
                val mDeviceId = telephonyManager.deviceId //imei or MEID
                jsonObject.put("deviceId", mDeviceId)
                val imsi = telephonyManager.subscriberId
                jsonObject.put("imsi", imsi)
                jsonObject.put("imei", mDeviceId)

            }
            if (TextUtils.isEmpty(mPhoneNumber)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val hasWriteContactsPermission = context.checkSelfPermission(Manifest.permission.GET_ACCOUNTS)
                    if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                        mPhoneNumber = getPhoneNumberAcc(context, mPhoneNumber)
                    }
                } else {
                    mPhoneNumber = getPhoneNumberAcc(context, mPhoneNumber)
                }

            }

            jsonObject.put("phoneno", mPhoneNumber)
            val networkOperator = telephonyManager.networkOperator
            val simOperator = telephonyManager.simOperator
            if (!TextUtils.isEmpty(simOperator)) {
                //                int mcc = Integer.parseInt(simOperator.substring(0, 3));
                //                int mnc = Integer.parseInt(simOperator.substring(3));
                //                jsonObject.put("mcc", mcc);
                //                jsonObject.put("mnc", mnc);
                jsonObject.put("mccmnc", simOperator)
            } else if (!TextUtils.isEmpty(networkOperator)) {
                //                int mcc = Integer.parseInt(networkOperator.substring(0, 3));
                //                int mnc = Integer.parseInt(networkOperator.substring(3));
                //                jsonObject.put("mcc", mcc);
                //                jsonObject.put("mnc", mnc);
                jsonObject.put("mccmnc", networkOperator)
            } else {
                val configuration = context.resources.configuration
                if (configuration != null) {
                    val mcc = configuration.mcc
                    val mnc = configuration.mnc
                    jsonObject.put("mcc", mcc)
                    jsonObject.put("mnc", mnc)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jsonObject.toString()
    }

    @RequiresPermission(Manifest.permission.GET_ACCOUNTS)
    private fun getPhoneNumberAcc(context: Context, mPhoneNumber: String?): String? {
        var mPhoneNumber = mPhoneNumber
        val am = AccountManager.get(context)
        val accounts = am.accounts

        for (ac in accounts) {
            val acname = ac.name
            val actype = ac.type
            if (actype.startsWith("com.whatsapp")) {
                mPhoneNumber = ac.name
            }

            if (TextUtils.isEmpty(mPhoneNumber)) {
                if (actype.startsWith("com.viber.voip")) {
                    mPhoneNumber = ac.name
                }
            }
            // Take your time to look at all available accounts
            Logger.d(TAG, "Accounts : $acname, $actype")
        }
        return mPhoneNumber
    }

    fun getMinimalDeviceInfo(): String {
        return "Device :" + Build.DEVICE + "\n" +
                "Id :" + Build.ID + "\n" +
                "Hardware :" + Build.HARDWARE + "\n" +
                "Manufacturer :" + Build.MANUFACTURER + "\n" +
                "Model :" + Build.MODEL + "\n" +
                "CPU :" + Build.CPU_ABI + "\n" +
                "OS :" + System.getProperty("os.version") + "\n" +
                "SDK :" + Build.VERSION.SDK_INT + "\n"
    }
}
