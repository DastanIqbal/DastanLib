package com.dastanapps.dastanapps

import com.dastanapps.dastanlib.DastanLibApp
import com.dastanapps.dastanlib.utils.ViewUtils

/**
 * Created by dastaniqbal on 08/10/2017.
 * 08/10/2017 12:53
 */

class MyAPP : DastanLibApp() {
    override fun onCreate() {
        super.onCreate()
        ViewUtils.showToast(this, "Initialized")
    }
}
