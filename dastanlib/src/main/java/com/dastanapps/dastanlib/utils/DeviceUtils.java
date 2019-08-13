package com.dastanapps.dastanlib.utils;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import androidx.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.dastanapps.dastanlib.DastanApp;
import com.dastanapps.dastanlib.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dastaniqbal on 15/12/2016.
 * ask2iqbal@gmail.com
 * 15/12/2016 11:05
 */

public class DeviceUtils {
    private static final String TAG = DeviceUtils.class.getSimpleName();

    public static String getDeviceInfo() {
        TelephonyManager telephonyManager = (TelephonyManager) DastanApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        JSONObject jsonObject = new JSONObject();
        try {
            String mPhoneNumber = "";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                int hasWriteContactsPermission = DastanApp.getInstance().checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
                if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                    mPhoneNumber = telephonyManager.getLine1Number();
                    String mDeviceId = telephonyManager.getDeviceId(); //imei or MEID
                    jsonObject.put("deviceId", mDeviceId);
                    String imsi = telephonyManager.getSubscriberId();
                    jsonObject.put("imsi", imsi);
                    jsonObject.put("imei", mDeviceId);

                }
            } else {
                mPhoneNumber = telephonyManager.getLine1Number();
                String mDeviceId = telephonyManager.getDeviceId(); //imei or MEID
                jsonObject.put("deviceId", mDeviceId);
                String imsi = telephonyManager.getSubscriberId();
                jsonObject.put("imsi", imsi);
                jsonObject.put("imei", mDeviceId);

            }
            if (TextUtils.isEmpty(mPhoneNumber)) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    int hasWriteContactsPermission = DastanApp.getInstance().checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
                    if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                        mPhoneNumber = getPhoneNumberAcc(mPhoneNumber);
                    }
                } else {
                    mPhoneNumber = getPhoneNumberAcc(mPhoneNumber);
                }

            }

            jsonObject.put("phoneno", mPhoneNumber);
            String networkOperator = telephonyManager.getNetworkOperator();
            String simOperator = telephonyManager.getSimOperator();
            if (!TextUtils.isEmpty(simOperator)) {
//                int mcc = Integer.parseInt(simOperator.substring(0, 3));
//                int mnc = Integer.parseInt(simOperator.substring(3));
//                jsonObject.put("mcc", mcc);
//                jsonObject.put("mnc", mnc);
                jsonObject.put("mccmnc", simOperator);
            } else if (!TextUtils.isEmpty(networkOperator)) {
//                int mcc = Integer.parseInt(networkOperator.substring(0, 3));
//                int mnc = Integer.parseInt(networkOperator.substring(3));
//                jsonObject.put("mcc", mcc);
//                jsonObject.put("mnc", mnc);
                jsonObject.put("mccmnc", networkOperator);
            } else {
                Configuration configuration = DastanApp.getInstance().getResources().getConfiguration();
                if (configuration != null) {
                    int mcc = configuration.mcc;
                    int mnc = configuration.mnc;
                    jsonObject.put("mcc", mcc);
                    jsonObject.put("mnc", mnc);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    @Nullable
    private static String getPhoneNumberAcc(String mPhoneNumber) {
        AccountManager am = AccountManager.get(DastanApp.getInstance());
        Account[] accounts = am.getAccounts();

        for (Account ac : accounts) {
            String acname = ac.name;
            String actype = ac.type;
            if (actype.startsWith("com.whatsapp")) {
                mPhoneNumber = ac.name;
            }

            if (TextUtils.isEmpty(mPhoneNumber)) {
                if (actype.startsWith("com.viber.voip")) {
                    mPhoneNumber = ac.name;
                }
            }
            // Take your time to look at all available accounts
            Logger.d(TAG, "Accounts : " + acname + ", " + actype);
        }
        return mPhoneNumber;
    }

    public static String getDeviceSerial() {
       /* String serial = android.os.Build.SERIAL;
        if (TextUtils.isEmpty(serial)) {
            serial = Settings.Secure.getString(DastanLibApp.INSTANCE.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        if (TextUtils.isEmpty(serial)) {*/
          String  serial = "34" +
                    Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                    Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                    Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                    Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                    Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                    Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                    Build.USER.length() % 10;
        //}
        return serial;
    }

    public static String getBasicDeviceInfo() {
        return Build.MANUFACTURER + Build.MODEL +
                Build.DEVICE + Build.VERSION.RELEASE + Build.VERSION.SDK_INT;
    }
}
