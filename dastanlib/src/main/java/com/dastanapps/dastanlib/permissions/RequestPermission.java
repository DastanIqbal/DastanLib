package com.dastanapps.dastanlib.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.dastanapps.dastanlib.DastanApp;
import com.dastanapps.dastanlib.R;
import com.dastanapps.dastanlib.utils.CommonUtils;
import com.dastanapps.dastanlib.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dastan Iqbal on 10/18/2016.
 * author Iqbal Ahmed
 * emailId: ask2iqbal@gmail.com
 */

public class RequestPermission {
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 100;
//    private static String allPermission[] = {Manifest.permission.READ_SMS,
//            Manifest.permission.RECEIVE_SMS,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.SEND_SMS,
//            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.GET_ACCOUNTS};

    public static boolean checkPermission(final Context ctxt, boolean justcheck,String allPermission[]) {
        final List<String> permissionsNeeded = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctxt != null) {
            for (int i = 0; i < allPermission.length; i++) {
                int hasWriteContactsPermission = ContextCompat.checkSelfPermission(ctxt, allPermission[i]);
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

                    permissionsNeeded.add(allPermission[i]);
                }
            }

            if (permissionsNeeded.size() > 0) {
                if (!justcheck) {
                    ActivityCompat.requestPermissions((Activity) ctxt, permissionsNeeded.toArray(new String[permissionsNeeded.size()]), 1000);
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
        return true;
    }

    public static boolean hasPermission(Context ctxt, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctxt != null) {
            int hasWriteContactsPermission = ContextCompat.checkSelfPermission(ctxt, permission);
            if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private static void showMessageOKCancel(Context ctxt, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ctxt)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public static boolean onRequestPermissionsResult(Context context,int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1000:
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        ViewUtils.showToast(context, DastanApp.getInstance().getString(R.string.need_permission_msg));
                        //showPermissionDialog(context);
                        return false;
                    }
                }
                break;
            default:
                return true;
        }
        return true;
    }

    public static boolean askPermission(Context ctxt, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctxt != null) {
            int hasWriteContactsPermission = ctxt.checkSelfPermission(permission);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                //open dialog to inform user for permission if requires
                ((Activity) ctxt).requestPermissions(new String[]{permission},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean askPermission(Context ctxt, String permission, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctxt != null) {
            int hasWriteContactsPermission = ctxt.checkSelfPermission(permission);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                //open dialog to inform user for permission if requires
                ((Activity) ctxt).requestPermissions(new String[]{permission},
                        requestCode);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static void askCallPermission(Context ctxt, String phone_no) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctxt != null) {
            int hasWriteContactsPermission = ctxt.checkSelfPermission(Manifest.permission.CALL_PHONE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                //open dialog to inform user for permission if requires
                ((Activity) ctxt).requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            } else {
                CommonUtils.makePhoneCall(ctxt, phone_no);
            }
        } else {
            CommonUtils.makePhoneCall(ctxt, phone_no);
        }
    }

    private static void showPermissionDialog(Context context) {
        AlertDialog dialog = ViewUtils.getDDialogOK(context, "Permission",
                String.format(context.getString(R.string.need_permission_msg), "Permission"), "Permission", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        CommonUtils.openAppPermission();

//                        //Analytics
//                        Bundle bundle = new Bundle();
//                        bundle.putString("Permission", "Opened Permission Settings");
//                        MarvelAnalytics.getInstance().sendParams(TAG, bundle);
//                        MarvelAnalytics.getInstance().sendProperty(TAG, "Permission");

                    }
                }, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!dialog.isShowing())
                dialog.show();
        }
    }
}
