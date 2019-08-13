package com.dastanapps.dastanlib.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.dastanapps.dastanlib.DastanLibApp
import kotlinx.android.synthetic.main.activity_log.*
import java.io.File
import java.util.*

/**
 * Created by dastaniqbal on 14/09/2018.
 * dastanIqbal@marvelmedia.com
 * 14/09/2018 5:56
 */

class LoggerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        val adapter = LogFileAdapter(getLogFiles())
        recyclerView.adapter = adapter
    }

    private fun getLogFiles(): ArrayList<FileItemB> {
        pb.visibility = View.VISIBLE
        val logFolder = DastanLibApp.INSTANCE.externalCacheDir.absolutePath + "/logs/"
        val file = File(logFolder)
        if (!file.exists()) file.mkdirs()
        val fileList = ArrayList<FileItemB>()
        try {
            file.listFiles { _, p1 -> p1.endsWith(".txt") }.forEach {
                fileList.add(FileItemB(it.name, it.absolutePath))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        pb.visibility = View.GONE
        Collections.sort(fileList, Collections.reverseOrder(FilterComparator()))
        return fileList

    }
}

