package com.dastanapps.dastanlib.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.dastanapps.dastanlib.DastanApp;
import com.dastanapps.dastanlib.SPConstant;
import com.dastanapps.dastanlib.analytics.DAnalytics;
import com.dastanapps.dastanlib.log.Logger;
import com.dastanapps.dastanlib.network.DRest;
import com.dastanapps.dastanlib.network.IRestRequest;
import com.dastanapps.dastanlib.receiver.SendBroadcast;
import com.dastanapps.dastanlib.utils.CommonUtils;
import com.dastanapps.dastanlib.utils.SPUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;
import com.dastanapps.dastanlib.firebase.NotificationB;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;



/**
 * Created by dastaniqbal on 15/12/2016.
 * 15/12/2016 10:07
 */

public class MarvelService extends Service {
    private static final String TAG = MarvelService.class.getSimpleName();
    private static final String STOP_SERVICE = "stop_service";
    private static MarvelService instance;

    public static MarvelService getInstance() {
        return instance;
    }

    public static void runService() {
        if (instance == null)
            DastanApp.getInstance().startService(new Intent(DastanApp.getInstance(), MarvelService.class));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d(TAG, "MarvelService Started");
        instance = this;
        checkGoogleService();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(gcmpusheventreceiver);
        LocalBroadcastManager.getInstance(this).registerReceiver(gcmpusheventreceiver, new IntentFilter(SendBroadcast.GCM_MESSAGE));
        return START_STICKY;
    }

    private BroadcastReceiver gcmpusheventreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String msg = intent.getStringExtra("msg");
                try {
                    JSONObject jsonObject = new JSONObject(msg);
                    NotificationB notificationB = new NotificationB();
                    if (jsonObject.has("title")) {
                        notificationB.setTitle(jsonObject.getString("title"));
                    }
                    if (jsonObject.has("content")) {
                        notificationB.setContent(jsonObject.getString("content"));
                    }
                    if (jsonObject.has("banner_url")) {
                        notificationB.setBanner_url(jsonObject.getString("banner_url"));
                    }
                    if (jsonObject.has("push_type")) {
                        notificationB.setPush_type(jsonObject.getString("push_type"));
                    }
                    openNotification(notificationB);
                } catch (Exception e) {
                    //e.printStackTrace();
                    DAnalytics.getInstance().sendLogs("crash", "push", "invalid json");
                }
            }
        }
    };

    private Thread thread = null;

    public void openNotification(final NotificationB notificationB) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream bigStream = null;
                    if (!TextUtils.isEmpty(notificationB.getBanner_url())) {
                        bigStream = new URL(notificationB.getBanner_url()).openStream();
                    }
//                    InputStream inputStream = null;
//                    if (!TextUtils.isEmpty(bigPicUrl))
//                        inputStream = new URL(largeIcon).openConnection().getInputStream();

                    CommonUtils.openNotification(MarvelService.this, notificationB.getTitle(),
                            notificationB.getContent(),
                            null, bigStream, false);
                    Bundle bundle = new Bundle();
                    bundle.putString("Notification", "Got Notification");
                    DAnalytics.getInstance().sendParams(TAG, bundle);
                    DAnalytics.getInstance().sendProperty(TAG, "Got Notification");
                    thread.interrupt();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(gcmpusheventreceiver);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        instance = null;
        runService();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }
    }

    public void stopService() {
        stopSelf();
        Logger.d(TAG, "MarvelService Stopped");
    }

    //Send FCM Token
    public void sendtoken() {
        Intent registrationComplete = new Intent(SendBroadcast.PUSH_TOKEN);
        try {
            // Get updated InstanceID token.
            if (!SPUtils.readBoolean(SPConstant.IS_PUSHED) && !TextUtils.isEmpty(SPUtils.readString(SPConstant.PUSH_URL_CONSTANT))) {
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                if (refreshedToken != null) {
                    Logger.d(TAG, "Refreshed token: " + refreshedToken);
                    registrationComplete.putExtra("gottoken", true);
                    registrationComplete.putExtra("token", refreshedToken);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
                    //Analytics
                    DAnalytics.getInstance().sendLogs("push", "token", "yes");
                    String callingUrl = String.format(SPUtils.readString(SPConstant.PUSH_URL_CONSTANT), refreshedToken);
                    DRest.sendGetRequest(callingUrl, 1001, new JSONObject(), new IRestRequest() {
                        @Override
                        public void onResponse(int reqID, String resp) {
                            if (resp != null && resp.contains("status")) {
                                if (resp.contains("ok")) {
                                    DAnalytics.getInstance().sendLogs("push", "token", "pushed success");
                                    SPUtils.writeBoolean(SPConstant.IS_PUSHED, true);
                                } else {
                                    DAnalytics.getInstance().sendLogs("push", "token", "pushed failed");
                                    SPUtils.writeBoolean(SPConstant.IS_PUSHED, false);
                                }
                            }
                        }

                        @Override
                        public void onError(int reqId, String error) {
                            DAnalytics.getInstance().sendLogs("push", "token", "pushed error");
                            SPUtils.writeBoolean(SPConstant.IS_PUSHED, false);
                        }
                    });
                } else {
                    //Analytics
                    DAnalytics.getInstance().sendLogs("push", "token", "null");
                }
            }
        } catch (Exception e) {
            DAnalytics.getInstance().sendLogs("push", "token", "no");
            Logger.d(TAG, "Failed to complete token refresh");
            registrationComplete.putExtra("gottoken", false);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        }
    }

    private void checkGoogleService() {
        //Checking play service is available or not
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (googleAPI.isUserResolvableError(resultCode)) {
                //Displaying message that play service is not installed
                //Toast.makeText(getApplicationContext(), "Google Play Service is not installed or enabled in this device!", Toast.LENGTH_LONG).show();
                Logger.w(TAG, "Google Play Service is not installed or enabled in this device!");

                //Analytics
                Bundle bundle = new Bundle();
                bundle.putString("GooglePlay", "NotInstalled");
                DAnalytics.getInstance().sendParams(TAG, bundle);
                DAnalytics.getInstance().sendProperty(TAG, "GooglePlayService not installed");
            } else {

                //Analytics
                Bundle bundle = new Bundle();
                bundle.putString("GooglePlay", "NotSupported");
                DAnalytics.getInstance().sendParams(TAG, bundle);
                DAnalytics.getInstance().sendProperty(TAG, "GooglePlayService not support");
                Logger.w(TAG, "This device does not support for Google Play Service!");
            }
            SendBroadcast.GooglePlayServiceMission(false);
            //If play service is available
        } else {
            sendtoken();
            SendBroadcast.GooglePlayServiceMission(true);
        }
    }
}