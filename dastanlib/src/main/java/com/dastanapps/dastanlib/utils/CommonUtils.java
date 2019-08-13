package com.dastanapps.dastanlib.utils;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.dastanapps.dastanlib.DastanApp;
import com.dastanapps.dastanlib.R;
import com.dastanapps.dastanlib.log.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static androidx.core.app.ActivityCompat.startActivityForResult;

/**
 * *@author : Dastan Iqbal
 *
 * @email : ask2iqbal@gmail.com
 */
public class CommonUtils {
    private static final String TAG = CommonUtils.class.getSimpleName();
    public static final int ACTIVITY_RESULT_SMS_CODE = 200;
    public static final int CAMERA_REQUEST_CODE_VEDIO = 201;
    public static final int PICK_VIDEO = 203;
    public static final int PICK_AUDIO = 204;
    private static Drawable drawable;

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getUUIDUsingTMgr() {
        final TelephonyManager tm = (TelephonyManager) DastanApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "";
        androidId = "";

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }

    public static String decodeBase64(String base64Str) {
        try {
            byte[] data = Base64.decode(base64Str, Base64.DEFAULT);
            return new String(data, Charset.defaultCharset());
        } catch (Exception e) {
            return null;
        }
    }

    public static String decodeBase64NoPadding(String base64Str) {
        try {
            byte[] data = Base64.decode(base64Str, Base64.NO_PADDING);
            return new String(data, Charset.defaultCharset());
        } catch (Exception e) {
            return null;
        }
    }

    public static String encodeBase64NoPadding(byte[] str) {
        try {
            byte[] data = Base64.encode(str, Base64.NO_PADDING);
            return new String(data, Charset.defaultCharset());
        } catch (Exception e) {
            return null;
        }
    }

    @RequiresPermission(Manifest.permission.GET_ACCOUNTS)
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

    public static boolean ValidateDecimalNumber(String number) {
        Pattern emailPattern = Pattern.compile("^(-)?\\d*(\\.\\d*)?$");
        return emailPattern.matcher(number).matches();
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

    public static void rateApp(Context ctxt, String appLink) {
        try {
            Uri uri = Uri.parse(appLink);
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

    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (v != null)
            imm.showSoftInput(v, 0);
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
     * @param context
     * @param shareTitle
     * @param filepath
     */
    public static void shareVideoIntent(Context context, String shareTitle, String filepath, boolean isVideo) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType(isVideo ? "video/*" : "audio/*");
        File fileToShare = new File(filepath);
        Uri uri = Uri.fromFile(fileToShare);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(sendIntent, shareTitle));
    }

    /**
     * used to share the link using existing app in device
     *
     * @param context
     * @param shareText
     * @param shareTitle
     * @param filepath
     */
    public static void shareVideoIntent(Context context, String shareTitle, String filepath, String shareText, boolean isVideo) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType(isVideo ? "video/*" : "audio/*");
        File fileToShare = new File(filepath);
        Uri uri = Uri.fromFile(fileToShare);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        if (shareText != null) {
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        }
        context.startActivity(Intent.createChooser(sendIntent, shareTitle));
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


    public static void openSpecialAccessSettings(Context ctxt) {
        Intent smsIntent = new Intent();
        smsIntent.setAction(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
        smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
        ((Activity) ctxt).startActivity(smsIntent);
    }

    public static void captureVideoIntent(Context ctxt) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        //takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        if (takeVideoIntent.resolveActivity(ctxt.getPackageManager()) != null) {
            ((Activity) ctxt).startActivityForResult(takeVideoIntent,
                    CAMERA_REQUEST_CODE_VEDIO);
        }
    }

    public static void pickVideoIntent(Context ctxt) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);//MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        ((Activity) ctxt).startActivityForResult(intent, PICK_VIDEO);
    }

    public static void pickAudioIntent(Context ctxt) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        ((Activity) ctxt).startActivityForResult(intent, PICK_AUDIO);
    }

    public static void pickAudioIntent(Context ctxt, int reqCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        ((Activity) ctxt).startActivityForResult(intent, reqCode);
    }


    /***
     * @param to
     * @param msg
     * @param sendPI
     * @param deliveredPI {@hide}
     */
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

    public static void openNotification(Context ctxt, String title, String desc,
                                        InputStream largeIconStream,
                                        InputStream bigPicStream, boolean isCancelable) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        ctxt,
                        0,
                        DastanApp.getInstance().getNotficationIntent(),
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctxt)
                        .setSmallIcon(DastanApp.getInstance().getSmallIcon())
                        .setContentTitle(title)
                        .setAutoCancel(isCancelable)
                        .setOngoing(false)
                        .setColor(DastanApp.getInstance().getNotificationColor())
                        .setPriority(NotificationCompat.PRIORITY_MAX)
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
        // mBuilder.setColor(ctxt.getResources().getColor(R.color.colorPrimary));
        //    }

        NotificationManager notificationManager = (NotificationManager) ctxt.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1001, mBuilder.build());

    }

    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public static String readFromRawRes(int rawId) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = DastanApp.getInstance().getResources().openRawResource(rawId);
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

    public static void playVideo(Context ctxt, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        i.setDataAndType(Uri.parse(url), "video/*");
        ctxt.startActivity(i);
    }

    public static void addAppShortcut(Intent shortCutintent, String name, int icon) {

        removeAppShortcut(shortCutintent, name);

        shortCutintent.setAction(Intent.ACTION_MAIN);
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutintent);

        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(DastanApp.getInstance(),
                        icon));

        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        DastanApp.getInstance().sendBroadcast(intent);

    }

    public static void removeAppShortcut(Intent shortCutintent, String name) {

        shortCutintent.setAction(Intent.ACTION_MAIN);

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutintent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        intent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        DastanApp.getInstance().sendBroadcast(intent);


    }

    public static void setLockScreenFlags(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }

    public static void setFullScreenHideNavBar(Activity activity) {
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; // hide nav bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags |= View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        activity.getWindow().getDecorView().setSystemUiVisibility(flags);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }

    public static void changeBackIconColor(Context ctxt, int colorAccent) {
        final Drawable upArrow = ContextCompat.getDrawable(ctxt, androidx.appcompat.appcompat.R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(ctxt, colorAccent), PorterDuff.Mode.SRC_ATOP);
        ((AppCompatActivity) ctxt).getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }


    public static Drawable getDrawable(@DrawableRes int resId) {
        return new BitmapDrawable(DastanApp.getInstance().getResources(),
                BitmapFactory.decodeResource(DastanApp.getInstance().getResources(), resId));
    }

    public static void unzip(InputStream inputStream, String dest) {
        dest += "/";
        dirChecker(dest, "");
        byte[] buffer = new byte[1024 * 10];
        try {
            ZipInputStream zin = new ZipInputStream(inputStream);
            ZipEntry ze = null;

            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                    dirChecker(dest, ze.getName());
                } else {
                    File f = new File(dest + ze.getName());
                    if (!f.exists()) {
                        Log.v(TAG, "Unzipping " + ze.getName());
                        FileOutputStream fout = new FileOutputStream(dest + ze.getName());
                        int count;
                        while ((count = zin.read(buffer)) != -1) {
                            fout.write(buffer, 0, count);
                        }
                        zin.closeEntry();
                        fout.close();
                    }
                }

            }
            zin.close();
        } catch (Exception e) {
            Log.e(TAG, "unzip", e);
        }

    }

    private static void dirChecker(String dir, String dest) {
        File f = new File(dir + dest);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }

    public static void openWebLinks(Context context, String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static void openAppOrWeb(Context context, String appUrl, String webUrl) {
        try {
            Intent appIntent = new Intent(Intent.ACTION_VIEW);
            appIntent.setData(Uri.parse(appUrl));
            context.startActivity(appIntent);
        } catch (Exception ex) {
            openWebLinks(context, webUrl);
        }
    }

    public static String escapeMetaCharacters(String inputString) {
        final String[] metaCharacters = {"\\", "^", "$", "{", "}", "[", "]", "(", ")","*", "+", "?", "|", "<", ">", "-", "&"};
        String outputString = "";
        for (int i = 0; i < metaCharacters.length; i++) {
            if (inputString.contains(metaCharacters[i])) {
                outputString = inputString.replace(metaCharacters[i], "\\" + metaCharacters[i]);
                inputString = outputString;
            }
        }
        return outputString;
    }

    public static String replaceMetaCharacters(String inputString,String character) {
        final String[] metaCharacters = {"\\", "^", "$", "{", "}", "[", "]", "(", ")","*", "+", "?", "|", "<", ">", "-", "&"};
        String outputString = "";
        for (int i = 0; i < metaCharacters.length; i++) {
            if (inputString.contains(metaCharacters[i])) {
                outputString = inputString.replace(metaCharacters[i], character);
                inputString = outputString;
            }
        }
        return outputString;
    }

    public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
        return iterable == null ? Collections.<T>emptyList() : iterable;
    }


    public static void sharingVideoToSocialMedia(Context context, String application, String shareTitle, String filepath, boolean isVideo) {

        sharingVideoToSocialMediaWithText(context, application, shareTitle, null, filepath, isVideo);
    }

    public static void sharingVideoToSocialMediaWithText(Context context, String application, String shareTitle, String shareText, String filepath, boolean isVideo) {

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType(isVideo ? "video/*" : "audio/*");
        File fileToShare = new File(filepath);
        Uri uri = Uri.fromFile(fileToShare);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        if (shareText != null) {
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        }
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        boolean installed = checkAppInstall(application);
        if (installed) {
            try {
                sendIntent.setPackage(application);
                context.startActivity(sendIntent);
            } catch (Exception e) {
                shareVideoIntent(context, shareTitle, filepath, shareText, isVideo);
            }
        } else {
            shareVideoIntent(context, shareTitle, filepath, shareText, isVideo);
        }
    }


    public static boolean checkAppInstall(String uri) {
        PackageManager pm = DastanApp.getInstance().getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return packageInfo != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static int getRandomNumber(int max, int min) {
        Random rn = new Random();
        int range = max - min + 1;
        return rn.nextInt(range) + min;
    }

    public static void screenBrightness(double newBrightnessValue, Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        float newBrightness = (float) newBrightnessValue;
        lp.screenBrightness = newBrightness / (float) 255;
        activity.getWindow().setAttributes(lp);
    }

    public static void changeBrightnessMode(Activity activity) {
        try {
            int brightnessMode = Settings.System.getInt(activity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                Settings.System.putInt(activity.getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }

        } catch (Exception e) {
            // do something useful
        }
    }

    public static void runInThread(Runnable runnable) {
        new Thread(runnable).start();
    }
}
