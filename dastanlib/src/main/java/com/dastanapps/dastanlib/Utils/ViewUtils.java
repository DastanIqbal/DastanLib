package com.dastanapps.dastanlib.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.dastanapps.dastanlib.R;


/**
 * *@author : Dastan Iqbal
 *
 * @email : iqbal.ahmed@mebelkart.com
 */
public class ViewUtils {

    private static final String TAG = ViewUtils.class.getSimpleName();
    private static ProgressDialog progressDialog;
    private static ProgressDialog progressDialogNotCancellable;
    private static Snackbar snack = null;

    private static Toast mToast = null;

    /**
     * @param ctxt
     */
    public static void showProgressDialog(Context ctxt) {
        if (ctxt != null && (progressDialog == null || !progressDialog.isShowing())) {
            progressDialog = new ProgressDialog(ctxt);
            // progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading");
            progressDialog.setIndeterminateDrawable(ViewUtils.getDrawable((Activity) ctxt, R.drawable.progressbar_circle));
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(true);

            progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    hideProgressDialog();
//                    progressDialog.dismiss();
                }
            });

            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    progressDialog.dismiss();
                }
            });
        }
    }

    public static boolean isProgressDialog() {
        if (progressDialog != null)
            return progressDialog.isShowing();
        return false;
    }

    public static void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static void showProgressDialogNotCancellable(Context ctxt) {
        if (ctxt != null && (progressDialog == null || !progressDialog.isShowing())) {
            progressDialog = new ProgressDialog(ctxt);
            // progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading");
            progressDialog.setIndeterminateDrawable(ViewUtils.getDrawable((Activity) ctxt, R.drawable.progressbar_circle));
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);

            progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    hideProgressDialog();
//                    progressDialog.dismiss();
                }
            });

            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    progressDialog.dismiss();
                }
            });
        }
    }

    public static void hideProgressDialogNotCancellable() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    public static void showToast(Context ctxt, String msg) {
        if (mToast != null)
            mToast.cancel();
        mToast = Toast.makeText(ctxt, msg, Toast.LENGTH_LONG);
        mToast.show();
    }

    public static void showSnack(String msg, String actionStr, View snackView, View.OnClickListener listener) {
        //with action listener
        if (snack != null)
            snack.dismiss();
        snack = Snackbar.make(snackView, msg, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snack.setAction(actionStr, listener);
        snack.setDuration(Snackbar.LENGTH_LONG);
        snack.show();
    }

    public static void showSnack(String msg, String actionStr, View snackView, View.OnClickListener listener, String tagValue) {
        //with action listener
        if (snack != null)
            snack.dismiss();
        snack = Snackbar.make(snackView, msg, Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout view = (Snackbar.SnackbarLayout) snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        view.findViewById(R.id.snackbar_action).setTag(R.id.handleMultipleTap, tagValue);
        snack.setAction(actionStr, listener);
        snack.setDuration(Snackbar.LENGTH_LONG);
        snack.show();
    }

    public static void showSnackDown(String msg, String actionStr, View snackView, View.OnClickListener listener) {
        //with action listener
        Snackbar snack = Snackbar.make(snackView, msg, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.BOTTOM;
        view.setLayoutParams(params);
        snack.setAction(actionStr, listener);
        snack.setDuration(Snackbar.LENGTH_INDEFINITE);
        snack.show();
    }

    public static void showSnack(String msg, View snackView) {
        Snackbar snack = Snackbar.make(snackView, msg, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snack.show();
    }

    public static View inflateLayout(Context ctxt, int resId) {
        return LayoutInflater.from(ctxt).inflate(resId, null);
    }
    public static View inflateLayout(Context ctxt, int resId, ViewGroup parent) {
        return LayoutInflater.from(ctxt).inflate(resId,parent,false);
    }

    public static Dialog getMKDialog(Context ctxt, int reslayout) {
        Dialog d = new Dialog(ctxt);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawableResource(R.color.transparent);
        d.setContentView(reslayout);
        d.setCanceledOnTouchOutside(false);
        return d;
    }

    public static Dialog getMKDialogOK(Context ctxt, String title, String msg,
                                       String positiveText, String negativeText, DialogInterface.OnClickListener okInterface,
                                       DialogInterface.OnClickListener cancelInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctxt);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveText, okInterface);
        builder.setNegativeButton(negativeText, cancelInterface);
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    public static Dialog getMKDialogOK(Context ctxt, String title, String msg,
                                       String positiveText, DialogInterface.OnClickListener okInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctxt);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveText, okInterface);
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }


    /**
     * used to support all versions of OS
     *
     * @param ctxt
     * @param res
     * @return
     */
    public static Drawable getDrawable(Activity ctxt, int res) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ctxt.getResources().getDrawable(res, ctxt.getTheme());
        } else {
            return ctxt.getResources().getDrawable(res);
        }
    }
}
