package com.dastanapps.dastanlib.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.ApplicationInfo.*
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.*
import android.content.pm.Signature
import android.net.Uri
import com.dastanapps.dastanlib.log.Logger
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.security.MessageDigest
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*

/**
 * Created by dastaniqbal on 19/01/2017.
 * 19/01/2017 12:48
 */

object PackageUtils {
    private val TAG = PackageUtils::class.java.simpleName
    private val FLAGS_PACKAGE = (GET_GIDS or GET_CONFIGURATIONS or GET_PERMISSIONS
            or GET_SIGNATURES or GET_INTENT_FILTERS)
    private val FLAGS_APPLICATION = GET_META_DATA//| GET_SHARED_LIBRARY_FILES |
    //MATCH_DISABLED_UNTIL_USED_COMPONENTS | MATCH_SYSTEM_ONLY | MATCH_UNINSTALLED_PACKAGES;
    private val FLAG_PACKAGE_INFO = GET_ACTIVITIES or GET_CONFIGURATIONS or GET_GIDS or GET_INSTRUMENTATION or
            GET_INTENT_FILTERS or GET_META_DATA or GET_PERMISSIONS or GET_PROVIDERS or GET_RECEIVERS or GET_SERVICES or
            GET_SHARED_LIBRARY_FILES or GET_SIGNATURES or GET_URI_PERMISSION_PATTERNS or GET_UNINSTALLED_PACKAGES or
            MATCH_DISABLED_COMPONENTS or MATCH_DISABLED_UNTIL_USED_COMPONENTS or MATCH_UNINSTALLED_PACKAGES

    fun getInstalledPackages(context: Context): List<PackageInfo> {
        return context.packageManager.getInstalledPackages(FLAGS_PACKAGE)
    }

    fun getInstalledApplication(context: Context): List<ApplicationInfo> {
        return context.packageManager.getInstalledApplications(FLAGS_APPLICATION)
    }

    fun getNonSystemPackages(context: Context): List<PackageInfo> {
        val applicationInfoList = ArrayList<PackageInfo>()
        val pkgInfoList = PackageUtils.getInstalledPackages(context)
        for (i in pkgInfoList.indices) {
            val pkgInfo = pkgInfoList[i]
            if (pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0 && context.packageManager.getLaunchIntentForPackage(pkgInfo.packageName) != null) {
                applicationInfoList.add(pkgInfo)
                Logger.d(TAG, pkgInfo.applicationInfo.loadLabel(context.packageManager).toString() + " removed "
                        + PackageUtils.getApplicationFlagName(pkgInfo.applicationInfo.flags))
            }
        }
        return applicationInfoList
    }

    fun getSHA256Signature(signatures: Array<Signature>): String? {
        val cert = signatures[0].toByteArray()
        val input = ByteArrayInputStream(cert)
        var cf: CertificateFactory? = null
        try {
            cf = CertificateFactory.getInstance("X509")
        } catch (e: CertificateException) {
            e.printStackTrace()
        }

        var c: X509Certificate? = null
        var hexString: String? = null
        try {
            c = cf!!.generateCertificate(input) as X509Certificate
            val md = MessageDigest.getInstance("SHA256")
            val publicKey = md.digest(c.encoded)
            hexString = byte2HexFormatted(publicKey)
        } catch (e1: Exception) {
            Logger.d(TAG, "Exception " + e1.message)
        }

        return hexString
    }

    fun getSHA1Signature(signatures: Array<Signature>): String? {
        val cert = signatures[0].toByteArray()
        val input = ByteArrayInputStream(cert)
        var cf: CertificateFactory? = null
        try {
            cf = CertificateFactory.getInstance("X509")
        } catch (e: CertificateException) {
            e.printStackTrace()
        }

        var c: X509Certificate? = null
        var hexString: String? = null
        try {
            c = cf!!.generateCertificate(input) as X509Certificate
            val md = MessageDigest.getInstance("SHA1")
            val publicKey = md.digest(c.encoded)
            hexString = byte2HexFormatted(publicKey)
        } catch (e1: Exception) {
            Logger.d(TAG, "Exception " + e1.message)
        }

        return hexString
    }

    fun getMD5Signature(signatures: Array<Signature>): String? {
        val cert = signatures[0].toByteArray()
        val input = ByteArrayInputStream(cert)
        var cf: CertificateFactory? = null
        try {
            cf = CertificateFactory.getInstance("X509")
        } catch (e: CertificateException) {
            e.printStackTrace()
        }

        var c: X509Certificate? = null
        var hexString: String? = null
        try {
            c = cf!!.generateCertificate(input) as X509Certificate
            val md = MessageDigest.getInstance("MD5")
            val publicKey = md.digest(c.encoded)
            hexString = byte2HexFormatted(publicKey)
        } catch (e1: Exception) {
            Logger.d(TAG, "Exception " + e1.message)
        }

        return hexString
    }

    fun byte2HexFormatted(arr: ByteArray): String {
        val str = StringBuilder(arr.size * 2)
        for (i in arr.indices) {
            var h = Integer.toHexString(arr[i].toInt())
            val l = h.length
            if (l == 1) h = "0$h"
            if (l > 2) h = h.substring(l - 2, l)
            str.append(h.toUpperCase())
            //if (i < (arr.length - 1)) str.append(':');
        }
        return str.toString()
    }

    fun getPackageJson(context: Context): JSONArray {
        val jsonArray = JSONArray()
        val pkgInfoList = PackageUtils.getInstalledPackages(context)
        for (i in pkgInfoList.indices) {
            val pkgInfo = pkgInfoList[i]
            try {
                if (pkgInfo.packageName != context.packageName) {
                    jsonArray.put(getPackageInfoJsonObj(context, pkgInfo))
                }
            } catch (e: Exception) {
                //e.printStackTrace();
            }

        }
        return jsonArray
    }

    fun getPackageInfoJsonObj(context: Context, pkgInfo: PackageInfo): JSONObject {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("version", pkgInfo.versionName + "(" + pkgInfo.versionCode + ")")
            jsonObject.put("name", pkgInfo.applicationInfo.loadLabel(context.packageManager))
            jsonObject.put("pkgname", pkgInfo.packageName)
            val sha256 = getSHA256Signature(pkgInfo.signatures)
            val sha1 = getSHA1Signature(pkgInfo.signatures)
            val md5 = getMD5Signature(pkgInfo.signatures)
            if (sha1 != null)
                jsonObject.put("sha1", sha1)
            if (sha256 != null)
                jsonObject.put("sha256", sha256)
            if (md5 != null)
                jsonObject.put("md5", md5)
        } catch (e: Exception) {
            //e.printStackTrace();
        }

        return jsonObject
    }

    fun getPackageInfor(context: Context, packageName: String): PackageInfo? {
        try {
            Thread.sleep(100)
            return context.packageManager.getPackageInfo(packageName, FLAG_PACKAGE_INFO)
        } catch (e: Exception) {
            Logger.e(TAG, "packgInfo" + e.message)
            return null
        }

    }

    fun getApplicationFlagName(flag: Int): String {
        if (flag and FLAG_SYSTEM != 0) {
            return "FLAG_SYSTEM"
        }
        if (flag and FLAG_DEBUGGABLE != 0)
            return "FLAG_DEBUGGABLE"

        if (flag and FLAG_HAS_CODE != 0)
            return "FLAG_HAS_CODE"

        if (flag and FLAG_PERSISTENT != 0)
            return "FLAG_PERSISTENT"

        if (flag and FLAG_FACTORY_TEST != 0)
            return "FLAG_FACTORY_TEST"

        if (flag and FLAG_ALLOW_TASK_REPARENTING != 0)
            return "FLAG_ALLOW_TASK_REPARENTING"

        if (flag and FLAG_ALLOW_CLEAR_USER_DATA != 0)
            return "FLAG_ALLOW_CLEAR_USER_DATA"

        if (flag and FLAG_UPDATED_SYSTEM_APP != 0)
            return "FLAG_UPDATED_SYSTEM_APP"

        if (flag and FLAG_TEST_ONLY != 0)
            return "FLAG_TEST_ONLY"

        if (flag and FLAG_SUPPORTS_SMALL_SCREENS != 0)
            return "FLAG_SUPPORTS_SMALL_SCREENS"

        if (flag and FLAG_SUPPORTS_NORMAL_SCREENS != 0)
            return "FLAG_SUPPORTS_NORMAL_SCREENS"

        if (flag and FLAG_SUPPORTS_LARGE_SCREENS != 0)
            return "FLAG_SUPPORTS_LARGE_SCREENS"

        if (flag and FLAG_RESIZEABLE_FOR_SCREENS != 0)
            return "FLAG_RESIZEABLE_FOR_SCREENS"

        if (flag and FLAG_SUPPORTS_SCREEN_DENSITIES != 0)
            return "FLAG_SUPPORTS_SCREEN_DENSITIES"

        if (flag and FLAG_VM_SAFE_MODE != 0)
            return "FLAG_VM_SAFE_MODE"

        if (flag and FLAG_ALLOW_BACKUP != 0)
            return "FLAG_ALLOW_BACKUP"

        if (flag and FLAG_KILL_AFTER_RESTORE != 0)
            return "FLAG_KILL_AFTER_RESTORE"

        if (flag and FLAG_RESTORE_ANY_VERSION != 0)
            return "FLAG_RESTORE_ANY_VERSION"

        if (flag and FLAG_EXTERNAL_STORAGE != 0)
            return "FLAG_EXTERNAL_STORAGE"

        if (flag and FLAG_SUPPORTS_XLARGE_SCREENS != 0)
            return "FLAG_SUPPORTS_XLARGE_SCREENS"

        if (flag and FLAG_LARGE_HEAP != 0)
            return "FLAG_LARGE_HEAP"

        if (flag and FLAG_STOPPED != 0)
            return "FLAG_STOPPED"

        if (flag and FLAG_SUPPORTS_RTL != 0)
            return "FLAG_SUPPORTS_RTL"

        if (flag and FLAG_INSTALLED != 0)
            return "FLAG_INSTALLED"
        if (flag and FLAG_IS_DATA_ONLY != 0)
            return "FLAG_IS_DATA_ONLY"

        if (flag and FLAG_IS_GAME != 0)
            return "FLAG_IS_GAME"
        if (flag and FLAG_FULL_BACKUP_ONLY != 0)
            return "FLAG_FULL_BACKUP_ONLY"

        if (flag and FLAG_USES_CLEARTEXT_TRAFFIC != 0)
            return "FLAG_USES_CLEARTEXT_TRAFFIC"
        if (flag and FLAG_EXTRACT_NATIVE_LIBS != 0)
            return "FLAG_EXTRACT_NATIVE_LIBS"

        if (flag and FLAG_HARDWARE_ACCELERATED != 0)
            return "FLAG_HARDWARE_ACCELERATED"
        if (flag and FLAG_SUSPENDED != 0)
            return "FLAG_SUSPENDED"

        return if (flag and FLAG_MULTIARCH != 0) "FALG_MULTIARCH" else ""

    }

    fun uninstallPkg(context: Context, packageName: String) {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.data = Uri.parse("package:$packageName")
        context.startActivity(intent)
    }

    fun launchApp(context: Context, packageName: String) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null)
            context.startActivity(intent)
    }
}
