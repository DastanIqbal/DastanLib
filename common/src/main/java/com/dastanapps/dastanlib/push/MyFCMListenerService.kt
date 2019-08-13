package com.dastanapps.dastanlib.push

/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.dastanapps.dastanlib.log.Logger
import com.dastanapps.dastanlib.receiver.SendBroadcast
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFCMListenerService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        Logger.d(TAG, "From: " + remoteMessage!!.from!!)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Logger.d(TAG, "Message data payload: " + remoteMessage.data)
            val message = remoteMessage.data["data"]
            message?.run {
                SendBroadcast.PushMsg(this)
            }
        }
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        Logger.d(TAG, "New Token")
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            it?.token?.run {
                val refreshedToken = this
                Logger.d(TAG, "Firebase Token is $refreshedToken")
                SendBroadcast.PushToken(this)
            } ?: Logger.d(TAG, "Firebase Token is null")
        }

    }

    companion object {

        private val TAG = "MyFCMListenerService"
    }
}

