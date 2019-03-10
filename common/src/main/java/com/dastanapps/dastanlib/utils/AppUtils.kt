package com.dastanapps.dastanlib.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager


/**
 * Created by dastaniqbal on 15/12/2016.
 * 15/12/2016 4:20
 */

object AppUtils {
    @Throws(PackageManager.NameNotFoundException::class)
    fun getVersionCode(ctxt: Context): Int {
        return ctxt.packageManager.getPackageInfo(ctxt.packageName, 0).versionCode
    }

    fun checkAppInstall(context: Context, uri: String): Boolean {
        val pm = context.packageManager
        return try {
            val packageInfo = pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            packageInfo != null
        } catch (e: Exception) {
            false
        }

    }

    fun addAppShortcut(context: Context, shortCutintent: Intent, name: String, icon: Int) {

        removeAppShortcut(context, shortCutintent, name)

        shortCutintent.action = Intent.ACTION_MAIN
        val intent = Intent()
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutintent)

        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name)

        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context,
                        icon))

        intent.action = "com.android.launcher.action.INSTALL_SHORTCUT"
        context.sendBroadcast(intent)

    }

    fun removeAppShortcut(context: Context, shortCutintent: Intent, name: String) {

        shortCutintent.action = Intent.ACTION_MAIN

        val intent = Intent()
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutintent)
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name)
        intent.action = "com.android.launcher.action.UNINSTALL_SHORTCUT"
        context.sendBroadcast(intent)


    }
}
