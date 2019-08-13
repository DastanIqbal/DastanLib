package com.dastanapps.dastanlib.permissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import com.dastanapps.dastanlib.utils.CommonUtils
import java.util.*

/**
 * Created by Dastan Iqbal on 10/18/2016.
 * author Iqbal Ahmed
 * emailId: ask2iqbal@gmail.com
 */

object RequestPermission {
    private val REQUEST_CODE_ASK_PERMISSIONS = 100

    fun checkPermission(ctxt: Context?, justcheck: Boolean, allPermission: Array<String>): Boolean {
        val permissionsNeeded = ArrayList<String>()
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctxt != null) {
            for (i in allPermission.indices) {
                val hasWriteContactsPermission = ContextCompat.checkSelfPermission(ctxt, allPermission[i])
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                    //open dialog to inform user for permission if requires
                    //                    if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) ctxt, allPermission[i])) {
                    //                        final int finalI = i;
                    //                        showMessageOKCancel(ctxt, "You need to enable this permission to use this App",
                    //                                new DialogInterface.OnClickListener() {
                    //                                    @Override
                    //                                    public void onClick(DialogInterface dialog, int which) {
                    //
                    //                                    }
                    //                                });
                    //                    }

                    permissionsNeeded.add(allPermission[i])
                }
            }

            if (permissionsNeeded.size > 0) {
                if (!justcheck) {
                    ActivityCompat.requestPermissions((ctxt as Activity?)!!, permissionsNeeded.toTypedArray(), 1000)
                } else {
                    return false
                }
            } else {
                return true
            }
        }
        return true
    }

    fun hasPermission(ctxt: Context?, permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctxt != null) {
            val hasWriteContactsPermission = ContextCompat.checkSelfPermission(ctxt, permission)
            return hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED
        }
        return true
    }

    private fun showMessageOKCancel(ctxt: Context, message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(ctxt)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
    }

    fun onRequestPermissionsResult(context: Context, requestCode: Int, permissions: Array<String>, grantResults: IntArray): Boolean {
        when (requestCode) {
            1000 -> for (grantResult in grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    //   ViewUtils.showToast(context, DastanApp.getInstance().getString(R.string.need_permission_msg));
                    //showPermissionDialog(context);
                    return false
                }
            }
            else -> return true
        }
        return true
    }

    fun askPermission(ctxt: Context?, permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctxt != null) {
            val hasWriteContactsPermission = ctxt.checkSelfPermission(permission)
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                //open dialog to inform user for permission if requires
                (ctxt as Activity).requestPermissions(arrayOf(permission),
                    REQUEST_CODE_ASK_PERMISSIONS)
                false
            } else {
                true
            }
        } else {
            true
        }
    }

    fun askPermission(ctxt: Context?, permission: String, requestCode: Int): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctxt != null) {
            val hasWriteContactsPermission = ctxt.checkSelfPermission(permission)
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                //open dialog to inform user for permission if requires
                (ctxt as Activity).requestPermissions(arrayOf(permission),
                    requestCode)
                false
            } else {
                true
            }
        } else {
            true
        }
    }

    fun askCallPermission(ctxt: Context?, phone_no: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctxt != null) {
            val hasWriteContactsPermission = ctxt.checkSelfPermission(Manifest.permission.CALL_PHONE)
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                //open dialog to inform user for permission if requires
                (ctxt as Activity).requestPermissions(arrayOf(Manifest.permission.CALL_PHONE),
                        REQUEST_CODE_ASK_PERMISSIONS)
                return
            } else {
                CommonUtils.makePhoneCall(ctxt, phone_no)
            }
        } else {
            CommonUtils.makePhoneCall(ctxt!!, phone_no)
        }
    }
}
