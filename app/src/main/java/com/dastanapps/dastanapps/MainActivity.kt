package com.dastanapps.dastanapps

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dastanapps.dastanapps.ads.admob.AdmobAdsTestActivity
import com.dastanapps.dastanlib.ui.LoggerActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listview.setOnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> startActivity(Intent(this@MainActivity, LoggerActivity::class.java))
                1 -> startActivity(Intent(this@MainActivity, AdmobAdsTestActivity::class.java))
                2 -> startActivity(Intent(this@MainActivity, CommonActivity::class.java))
            }
        }
    }
}
