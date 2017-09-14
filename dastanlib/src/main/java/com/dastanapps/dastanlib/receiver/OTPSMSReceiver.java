package com.dastanapps.dastanlib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;

import com.dastanapps.dastanlib.log.Logger;

/**
 * Created by Dastan Iqbal on 10/20/2016.
 * author Iqbal Ahmed
 * emailId: ask2iqbal@gmail.com
 */

public class OTPSMSReceiver extends BroadcastReceiver {
    public static final String OTP_INTENT_CONSTANT = "otp_sms";
    public static final String TAG = OTPSMSReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();

        if (intentExtras != null) {
            /* Get Messages */
            Object[] sms = (Object[]) intentExtras.get("pdus");

            assert sms != null;
            for (Object sm : sms) {
                /* Parse Each Message */
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sm);

                String phone = smsMessage.getOriginatingAddress();
                String message = smsMessage.getMessageBody();

                Intent localSmsReciver = new Intent(ReceiverFilter.OTP_SMS_RECEIVER);
                localSmsReciver.putExtra(OTP_INTENT_CONSTANT, message);
                LocalBroadcastManager.getInstance(context).sendBroadcast(localSmsReciver);
                Logger.d(TAG, phone + " : " + message);
            }
        }
    }
}
