package com.dastanapps.dastanlib.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dastanapps.dastanlib.log.Logger;
import com.dastanapps.dastanlib.R;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * *@author : Dastan Iqbal
 *
 * @email : iqbal.ahmed@mebelkart.com
 */
public class CommonUtils {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 100;
    private static final String TAG = CommonUtils.class.getSimpleName();

    public static String decodeBase64(String base64Str) {
        try {
            byte[] data = Base64.decode(base64Str, Base64.DEFAULT);
            try {
                return new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static String decodeBase64NoPadding(String base64Str) {
        try {
            byte[] data = Base64.decode(base64Str, Base64.NO_PADDING);
            try {
                return new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static String encodeBase64NoPadding(byte[] str) {
        try {
            try {
                byte[] data = Base64.encode(str, Base64.NO_PADDING);
                return new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static String getAccountName(Context ctxt) {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(ctxt).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                return possibleEmail;
            }
        }
        return null;
    }

    public static SpannableString getBlackFText(String str) {
        SpannableString blackSpannable = new SpannableString(str);
        blackSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        blackSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, str.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return blackSpannable;
    }

    public static void makePhoneCall(Context ctxt, String phoneNo) {
        Intent intentCall = new Intent(Intent.ACTION_CALL);
        intentCall.setData(Uri.parse("tel:" + phoneNo));
        ctxt.startActivity(intentCall);
    }

    public static void askCallPermission(Context ctxt,String phone_no) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctxt!=null) {
            int hasWriteContactsPermission = ctxt.checkSelfPermission(android.Manifest.permission.CALL_PHONE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                //open dialog to inform user for permission if requires
                ((Activity) ctxt).requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            } else {
                makePhoneCall(ctxt, phone_no);
            }
        } else {
            makePhoneCall(ctxt, phone_no);
        }
    }

    public static void rateApp(Context ctxt) {
        try {
            Uri uri = Uri.parse(String.format(ctxt.getResources().getString(R.string.app_link), ctxt.getPackageName().toString()));
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            ctxt.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            ctxt.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + ctxt.getPackageName())));
        }
    }

    /**
     * Used to disable the view for 'delayTime' time.
     *
     * @param context
     * @param view
     * @param delayTime
     */
    public static void DelayButtonClick(Context context, final View view, int delayTime) {
        if (context != null && view.isEnabled()) {
            view.setEnabled(false);
            view.setClickable(false);
            Handler handler = new Handler();
            Logger.d(TAG, "disabled button");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setEnabled(true);
                    view.setClickable(true);
                    Logger.d(TAG, "enabled button");
                }
            }, delayTime);
        } else {
            Logger.d(TAG, "context is null");
        }
    }

    public static void hideKeyBoard(Activity activity) {
        if (activity != null) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
    }

    /**
     * used to share the link using existing app in device
     *
     * @param activity
     * @param shareText
     * @param shareLink
     */
    public static void shareIntent(Activity activity, String shareText, String shareLink, int reqId) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText + "\n" + shareLink);
        sendIntent.setType("text/plain");
        startActivityForResult(activity, sendIntent, reqId, new Bundle());
    }


    /**
     * used to share the link using existing app in device
     *
     * @param mFragment
     * @param shareText
     * @param shareLink
     */
    public static void shareIntent(Fragment mFragment, String shareText, String shareLink, int reqId) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText + "\n" + shareLink);
        sendIntent.setType("text/plain");
        mFragment.startActivityForResult(sendIntent, reqId);
    }

}
