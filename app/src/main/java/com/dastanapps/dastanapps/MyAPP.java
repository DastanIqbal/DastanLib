package com.dastanapps.dastanapps;

import com.dastanapps.dastanlib.DastanApp;
import com.dastanapps.dastanlib.utils.ViewUtils;

/**
 * Created by dastaniqbal on 08/10/2017.
 * 08/10/2017 12:53
 */

public class MyAPP extends DastanApp {
    @Override
    public void onCreate() {
        super.onCreate();
        ViewUtils.showToast(this, "Initialized");
    }
}
