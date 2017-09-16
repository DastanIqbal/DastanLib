package com.dastanapps.dastanlib.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dastanapps.dastanlib.DastanApp;
import com.dastanapps.dastanlib.R;
import com.dastanapps.dastanlib.log.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.UUID;
import java.util.regex.Pattern;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * *@author : Dastan Iqbal
 *
 * @email : ask2iqbal@gmail.com
 */
public class CommonUtils {
    public static final int ACTIVITY_RESULT_SMS_CODE = 200;
    private static final String TAG = CommonUtils.class.getSimpleName();

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

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

    public static void sendSMSIntent(Context ctxt, String to, String msg) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + to));
        smsIntent.putExtra("sms_body", msg);
        //smsIntent.putExtra("exit_on_sent", true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(ctxt);
//            smsIntent.setPackage(defaultSmsPackageName);
//        }
        ((Activity) ctxt).startActivityForResult(smsIntent, ACTIVITY_RESULT_SMS_CODE);
    }

    public static void sendSMS(String to, String msg, PendingIntent sendPI, PendingIntent deliveredPI) {
        //setPremiumMessageEnable(to, msg, sendPI, deliveredPI);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(to, null, msg, sendPI, deliveredPI);
    }

    public static void oepnDNDSettings(Context ctxt) {
        Intent smsIntent = new Intent();
        smsIntent.setAction(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
        smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
        ((Activity) ctxt).startActivity(smsIntent);
    }

    public static String checkNull(String str, String nullVal) {
        return null == str ? nullVal : str;
    }

    public static boolean fileExists(String path) {
        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + path;
        File imageFile = new File(mPath);
        return imageFile.exists();
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

    public static void openAppPermission() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Uri uri = Uri.fromParts("package", DastanApp.getInstance().getPackageName(), null);
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        DastanApp.getInstance().startActivity(intent);
    }

    public static void setRepeatSchedular(Class brclazz, long triggerMillis, long repeatMillis) {
        Intent triggerIntent = new Intent(DastanApp.getInstance(), brclazz);
        AlarmManager alarmManager = (AlarmManager) DastanApp.getInstance().getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(DastanApp.getInstance(), 20, triggerIntent, 0);
        // Set the alarm to start at 8:30 a.m.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 3);
//        calendar.set(Calendar.MINUTE, 36);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.AM_PM, Calendar.PM);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +
                triggerMillis, repeatMillis, alarmIntent);
    }

    public static void setSchedular(Class brclazz, long triggerMillis) {
        AlarmManager alarmManager = (AlarmManager) DastanApp.getInstance().getSystemService(Context.ALARM_SERVICE);
        Intent triggerIntent = new Intent(DastanApp.getInstance(), brclazz);
        triggerIntent.putExtra("action", "retrySMS");
        PendingIntent alarmIntent = PendingIntent.getBroadcast(DastanApp.getInstance(), 10, triggerIntent, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +
                triggerMillis, alarmIntent);
    }

    public static String readFromAssests(String fileName) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = DastanApp.getInstance().getResources().getAssets()
                    .open(fileName);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }

    public static void openNotification(Context ctxt, String title, String desc, int smallIcon, InputStream largeIconStream, InputStream bigPicStream, Intent resultIntent) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        ctxt,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctxt)
                        .setSmallIcon(smallIcon)
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentText(desc)
                        .setContentIntent(resultPendingIntent);

        if (largeIconStream != null) {
            Bitmap largBitmap = BitmapFactory.decodeStream(largeIconStream);
            if (largBitmap != null) {
                mBuilder.setLargeIcon(largBitmap);
            }
        }

        if (bigPicStream != null) {
            Bitmap bigBitmap = BitmapFactory.decodeStream(bigPicStream);

            if (bigBitmap != null) {
                mBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bigBitmap)
                        .setBigContentTitle(title)
                        .setSummaryText(desc));
            }
        }
        //     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        mBuilder.setColor(ctxt.getResources().getColor(R.color.colorPrimary));
        //    }

        NotificationManager notificationManager = (NotificationManager) ctxt.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1001, mBuilder.build());

    }

    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
