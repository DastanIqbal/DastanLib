/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dastanapps.dastanlib.Push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.mebelkart.app.Activity.MainActivity;
import com.mebelkart.app.Activity.ProductDetailsA;
import com.mebelkart.app.Activity.ProductListA;
import com.mebelkart.app.AppConstant;
import com.mebelkart.app.Beans.PushB;
import com.mebelkart.app.Image.MkartImage;
import com.mebelkart.app.MkartApp;
import com.mebelkart.app.Network.RestResponse;
import com.mebelkart.app.R;
import com.mebelkart.app.Utils.MkUtils;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("data");

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        PushB pushB = RestResponse.paresePush(message);
        if (pushB != null) {
            MkartApp.getMkartAnalytics().getInstance().sendEvent("PushListener", "Got Push :" + pushB.getTitle() + " : " + pushB.getMsgTitle());
            Intent intent = null;
            if (pushB.getType().equals("category")) {
                intent = new Intent(this, ProductListA.class);
                intent.putExtra(AppConstant.CAT_ID, pushB.getId());
                intent.putExtra(AppConstant.CAT_NAME, pushB.getTitle());
            } else if (pushB.getType().equals("product")) {
                intent = new Intent(this, ProductDetailsA.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtra(AppConstant.PROD_TYPE, "featured");
                intent.putExtra(AppConstant.CAT_NAME, pushB.getTitle());
                intent.putExtra(AppConstant.PROD_DETAILS, pushB.getId());
            } else {
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Bitmap bigBitmap = MkartImage.loadBitmap(this, pushB.getImage_url());
            Bitmap largBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(MkUtils.getNotificationIcon())
                    .setContentTitle(pushB.getTitle())
                    .setContentText(pushB.getMsgTitle())
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            if (bigBitmap != null) {
                notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bigBitmap)
                        .setBigContentTitle(pushB.getTitle())
                        .setSummaryText(pushB.getMsgTitle()));
            }

            if (bigBitmap != null) {
                notificationBuilder.setLargeIcon(largBitmap);
            }


       //     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        //    }


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }
}
