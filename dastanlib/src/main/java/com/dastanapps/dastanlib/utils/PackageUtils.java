package com.dastanapps.dastanlib.utils;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.net.Uri;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.dastanapps.dastanlib.DastanApp;
import com.dastanapps.dastanlib.log.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import static android.content.pm.ApplicationInfo.FLAG_ALLOW_BACKUP;
import static android.content.pm.ApplicationInfo.FLAG_ALLOW_CLEAR_USER_DATA;
import static android.content.pm.ApplicationInfo.FLAG_ALLOW_TASK_REPARENTING;
import static android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE;
import static android.content.pm.ApplicationInfo.FLAG_EXTERNAL_STORAGE;
import static android.content.pm.ApplicationInfo.FLAG_EXTRACT_NATIVE_LIBS;
import static android.content.pm.ApplicationInfo.FLAG_FACTORY_TEST;
import static android.content.pm.ApplicationInfo.FLAG_FULL_BACKUP_ONLY;
import static android.content.pm.ApplicationInfo.FLAG_HARDWARE_ACCELERATED;
import static android.content.pm.ApplicationInfo.FLAG_HAS_CODE;
import static android.content.pm.ApplicationInfo.FLAG_INSTALLED;
import static android.content.pm.ApplicationInfo.FLAG_IS_DATA_ONLY;
import static android.content.pm.ApplicationInfo.FLAG_IS_GAME;
import static android.content.pm.ApplicationInfo.FLAG_KILL_AFTER_RESTORE;
import static android.content.pm.ApplicationInfo.FLAG_LARGE_HEAP;
import static android.content.pm.ApplicationInfo.FLAG_MULTIARCH;
import static android.content.pm.ApplicationInfo.FLAG_PERSISTENT;
import static android.content.pm.ApplicationInfo.FLAG_RESIZEABLE_FOR_SCREENS;
import static android.content.pm.ApplicationInfo.FLAG_RESTORE_ANY_VERSION;
import static android.content.pm.ApplicationInfo.FLAG_STOPPED;
import static android.content.pm.ApplicationInfo.FLAG_SUPPORTS_LARGE_SCREENS;
import static android.content.pm.ApplicationInfo.FLAG_SUPPORTS_NORMAL_SCREENS;
import static android.content.pm.ApplicationInfo.FLAG_SUPPORTS_RTL;
import static android.content.pm.ApplicationInfo.FLAG_SUPPORTS_SCREEN_DENSITIES;
import static android.content.pm.ApplicationInfo.FLAG_SUPPORTS_SMALL_SCREENS;
import static android.content.pm.ApplicationInfo.FLAG_SUPPORTS_XLARGE_SCREENS;
import static android.content.pm.ApplicationInfo.FLAG_SUSPENDED;
import static android.content.pm.ApplicationInfo.FLAG_SYSTEM;
import static android.content.pm.ApplicationInfo.FLAG_TEST_ONLY;
import static android.content.pm.ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
import static android.content.pm.ApplicationInfo.FLAG_USES_CLEARTEXT_TRAFFIC;
import static android.content.pm.ApplicationInfo.FLAG_VM_SAFE_MODE;
import static android.content.pm.PackageManager.GET_ACTIVITIES;
import static android.content.pm.PackageManager.GET_CONFIGURATIONS;
import static android.content.pm.PackageManager.GET_GIDS;
import static android.content.pm.PackageManager.GET_INSTRUMENTATION;
import static android.content.pm.PackageManager.GET_INTENT_FILTERS;
import static android.content.pm.PackageManager.GET_META_DATA;
import static android.content.pm.PackageManager.GET_PERMISSIONS;
import static android.content.pm.PackageManager.GET_PROVIDERS;
import static android.content.pm.PackageManager.GET_RECEIVERS;
import static android.content.pm.PackageManager.GET_SERVICES;
import static android.content.pm.PackageManager.GET_SHARED_LIBRARY_FILES;
import static android.content.pm.PackageManager.GET_SIGNATURES;
import static android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES;
import static android.content.pm.PackageManager.GET_URI_PERMISSION_PATTERNS;
import static android.content.pm.PackageManager.MATCH_DISABLED_COMPONENTS;
import static android.content.pm.PackageManager.MATCH_DISABLED_UNTIL_USED_COMPONENTS;
import static android.content.pm.PackageManager.MATCH_UNINSTALLED_PACKAGES;

/**
 * Created by dastaniqbal on 19/01/2017.
 * 19/01/2017 12:48
 */

public class PackageUtils {
    private static final String TAG = PackageUtils.class.getSimpleName();
    private static final int FLAGS_PACKAGE = GET_GIDS | GET_CONFIGURATIONS | GET_PERMISSIONS
            | GET_SIGNATURES | GET_INTENT_FILTERS;
    private static final int FLAGS_APPLICATION = GET_META_DATA;//| GET_SHARED_LIBRARY_FILES |
    //MATCH_DISABLED_UNTIL_USED_COMPONENTS | MATCH_SYSTEM_ONLY | MATCH_UNINSTALLED_PACKAGES;
    private static final int FLAG_PACKAGE_INFO = GET_ACTIVITIES | GET_CONFIGURATIONS | GET_GIDS | GET_INSTRUMENTATION |
            GET_INTENT_FILTERS | GET_META_DATA | GET_PERMISSIONS | GET_PROVIDERS | GET_RECEIVERS | GET_SERVICES |
            GET_SHARED_LIBRARY_FILES | GET_SIGNATURES | GET_URI_PERMISSION_PATTERNS | GET_UNINSTALLED_PACKAGES |
            MATCH_DISABLED_COMPONENTS | MATCH_DISABLED_UNTIL_USED_COMPONENTS | MATCH_UNINSTALLED_PACKAGES;

    public static List<PackageInfo> getInstalledPackages() {
        return DastanApp.getInstance().getPackageManager().getInstalledPackages(FLAGS_PACKAGE);
    }

    public static List<ApplicationInfo> getInstalledApplication() {
        return DastanApp.getInstance().getPackageManager().getInstalledApplications(FLAGS_APPLICATION);
    }

    public static List<PackageInfo> getNonSystemPackages() {
        List<PackageInfo> applicationInfoList = new ArrayList<>();
        List<PackageInfo> pkgInfoList = PackageUtils.getInstalledPackages();
        for (int i = 0; i < pkgInfoList.size(); i++) {
            PackageInfo pkgInfo = pkgInfoList.get(i);
            if ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 &&
                    DastanApp.getInstance().getPackageManager().getLaunchIntentForPackage(pkgInfo.packageName) != null) {
                applicationInfoList.add(pkgInfo);
                Logger.d(TAG, pkgInfo.applicationInfo.loadLabel(DastanApp.getInstance().getPackageManager()) + " removed "
                        + PackageUtils.getApplicationFlagName(pkgInfo.applicationInfo.flags));
            }
        }
        return applicationInfoList;
    }

    public static String getSHA256Signature(Signature[] signatures) {
        byte[] cert = signatures[0].toByteArray();
        InputStream input = new ByteArrayInputStream(cert);
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        X509Certificate c = null;
        String hexString = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
            MessageDigest md = MessageDigest.getInstance("SHA256");
            byte[] publicKey = md.digest(c.getEncoded());
            hexString = byte2HexFormatted(publicKey);
        } catch (Exception e1) {
            Logger.d(TAG, "Exception " + e1.getMessage());
        }
        return hexString;
    }

    public static String getSHA1Signature(Signature[] signatures) {
        byte[] cert = signatures[0].toByteArray();
        InputStream input = new ByteArrayInputStream(cert);
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        X509Certificate c = null;
        String hexString = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(c.getEncoded());
            hexString = byte2HexFormatted(publicKey);
        } catch (Exception e1) {
            Logger.d(TAG, "Exception " + e1.getMessage());
        }
        return hexString;
    }

    public static String getMD5Signature(Signature[] signatures) {
        byte[] cert = signatures[0].toByteArray();
        InputStream input = new ByteArrayInputStream(cert);
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        X509Certificate c = null;
        String hexString = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] publicKey = md.digest(c.getEncoded());
            hexString = byte2HexFormatted(publicKey);
        } catch (Exception e1) {
            Logger.d(TAG, "Exception " + e1.getMessage());
        }
        return hexString;
    }

    public static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) h = "0" + h;
            if (l > 2) h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            //if (i < (arr.length - 1)) str.append(':');
        }
        return str.toString();
    }

    public static JSONArray getPackageJson() {
        JSONArray jsonArray = new JSONArray();
        List<PackageInfo> pkgInfoList = PackageUtils.getInstalledPackages();
        for (int i = 0; i < pkgInfoList.size(); i++) {
            PackageInfo pkgInfo = pkgInfoList.get(i);
            try {
                if (!pkgInfo.packageName.equals(DastanApp.getInstance().getPackageName())) {
                    jsonArray.put(getPackageInfoJsonObj(pkgInfo));
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return jsonArray;
    }

    public static JSONObject getPackageInfoJsonObj(PackageInfo pkgInfo) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("version", pkgInfo.versionName + "(" + pkgInfo.versionCode + ")");
            jsonObject.put("name", pkgInfo.applicationInfo.loadLabel(DastanApp.getInstance().getPackageManager()));
            jsonObject.put("pkgname", pkgInfo.packageName);
            String sha256 = getSHA256Signature(pkgInfo.signatures);
            String sha1 = getSHA1Signature(pkgInfo.signatures);
            String md5 = getMD5Signature(pkgInfo.signatures);
            if (sha1 != null)
                jsonObject.put("sha1", sha1);
            if (sha256 != null)
                jsonObject.put("sha256", sha256);
            if (md5 != null)
                jsonObject.put("md5", md5);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return jsonObject;
    }

    public static PackageInfo getPackageInfor(String packageName) {
        try {
            Thread.sleep(100);
            return DastanApp.getInstance().getPackageManager().getPackageInfo(packageName, FLAG_PACKAGE_INFO);
        } catch (Exception e) {
            Crashlytics.log(Log.ERROR, "packgInfo", e.getMessage());
            return null;
        }
    }

    public static String getApplicationFlagName(int flag) {
        if ((flag & FLAG_SYSTEM) != 0) {
            return "FLAG_SYSTEM";
        }
        if ((flag & FLAG_DEBUGGABLE) != 0)
            return "FLAG_DEBUGGABLE";

        if ((flag & FLAG_HAS_CODE) != 0)
            return "FLAG_HAS_CODE";

        if ((flag & FLAG_PERSISTENT) != 0)
            return "FLAG_PERSISTENT";

        if ((flag & FLAG_FACTORY_TEST) != 0)
            return "FLAG_FACTORY_TEST";

        if ((flag & FLAG_ALLOW_TASK_REPARENTING) != 0)
            return "FLAG_ALLOW_TASK_REPARENTING";

        if ((flag & FLAG_ALLOW_CLEAR_USER_DATA) != 0)
            return "FLAG_ALLOW_CLEAR_USER_DATA";

        if ((flag & FLAG_UPDATED_SYSTEM_APP) != 0)
            return "FLAG_UPDATED_SYSTEM_APP";

        if ((flag & FLAG_TEST_ONLY) != 0)
            return "FLAG_TEST_ONLY";

        if ((flag & FLAG_SUPPORTS_SMALL_SCREENS) != 0)
            return "FLAG_SUPPORTS_SMALL_SCREENS";

        if ((flag & FLAG_SUPPORTS_NORMAL_SCREENS) != 0)
            return "FLAG_SUPPORTS_NORMAL_SCREENS";

        if ((flag & FLAG_SUPPORTS_LARGE_SCREENS) != 0)
            return "FLAG_SUPPORTS_LARGE_SCREENS";

        if ((flag & FLAG_RESIZEABLE_FOR_SCREENS) != 0)
            return "FLAG_RESIZEABLE_FOR_SCREENS";

        if ((flag & FLAG_SUPPORTS_SCREEN_DENSITIES) != 0)
            return "FLAG_SUPPORTS_SCREEN_DENSITIES";

        if ((flag & FLAG_VM_SAFE_MODE) != 0)
            return "FLAG_VM_SAFE_MODE";

        if ((flag & FLAG_ALLOW_BACKUP) != 0)
            return "FLAG_ALLOW_BACKUP";

        if ((flag & FLAG_KILL_AFTER_RESTORE) != 0)
            return "FLAG_KILL_AFTER_RESTORE";

        if ((flag & FLAG_RESTORE_ANY_VERSION) != 0)
            return "FLAG_RESTORE_ANY_VERSION";

        if ((flag & FLAG_EXTERNAL_STORAGE) != 0)
            return "FLAG_EXTERNAL_STORAGE";

        if ((flag & FLAG_SUPPORTS_XLARGE_SCREENS) != 0)
            return "FLAG_SUPPORTS_XLARGE_SCREENS";

        if ((flag & FLAG_LARGE_HEAP) != 0)
            return "FLAG_LARGE_HEAP";

        if ((flag & FLAG_STOPPED) != 0)
            return "FLAG_STOPPED";

        if ((flag & FLAG_SUPPORTS_RTL) != 0)
            return "FLAG_SUPPORTS_RTL";

        if ((flag & FLAG_INSTALLED) != 0)
            return "FLAG_INSTALLED";
        if ((flag & FLAG_IS_DATA_ONLY) != 0)
            return "FLAG_IS_DATA_ONLY";

        if ((flag & FLAG_IS_GAME) != 0)
            return "FLAG_IS_GAME";
        if ((flag & FLAG_FULL_BACKUP_ONLY) != 0)
            return "FLAG_FULL_BACKUP_ONLY";

        if ((flag & FLAG_USES_CLEARTEXT_TRAFFIC) != 0)
            return "FLAG_USES_CLEARTEXT_TRAFFIC";
        if ((flag & FLAG_EXTRACT_NATIVE_LIBS) != 0)
            return "FLAG_EXTRACT_NATIVE_LIBS";

        if ((flag & FLAG_HARDWARE_ACCELERATED) != 0)
            return "FLAG_HARDWARE_ACCELERATED";
        if ((flag & FLAG_SUSPENDED) != 0)
            return "FLAG_SUSPENDED";

        if ((flag & FLAG_MULTIARCH) != 0)
            return "FALG_MULTIARCH";

        return "";
    }

    public static void uninstallPkg(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("package:" + packageName));
        DastanApp.getInstance().startActivity(intent);
    }

    public static void launchApp(String packageName) {
        Intent intent = DastanApp.getInstance().getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null)
            DastanApp.getInstance().startActivity(intent);
    }
}
