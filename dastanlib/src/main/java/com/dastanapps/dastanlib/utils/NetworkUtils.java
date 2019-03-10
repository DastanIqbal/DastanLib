package com.dastanapps.dastanlib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.dastanapps.dastanlib.DastanApp;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Iqbal Ahmed on 10/8/2015.
 */
public class NetworkUtils {

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public static String bytes2String(long sizeInBytes) {
        double SPACE_KB = 1024;
        double SPACE_MB = 1024 * SPACE_KB;
        double SPACE_GB = 1024 * SPACE_MB;
        double SPACE_TB = 1024 * SPACE_GB;
        NumberFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(2);

        try {
            if (sizeInBytes < SPACE_KB) {
                return nf.format(sizeInBytes) + " Byte(s)";
            } else if (sizeInBytes < SPACE_MB) {
                return nf.format(sizeInBytes / SPACE_KB) + " KB";
            } else if (sizeInBytes < SPACE_GB) {
                return nf.format(sizeInBytes / SPACE_MB) + " MB";
            } else if (sizeInBytes < SPACE_TB) {
                return nf.format(sizeInBytes / SPACE_GB) + " GB";
            } else {
                return nf.format(sizeInBytes / SPACE_TB) + " TB";
            }
        } catch (Exception e) {
            return sizeInBytes + " Byte(s)";
        }
    }

    public static boolean checkMobileNetworkAvailable() {
        boolean isMobileNetworkAvailable = false;
        ConnectivityManager cm = (ConnectivityManager) DastanApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networks = cm.getAllNetworkInfo();
        for (int i = 0; i < networks.length; i++) {
            if (networks[i].getType() == ConnectivityManager.TYPE_MOBILE) {
                if (networks[i].isConnected()) {
                    isMobileNetworkAvailable = true;
                }
            }
        }

        return isMobileNetworkAvailable;
    }

    public static Boolean isMobileAvailable() {
        TelephonyManager tel = (TelephonyManager) DastanApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        return ((tel.getNetworkOperator() != null && tel.getNetworkOperator().equals("")) ? false : true);
    }

    public static boolean canSendSMS() {
        TelephonyManager manager = (TelephonyManager) DastanApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return manager.isSmsCapable();
        } else {
            return true;
        }
    }
}
