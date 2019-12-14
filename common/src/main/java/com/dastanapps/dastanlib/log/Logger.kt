package com.dastanapps.dastanlib.log

import android.util.Log

import com.dastanapps.dastanlib.DastanLibApp
import com.dastanapps.dastanlib.utils.DateTimeUtils

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException


/**
 * Created by Iqbal Ahmed on 10/8/2015.
 *
 *
 * 10/8/2015 11:07
 */
object Logger {
    private val prefix = "dastanlibs"
    private val logger = org.slf4j.LoggerFactory.getLogger("DLogger")

    var LOG_FILE_NAME = "Dlog.txt"

    private fun checkNull(log: String?): String {
        return log ?: "Null"
    }

    fun i(tag: String, log: String?) {
        logger.info(tag + log)
        if (!DastanLibApp.INSTANCE.isRelease()) {
            Log.d(prefix, tag + ":" + checkNull(log))
            appendLog("$tag : $log")
        }
    }

    fun w(tag: String, log: String?) {
        logger.warn("$tag:$log")
        if (!DastanLibApp.INSTANCE.isRelease()) {
            Log.w(prefix, tag + ":" + checkNull(log))
            appendLog("$tag : $log")
        }
    }

    fun d(tag: String, log: String?) {
        logger.debug("$tag:$log")
        if (!DastanLibApp.INSTANCE.isRelease()) {
            Log.d(prefix, tag + ":" + checkNull(log))
            appendLog("$tag : $log")
        }
    }

    fun e(tag: String, log: String?) {
        logger.error("$tag:$log")
        if (!DastanLibApp.INSTANCE.isRelease()) {
            Log.e(prefix, tag + ":" + checkNull(log))
            appendLog("$tag : $log")
        }
    }

    fun v(tag: String, log: String?) {
        logger.trace("$tag:$log")
        if (!DastanLibApp.INSTANCE.isRelease()) {
            Log.v(prefix, "$tag:$log")
            appendLog("$tag : $log")
        }
    }

    fun onlyDebug(log1: String?) {
        var log:String?=null
        if (log1 == null) {
            log = "null"
        }
        logger.debug(log)
        if (!DastanLibApp.INSTANCE.isRelease()) {
            d(prefix, checkNull(log))
            appendLog(prefix + log)
        }
    }

    private fun appendLog(text: String?) {
        if (!DastanLibApp.INSTANCE.isRelease()) {
            val file = File(DastanLibApp.INSTANCE.externalCacheDir!!.toString() + "/logs/")
            if (!file.exists())
                file.mkdir()
            val logFile = File(file, LOG_FILE_NAME)
            try {
                if (!logFile.exists()) {
                    logFile.createNewFile()
                }
                //BufferedWriter for performance, true to set append to file flag
                val buf = BufferedWriter(FileWriter(logFile, true))
                buf.append(DateTimeUtils.currentTimeStampInDateTime("yyMMdd:HH:mm:ss:S") + " " + text)
                buf.newLine()
                buf.close()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        }
    }

    fun generateNewFile() {
        if (!DastanLibApp.INSTANCE.isRelease()) {
            LOG_FILE_NAME = DateTimeUtils.currentTimeStampInDateTime("yyyy-MM-dd_HH_mm_ss") + ".txt"
        }
    }
}
