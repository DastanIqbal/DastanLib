package com.dastanapps.dastanapps

import com.dastanapps.dastanlib.DastanAdsApp
import com.dastanapps.dastanlib.utils.ViewUtils

/**
 * Created by dastaniqbal on 08/10/2017.
 * 08/10/2017 12:53
 */

class MyAPP : DastanAdsApp() {
    override fun onCreate() {
        super.onCreate()
        ViewUtils.showToast(this, "Initialized")

        //Admob
        adsConfiguration.adMobAppId = getString(R.string.admob_app_id)
        adsConfiguration.adMobRewardId = getString(R.string.admob_reward_ad)
        adsConfiguration.adMobBannerId = getString(R.string.admob_ads_banner)
        adsConfiguration.adMobInterstialAd = getString(R.string.admob_ads_interstial)

    }
}
