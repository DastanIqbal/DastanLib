package com.dastanapps.dastanlib.ads;

import com.applovin.nativeAds.AppLovinNativeAd;
import com.applovin.sdk.AppLovinAd;

import java.util.List;

/**
 * Created by dastaniqbal on 17/03/2017.
 * dastanIqbal@marvelmedia.com
 * 17/03/2017 12:49
 */

public interface IAppLovin extends IMarvelAds{

    void currentLoadedAd(AppLovinAd appLovinAd);

    void loadNativeAds(List<AppLovinNativeAd> appLovinNativeAdList);

    void onNativeAdImagesPrecached(int nativeAdsPos, AppLovinNativeAd appLovinNativeAd);
}
