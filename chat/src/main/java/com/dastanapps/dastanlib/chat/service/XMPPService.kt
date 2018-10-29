package com.dastanapps.dastanlib.chat.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import android.support.v4.content.LocalBroadcastManager
import android.text.TextUtils
import android.util.Log
import com.dastanapps.dastanlib.DastanChatApp
import com.dastanapps.dastanlib.chat.DChatXMPP
import com.dastanapps.dastanlib.chat.receiver.DChatStates
import com.dastanapps.dastanlib.chat.receiver.SendBroadcast
import org.jetbrains.annotations.Nullable
import java.util.concurrent.Executors


/**
 * Created by DastanIqbal on 5/10/16.
 */

class XMPPService : Service() {
    private var mWakeLock: PowerManager.WakeLock? = null
    private val executor = Executors.newSingleThreadScheduledExecutor()

    private val mStopReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            stopSelf()
        }
    }

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "SDK DChatXMPP Service Intialized")
        // create the global wake lock
        val pwr = getSystemService(Context.POWER_SERVICE) as PowerManager
        mWakeLock = pwr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG)
        mWakeLock!!.setReferenceCounted(false)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        start()
        xmppService = this
        LocalBroadcastManager.getInstance(this).registerReceiver(mStopReceiver, IntentFilter(ACTION_STOP))
        return Service.START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)
        XMPPService.runService(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mStopReceiver)
        stop()
        // release the wakelock
        mWakeLock!!.release()
    }

    private fun connect() {
        dastanXmppConfig = getDastanXmppConfig()
        if (!dastanXmppConfig!!.isConnected) {
            dastanXmppConfig!!.configXMPP()
            dastanXmppConfig!!.connect()
        } else {
            if (dastanXmppConfig!!.isAuthenticated)
                SendBroadcast.sendConnectionState(DChatStates.ConnectionState.AUTHENTICATED)
        }
    }

    fun start() {
        Log.d(TAG, " Service Start() function called.")
        executor.execute {
            if (!TextUtils.isEmpty(DastanChatApp.INSTANCE.chatUser) && !TextUtils.isEmpty(DastanChatApp.INSTANCE.chatPwd)) {
                connect()
            }
        }
    }

    fun stop() {
        Log.d(TAG, "stop()")
        executor.execute {
            if (dastanXmppConfig != null) {
                dastanXmppConfig!!.disconnect()
            }
        }

    }

    companion object {
        private val ACTION_STOP = "stop_xmpp_service"
        private val TAG = XMPPService::class.java.simpleName
        private var dastanXmppConfig: DChatXMPP? = null
        private var xmppService: XMPPService? = null

        fun getXMPPService(): XMPPService {
            if (xmppService == null) {
                throw NullPointerException("XMPPService not intialized")
            }
            return xmppService as XMPPService
        }

        fun getDastanXmppConfig(): DChatXMPP {
            if (dastanXmppConfig == null) {
                dastanXmppConfig = DChatXMPP()
            }
            return dastanXmppConfig as DChatXMPP
        }

        fun runService(context: Context) {
            // if (xmppService == null) {
            val intent = Intent(context, XMPPService::class.java)
            context.startService(intent)
            //        } else {
            //            DChatXMPP.initEngine();
            //        }
        }

        fun stopService(context: Context) {
            val broadcastManager = LocalBroadcastManager.getInstance(context)
            broadcastManager.sendBroadcast(Intent(ACTION_STOP))
        }

        private fun getStartIntent(context: Context): Intent {
            return Intent(context, XMPPService::class.java)
        }

        fun setWakeupAlarm(context: Context) {
            val am = context
                    .getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // start message center pending intent
            val pi = PendingIntent.getService(context
                    .applicationContext, 0, getStartIntent(context),
                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_ONE_SHOT)

            am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 1000 * 60 * 1, pi)
        }
    }

}
