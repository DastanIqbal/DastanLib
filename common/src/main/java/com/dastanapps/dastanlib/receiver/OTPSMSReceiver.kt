package com.dastanapps.dastanlib.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.telephony.SmsMessage
import android.text.TextUtils
import com.dastanapps.dastanlib.log.Logger
import java.util.*
import java.util.regex.Pattern

class OTPSMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val intentExtras = intent.extras

        if (intentExtras != null) {
            /* Get Messages */
            val sms = intentExtras.get("pdus") as Array<Any>

            for (sm in sms) {
                /* Parse Each Message */
                val smsMessage = SmsMessage.createFromPdu(sm as ByteArray)

                val phone = smsMessage.originatingAddress
                val message = smsMessage.messageBody
                val otp = extractOTP(message)
                if (!TextUtils.isEmpty(otp)) {
                    val localSmsReciver = Intent(INTENT_SMS_RECEIVER)
                    localSmsReciver.putExtra(RECEIVE_CONSTANT, otp)
                    androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(context).sendBroadcast(localSmsReciver)
                    Logger.d(TAG, "$phone : $otp")
                }
            }
        }
    }

    private fun extractOTP(message: String): String {
        val p = Pattern.compile("\\d+")
        val m = p.matcher(message)
        val mList = ArrayList<String>()
        while (m.find()) {
            Logger.d(TAG, m.group())
            mList.add(m.group())
        }
        return if (mList.size > 0) {
            mList[0]
        } else {
            ""
        }
    }

    companion object {
        val RECEIVE_CONSTANT = "received_sms"
        val INTENT_SMS_RECEIVER = "intent_receive_sms"
        val TAG = OTPSMSReceiver::class.java.simpleName
    }
}