package com.dastanapps.dastanlib

import android.app.Application
import android.content.Intent
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module

/**
 * Created by dastaniqbal on 27/10/2018.
 * 27/10/2018 3:08
 */
class DastanLibApp : Application() {
    private val TAG = this::class.java.simpleName
    var isRelease = false
    var notficationIntent: Intent? = null
    var smallIcon: Int = -1
    var notificationColor: Int = -1

    companion object {
        lateinit var INSTANCE: DastanLibApp
    }

    val appModule = module {

    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        startKoin(this, listOf(appModule))
    }
}