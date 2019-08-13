package com.dastanapps.dastanlib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import com.dastanapps.dastanlib.log.Logger;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OTPSMSReceiver extends BroadcastReceiver {
    public static final String RECEIVE_CONSTANT = "received_sms";
    public static final String INTENT_SMS_RECEIVER = "intent_receive_sms";
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
                String otp = extractOTP(message);
                if (!TextUtils.isEmpty(otp)) {
                    Intent localSmsReciver = new Intent(INTENT_SMS_RECEIVER);
                    localSmsReciver.putExtra(RECEIVE_CONSTANT, otp);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(localSmsReciver);
                    Logger.d(TAG, phone + " : " + otp);
                }
            }
        }
    }

    private String extractOTP(String message) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(message);
        ArrayList<String> mList = new ArrayList<>();
        while (m.find()) {
            Logger.d(TAG, m.group());
            mList.add(m.group());
        }
        if (mList.size() > 0) {
            return mList.get(0);
        } else {
            return "";
        }
    }
}