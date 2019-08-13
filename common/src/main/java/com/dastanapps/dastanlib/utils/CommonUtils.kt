package com.dastanapps.dastanlib.utils

import android.Manifest
import android.accounts.AccountManager
import android.app.*
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dastanapps.dastanlib.NotificationB
import com.dastanapps.dastanlib.log.Logger
import java.io.*
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern
import java.util.zip.ZipInputStream

/**
 * *@author : Dastan Iqbal
 *
 * @email : ask2iqbal@gmail.com
 */
object CommonUtils {
    private val TAG = CommonUtils::class.java.simpleName
    val ACTIVITY_RESULT_SMS_CODE = 200
    val CAMERA_REQUEST_CODE_VEDIO = 201
    val PICK_VIDEO = 203
    val PICK_AUDIO = 204
    val PICK_IMAGES = 205
    private val drawable: Drawable? = null

    val uuid: String
        get() = UUID.randomUUID().toString()

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun getUUIDUsingTMgr(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val tmDevice: String
        val tmSerial: String
        val androidId: String
        tmDevice = "" + tm.deviceId
        tmSerial = ""
        androidId = ""

        val deviceUuid = UUID(androidId.hashCode().toLong(), tmDevice.hashCode().toLong() shl 32 or tmSerial.hashCode().toLong())
        return deviceUuid.toString()
    }

    fun decodeBase64(base64Str: String): String? {
        try {
            val data = Base64.decode(base64Str, Base64.DEFAULT)
            try {
                return String(data, Charset.defaultCharset())
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

        } catch (e: Exception) {
            return null
        }

        return null
    }

    fun decodeBase64NoPadding(base64Str: String): String? {
        try {
            val data = Base64.decode(base64Str, Base64.NO_PADDING)
            try {
                return String(data, Charset.defaultCharset())
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

        } catch (e: Exception) {
            return null
        }

        return null
    }

    fun encodeBase64NoPadding(str: ByteArray): String? {
        try {
            try {
                val data = Base64.encode(str, Base64.NO_PADDING)
                return String(data, Charset.defaultCharset())
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

        } catch (e: Exception) {
            return null
        }

        return null
    }

    @RequiresPermission(Manifest.permission.GET_ACCOUNTS)
    fun getAccountName(ctxt: Context): String? {
        val emailPattern = Patterns.EMAIL_ADDRESS // API level 8+
        val accounts = AccountManager.get(ctxt).accounts
        for (account in accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                return account.name
            }
        }
        return null
    }

    fun ValidateDecimalNumber(number: String): Boolean {
        val emailPattern = Pattern.compile("^(-)?\\d*(\\.\\d*)?$")
        return emailPattern.matcher(number).matches()
    }

    fun getBlackFText(str: String): SpannableString {
        val blackSpannable = SpannableString(str)
        blackSpannable.setSpan(ForegroundColorSpan(Color.BLACK), 0, str.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        blackSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, str.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        return blackSpannable
    }

    fun makePhoneCall(ctxt: Context, phoneNo: String) {
        val intentCall = Intent(Intent.ACTION_CALL)
        intentCall.data = Uri.parse("tel:$phoneNo")
        ctxt.startActivity(intentCall)
    }

    fun rateApp(ctxt: Context, appLink: String) {
        try {
            val uri = Uri.parse(appLink)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            ctxt.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            ctxt.startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + ctxt.packageName)))
        }

    }

    /**
     * Used to disable the view for 'delayTime' time.
     *
     * @param context
     * @param view
     * @param delayTime
     */
    fun DelayButtonClick(context: Context?, view: View, delayTime: Int) {
        if (context != null && view.isEnabled) {
            view.isEnabled = false
            view.isClickable = false
            val handler = Handler()
            Logger.d(TAG, "disabled button")
            handler.postDelayed({
                view.isEnabled = true
                view.isClickable = true
                Logger.d(TAG, "enabled button")
            }, delayTime.toLong())
        } else {
            Logger.d(TAG, "context is null")
        }
    }

    fun hideKeyBoard(activity: Activity?) {
        if (activity != null) {
            val view = activity.currentFocus
            if (view != null) {
                val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm.isActive) {
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }
    }

    fun showKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val v = activity.currentFocus
        if (v != null)
            imm.showSoftInput(v, 0)
    }

    /**
     * used to share the link using existing app in device
     *
     * @param activity
     * @param shareText
     * @param shareLink
     */
    fun shareIntent(activity: Activity, shareText: String, shareLink: String, reqId: Int) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText + "\n" + shareLink)
        sendIntent.type = "text/plain"
        startActivityForResult(activity, sendIntent, reqId, Bundle())
    }

    /**
     * used to share the link using existing app in device
     *
     * @param context
     * @param shareTitle
     * @param filepath
     */
    fun shareVideoIntent(context: Context, shareTitle: String, filepath: String, isVideo: Boolean) {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = if (isVideo) "video/*" else "audio/*"
        val fileToShare = File(filepath)
        val uri = Uri.fromFile(fileToShare)
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(Intent.createChooser(sendIntent, shareTitle))
    }

    /**
     * used to share the link using existing app in device
     *
     * @param context
     * @param shareText
     * @param shareTitle
     * @param filepath
     */
    fun shareVideoIntent(context: Context, shareTitle: String, filepath: String, shareText: String?, isVideo: Boolean) {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = if (isVideo) "video/*" else "audio/*"
        val fileToShare = File(filepath)
        val uri = Uri.fromFile(fileToShare)
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
        if (shareText != null) {
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(sendIntent, shareTitle))
    }


    /**
     * used to share the link using existing app in device
     *
     * @param mFragment
     * @param shareText
     * @param shareLink
     */
    fun shareIntent(mFragment: androidx.fragment.app.Fragment, shareText: String, shareLink: String, reqId: Int) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText + "\n" + shareLink)
        sendIntent.type = "text/plain"
        mFragment.startActivityForResult(sendIntent, reqId)
    }

    fun sendSMSIntent(ctxt: Context, to: String, msg: String) {
        val smsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$to"))
        smsIntent.putExtra("sms_body", msg)
        //smsIntent.putExtra("exit_on_sent", true);
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(ctxt);
        //            smsIntent.setPackage(defaultSmsPackageName);
        //        }
        (ctxt as Activity).startActivityForResult(smsIntent, ACTIVITY_RESULT_SMS_CODE)
    }


    fun openSpecialAccessSettings(ctxt: Context) {
        val smsIntent = Intent()
        smsIntent.action = Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
        smsIntent.addCategory(Intent.CATEGORY_DEFAULT)
        (ctxt as Activity).startActivity(smsIntent)
    }

    fun captureVideoIntent(ctxt: Context) {
        val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        //takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        if (takeVideoIntent.resolveActivity(ctxt.packageManager) != null) {
            (ctxt as Activity).startActivityForResult(takeVideoIntent,
                    CAMERA_REQUEST_CODE_VEDIO)
        }
    }

    @RequiresPermission("android.Manifest.permission.READ_EXTERNAL_STORAGE")
    fun pickImageIntent(ctxt: Context, reqId: Int = PICK_IMAGES) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        (ctxt as Activity).startActivityForResult(intent, PICK_IMAGES)
    }

    @RequiresPermission("android.Manifest.permission.READ_EXTERNAL_STORAGE")
    fun pickVideoIntent(ctxt: Context, reqId: Int = PICK_VIDEO) {
        val intent = Intent(Intent.ACTION_PICK, null)//MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.type = "video/*"
        (ctxt as Activity).startActivityForResult(intent, PICK_VIDEO)
    }

    @RequiresPermission("android.Manifest.permission.READ_EXTERNAL_STORAGE")
    fun pickAudioIntent(ctxt: Context, reqId: Int = PICK_AUDIO) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        intent.type = "audio/*"
        (ctxt as Activity).startActivityForResult(intent, PICK_AUDIO)
    }

    /***
     * @param to
     * @param msg
     * @param sendPI
     * @param deliveredPI {@hide}
     */
    fun sendSMS(to: String, msg: String, sendPI: PendingIntent, deliveredPI: PendingIntent) {
        //setPremiumMessageEnable(to, msg, sendPI, deliveredPI);
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(to, null, msg, sendPI, deliveredPI)
    }

    fun oepnDNDSettings(ctxt: Context) {
        val smsIntent = Intent()
        smsIntent.action = Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
        smsIntent.addCategory(Intent.CATEGORY_DEFAULT)
        (ctxt as Activity).startActivity(smsIntent)
    }

    fun checkNull(str: String?, nullVal: String): String {
        return str ?: nullVal
    }

    fun fileExists(path: String): Boolean {
        val mPath = Environment.getExternalStorageDirectory().toString() + "/" + path
        val imageFile = File(mPath)
        return imageFile.exists()
    }

    fun bytes2String(sizeInBytes: Long): String {
        val SPACE_KB = 1024.0
        val SPACE_MB = 1024 * SPACE_KB
        val SPACE_GB = 1024 * SPACE_MB
        val SPACE_TB = 1024 * SPACE_GB
        val nf = DecimalFormat()
        nf.maximumFractionDigits = 2

        try {
            return if (sizeInBytes < SPACE_KB) {
                nf.format(sizeInBytes) + " Byte(s)"
            } else if (sizeInBytes < SPACE_MB) {
                nf.format(sizeInBytes / SPACE_KB) + " KB"
            } else if (sizeInBytes < SPACE_GB) {
                nf.format(sizeInBytes / SPACE_MB) + " MB"
            } else if (sizeInBytes < SPACE_TB) {
                nf.format(sizeInBytes / SPACE_GB) + " GB"
            } else {
                nf.format(sizeInBytes / SPACE_TB) + " TB"
            }
        } catch (e: Exception) {
            return sizeInBytes.toString() + " Byte(s)"
        }

    }

    fun openAppPermission(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        context.startActivity(intent)
    }

    fun setRepeatSchedular(context: Context, brclazz: Class<*>, triggerMillis: Long, repeatMillis: Long) {
        val triggerIntent = Intent(context, brclazz)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = PendingIntent.getBroadcast(context, 20, triggerIntent, 0)
        // Set the alarm to start at 8:30 a.m.
        //        Calendar calendar = Calendar.getInstance();
        //        calendar.setTimeInMillis(System.currentTimeMillis());
        //        calendar.set(Calendar.HOUR_OF_DAY, 3);
        //        calendar.set(Calendar.MINUTE, 36);
        //        calendar.set(Calendar.SECOND, 0);
        //        calendar.set(Calendar.AM_PM, Calendar.PM);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + triggerMillis, repeatMillis, alarmIntent)
    }

    fun setSchedular(context: Context, brclazz: Class<*>, triggerMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerIntent = Intent(context, brclazz)
        triggerIntent.putExtra("action", "retrySMS")
        val alarmIntent = PendingIntent.getBroadcast(context, 10, triggerIntent, 0)
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + triggerMillis, alarmIntent)
    }

    fun readFromAssests(context: Context, fileName: String): String {
        val returnString = StringBuilder()
        var fIn: InputStream? = null
        var isr: InputStreamReader? = null
        var input: BufferedReader? = null
        try {
            fIn = context.resources.assets
                    .open(fileName)
            isr = InputStreamReader(fIn)
            input = BufferedReader(isr)
            var line = input.readLine()
            while (line != null) {
                returnString.append(line)
                line = input.readLine()
            }
        } catch (e: Exception) {
            e.message
        } finally {
            try {
                isr?.close()
                fIn?.close()
                input?.close()
            } catch (e2: Exception) {
                e2.message
            }

        }
        return returnString.toString()
    }

    fun createNotificationChannel(nm: NotificationManager?, channelName: String?, channelId: String?): String? {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val chan = NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW)
            chan.setSound(null, null)
            chan.enableLights(false)
            chan.enableVibration(false)
            chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            nm?.createNotificationChannel(chan)
        }
        return channelId
    }

    fun openNotification(ctxt: Context, title: String, desc: String,
                         largeIconStream: InputStream?,
                         bigPicStream: InputStream?, isCancelable: Boolean) {
        var largBitmap: Bitmap? = null
        var bigBitmap: Bitmap? = null
        if (largeIconStream != null) {
            largBitmap = BitmapFactory.decodeStream(largeIconStream)
        }

        if (bigPicStream != null) {
            bigBitmap = BitmapFactory.decodeStream(bigPicStream)
        }
        val notificationB = NotificationB()
                .cancelable(isCancelable)
                .title(title)
                .desc(desc)
        largBitmap?.run { notificationB.largeBmp(this) }
        bigBitmap?.run { notificationB.bigBmp(this) }
        openNotification2(ctxt, notificationB)
    }

    fun getNotificationB(ctxt: Context, title: String, desc: String,
                          largBitmap: Bitmap?,
                          bigBitmap: Bitmap?, isCancelable: Boolean): NotificationB {
        val notificationB = NotificationB()
                .cancelable(isCancelable)
                .title(title)
                .desc(desc)
                .channelName("Notification")
                .channelId(ctxt.getString(R.string.fcm_default_channel))
        largBitmap?.run { notificationB.largeBmp(this) }
        bigBitmap?.run { notificationB.bigBmp(this) }
        return notificationB
    }

    fun openNotification2(ctxt: Context, notificationB: NotificationB) {
        val notificationManager = ctxt.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var channelId = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel(notificationManager, notificationB.channelName, notificationB.channelId)
                    ?: ""
        }
        val mBuilder = NotificationCompat.Builder(ctxt, channelId)
                .setSmallIcon(notificationB.smallIcon)
                .setContentTitle(notificationB.title)
                .setAutoCancel(notificationB.cancelable)
                .setOngoing(!notificationB.cancelable)
                .setColor(notificationB.color)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(defaultSoundUri)
                .setContentText(notificationB.desc)

        if (notificationB.pendingIntent != null) {
            val resultPendingIntent = PendingIntent.getActivity(ctxt,
                    0, notificationB.pendingIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            mBuilder.setContentIntent(resultPendingIntent)
        }

        if (notificationB.largeBmp != null) {
            mBuilder.setLargeIcon(notificationB.largeBmp)
        }

        if (notificationB.bigBmp != null) {
            mBuilder.setStyle(NotificationCompat.BigPictureStyle()
                    .bigPicture(notificationB.bigBmp)
                    .setBigContentTitle(notificationB.title)
                    .setSummaryText(notificationB.desc))
        }
        //     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        // mBuilder.setColor(ctxt.getResources().getColor(R.color.colorPrimary));
        //    }

        notificationManager.notify(notificationB.id, mBuilder.build())
    }

    fun cancelNotificaiton(context: Context, id: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(id)
    }

    fun getFileName(path: String): String {
        return path.substring(path.lastIndexOf("/") + 1)
    }

    fun readFromRawRes(context: Context, rawId: Int): String {
        val returnString = StringBuilder()
        var fIn: InputStream? = null
        var isr: InputStreamReader? = null
        var input: BufferedReader? = null
        try {
            fIn = context.resources.openRawResource(rawId)
            isr = InputStreamReader(fIn)
            input = BufferedReader(isr)
            var line = input.readLine()
            while (line != null) {
                returnString.append(line)
                line = input.readLine()
            }
        } catch (e: Exception) {
            e.message
        } finally {
            try {
                isr?.close()
                fIn?.close()
                input?.close()
            } catch (e2: Exception) {
                e2.message
            }

        }
        return returnString.toString()
    }

    fun playVideo(ctxt: Context, url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        i.setDataAndType(Uri.parse(url), "video/*")
        ctxt.startActivity(i)
    }

    fun setLockScreenFlags(activity: Activity) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
    }

    fun setFullScreenHideNavBar(activity: Activity) {
        var flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            flags = flags or (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_FULLSCREEN) // hide status bar;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags = flags or (View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }

        activity.window.decorView.systemUiVisibility = flags
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
    }

    fun changeBackIconColor(ctxt: Context, colorAccent: Int) {
        val upArrow = ContextCompat.getDrawable(ctxt, R.drawable.abc_ic_ab_back_material)
        upArrow!!.setColorFilter(ContextCompat.getColor(ctxt, colorAccent), PorterDuff.Mode.SRC_ATOP)
        (ctxt as AppCompatActivity).supportActionBar!!.setHomeAsUpIndicator(upArrow)
    }


    fun getDrawable(context: Context, @DrawableRes resId: Int): Drawable {
        return BitmapDrawable(context.resources,
                BitmapFactory.decodeResource(context.resources, resId))
    }

    fun unzip(inputStream: InputStream, dest: String) {
        var dest = dest
        dest += "/"
        dirChecker(dest, "")
        val buffer = ByteArray(1024 * 10)
        try {
            val zin = ZipInputStream(inputStream)
            var ze = zin.nextEntry

            while (ze != null) {
                if (ze.isDirectory) {
                    dirChecker(dest, ze.name)
                } else {
                    val f = File(dest + ze.name)
                    if (!f.exists()) {
                        Log.v(TAG, "Unzipping " + ze.name)
                        val fout = FileOutputStream(dest + ze.name)
                        var count = zin.read(buffer)
                        while (count != -1) {
                            fout.write(buffer, 0, count)
                            count = zin.read(buffer)
                        }
                        zin.closeEntry()
                        fout.close()
                    }
                }
                ze = zin.nextEntry
            }
            zin.close()
        } catch (e: Exception) {
            Log.e(TAG, "unzip", e)
        }

    }

    private fun dirChecker(dir: String, dest: String) {
        val f = File(dir + dest)
        if (!f.isDirectory) {
            f.mkdirs()
        }
    }

    fun openWebLinks(context: Context, url: String) {
        var url = url
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://$url"
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

    fun openAppOrWeb(context: Context, appUrl: String, webUrl: String) {
        try {
            val appIntent = Intent(Intent.ACTION_VIEW)
            appIntent.data = Uri.parse(appUrl)
            context.startActivity(appIntent)
        } catch (ex: Exception) {
            openWebLinks(context, webUrl)
        }

    }

    fun escapeMetaCharacters(inputString: String): String {
        var inputString = inputString
        val metaCharacters = arrayOf("\\", "^", "$", "{", "}", "[", "]", "(", ")", "*", "+", "?", "|", "<", ">", "-", "&")
        var outputString = ""
        for (i in metaCharacters.indices) {
            if (inputString.contains(metaCharacters[i])) {
                outputString = inputString.replace(metaCharacters[i], "\\" + metaCharacters[i])
                inputString = outputString
            }
        }
        return outputString
    }

    fun replaceMetaCharacters(inputString: String, character: String): String {
        var inputString = inputString
        val metaCharacters = arrayOf("\\", "^", "$", "{", "}", "[", "]", "(", ")", "*", "+", "?", "|", "<", ">", "-", "&")
        var outputString = ""
        for (i in metaCharacters.indices) {
            if (inputString.contains(metaCharacters[i])) {
                outputString = inputString.replace(metaCharacters[i], character)
                inputString = outputString
            }
        }
        return outputString
    }

    fun <T> emptyIfNull(iterable: Iterable<T>?): Iterable<T> {
        return iterable ?: emptyList()
    }


    fun sharingVideoToSocialMedia(context: Context, application: String, shareTitle: String, filepath: String, isVideo: Boolean) {

        sharingVideoToSocialMediaWithText(context, application, shareTitle, null, filepath, isVideo)
    }

    fun sharingVideoToSocialMediaWithText(context: Context, application: String, shareTitle: String, shareText: String?, filepath: String, isVideo: Boolean) {

        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = if (isVideo) "video/*" else "audio/*"
        val fileToShare = File(filepath)
        val uri = Uri.fromFile(fileToShare)
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
        if (shareText != null) {
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        }
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val installed = AppUtils.checkAppInstall(context, application)
        if (installed) {
            try {
                sendIntent.setPackage(application)
                context.startActivity(sendIntent)
            } catch (e: Exception) {
                shareVideoIntent(context, shareTitle, filepath, shareText, isVideo)
            }

        } else {
            shareVideoIntent(context, shareTitle, filepath, shareText, isVideo)
        }
    }

    fun getRandomNumber(max: Int, min: Int): Int {
        val rn = Random()
        val range = max - min + 1
        return rn.nextInt(range) + min
    }

    fun screenBrightness(newBrightnessValue: Double, activity: Activity) {
        val lp = activity.window.attributes
        val newBrightness = newBrightnessValue.toFloat()
        lp.screenBrightness = newBrightness / 255.toFloat()
        activity.window.attributes = lp
    }

    fun changeBrightnessMode(activity: Activity) {
        try {
            val brightnessMode = Settings.System.getInt(activity.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE)
            if (brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                Settings.System.putInt(activity.contentResolver,
                        Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)
            }

        } catch (e: Exception) {
            // do something useful
        }

    }

    fun runInThread(runnable: Runnable) {
        Thread(runnable).start()
    }

    fun sendEmailAttachment(context: Context, content: String, filePath: String, to: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Report Issue")
        intent.putExtra(Intent.EXTRA_TEXT, DeviceUtils.getMinimalDeviceInfo() + content)
        val uri = Uri.fromFile(File(filePath))
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        val chooserIntent = Intent.createChooser(intent, "Report Issue")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }

    fun openAppSettings(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }
}
