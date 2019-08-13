package com.dastanapps.dastanlib.chat.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.dastanapps.dastanlib.DastanApp;
import com.dastanapps.dastanlib.chat.DChatXMPP;
import com.dastanapps.dastanlib.chat.receiver.DChatStates;
import com.dastanapps.dastanlib.chat.receiver.SendBroadcast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


/**
 * Created by DastanIqbal on 5/10/16.
 */

public class XMPPService extends Service {
    private static final String ACTION_STOP = "stop_xmpp_service";
    private static final String TAG = XMPPService.class.getSimpleName();
    private static DChatXMPP dastanXmppConfig;
    private PowerManager.WakeLock mWakeLock = null;
    private static XMPPService xmppService;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public static XMPPService getXMPPService() {
        if (xmppService == null) {
            throw new NullPointerException("XMPPService not intialized");
        }
        return xmppService;
    }

    public static DChatXMPP getDastanXmppConfig() {
        if (dastanXmppConfig == null) {
            dastanXmppConfig = new DChatXMPP();
        }
        return dastanXmppConfig;
    }

    public static void runService(Context context) {
        // if (xmppService == null) {
        Intent intent = new Intent(context, XMPPService.class);
        context.startService(intent);
//        } else {
//            DChatXMPP.initEngine();
//        }
    }

    public static void stopService(Context context) {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
        broadcastManager.sendBroadcast(new Intent(ACTION_STOP));
    }

    private final BroadcastReceiver mStopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopSelf();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "SDK DChatXMPP Service Intialized");
        // create the global wake lock
        PowerManager pwr = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pwr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        mWakeLock.setReferenceCounted(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        start();
        xmppService = this;
        LocalBroadcastManager.getInstance(this).registerReceiver(mStopReceiver, new IntentFilter(ACTION_STOP));
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        XMPPService.runService(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mStopReceiver);
        stop();
        // release the wakelock
        mWakeLock.release();
    }

    private void connect() {
        dastanXmppConfig = getDastanXmppConfig();
        if (!dastanXmppConfig.isConnected()) {
            dastanXmppConfig.configXMPP();
            dastanXmppConfig.connect();
        } else {
            if (dastanXmppConfig.isAuthenticated())
                SendBroadcast.sendConnectionState(DChatStates.ConnectionState.AUTHENTICATED);
        }
    }

    public void start() {
        Log.d(TAG, " Service Start() function called.");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(DastanApp.getChatUser()) &&
                        !TextUtils.isEmpty(DastanApp.getChatPwd())) {
                    connect();
                }
            }
        });
    }

    public void stop() {
        Log.d(TAG, "stop()");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (dastanXmppConfig != null) {
                    dastanXmppConfig.disconnect();
                }
            }
        });

    }

    private static Intent getStartIntent(Context context) {
        return new Intent(context, XMPPService.class);
    }

    public static void setWakeupAlarm(Context context) {
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        // start message center pending intent
        PendingIntent pi = PendingIntent.getService(context
                        .getApplicationContext(), 0, getStartIntent(context),
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 1000 * 60 * 1, pi);
    }

}
