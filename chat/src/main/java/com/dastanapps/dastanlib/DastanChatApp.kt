package com.dastanapps.dastanlib

/**
 * Created by dastaniqbal on 29/10/2018.
 * 29/10/2018 10:33
 */
class DastanChatApp : DastanLibApp() {
    private val TAG = this::class.java.simpleName
    var chatUser: String? = null
    var chatPwd: String? = null

    companion object {
        lateinit var INSTANCE: DastanChatApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        if (chatUser.isNullOrEmpty()) {
            RuntimeException("Chat User cannot be Null")
        }

        if (chatPwd.isNullOrEmpty()) {
            RuntimeException("Chat Password cannot be Null")
        }
    }
}