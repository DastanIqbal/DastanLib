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
 *//*


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

import com.dastanapps.dastanlib.Image.MkartImage;
import com.dastanapps.dastanlib.R;
import com.google.android.gms.gcm.GcmListenerService;

import static android.R.id.message;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    */
/**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     *//*

    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("data");

        */
/**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         *//*

        sendNotification(message);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    */
/**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     *//*

    private void sendNotification(Intent intent, String title, String msgTitle, String image_url) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 */
/* Request code *//*
, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bigBitmap = MkartImage.loadBitmap(this, image_url);
        Bitmap largBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setContentText(msgTitle)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        if (bigBitmap != null) {
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(bigBitmap)
                    .setBigContentTitle(title)
                    .setSummaryText(msgTitle));
        }

        if (bigBitmap != null) {
            notificationBuilder.setLargeIcon(largBitmap);
        }


        //     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        //    }


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 */
/* ID of notification *//*
, notificationBuilder.build());
    }

    public static int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_stat_transparent_notif : R.mipmap.ic_stat_launcher;
    }
}
*/
