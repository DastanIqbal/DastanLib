package com.dastanapps.dastanlib

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.dastanapps.dastanlib.log.Logger



/**
 * Created by dastaniqbal on 27/10/2018.
 * 27/10/2018 3:08
 */
open class DastanLibApp : Application() {
    private val TAG = this::class.java.simpleName
    val supportEmail: String? = null
    val commonConfiguration = CommonConfiguration()

    companion object {
        lateinit var INSTANCE: DastanLibApp
    }

//    val appModule = module {
//
//    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Logger.onlyDebug("Lib Initialized")
     //   startKoin(this, listOf(appModule))
    }

    fun isRelease(): Boolean {
        return commonConfiguration.devMode == "release"
    }

}