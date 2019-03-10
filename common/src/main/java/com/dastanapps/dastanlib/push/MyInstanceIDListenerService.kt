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

package com.dastanapps.dastanlib.push

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.dastanapps.dastanlib.receiver.SendBroadcast

import com.dastanapps.dastanlib.utils.R
import com.google.firebase.iid.FirebaseInstanceIdService

class MyInstanceIDListenerService : FirebaseInstanceIdService() {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    override fun onTokenRefresh() {
        SendBroadcast.PushToken()
    }

    companion object {

        private val TAG = "MyInstanceIDLS"
    }
    // [END refresh_token]
}
