package com.dastanapps.dastanlib

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.dastanapps.dastanlib.log.Logger
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module



/**
 * Created by dastaniqbal on 27/10/2018.
 * 27/10/2018 3:08
 */
open class DastanLibApp : Application() {
    private val TAG = this::class.java.simpleName
    var isRelease = false
    val supportEmail: String? = null

    companion object {
        lateinit var INSTANCE: DastanLibApp
    }

    val appModule = module {

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Logger.onlyDebug("Lib Initialized")
        startKoin(this, listOf(appModule))
    }
}