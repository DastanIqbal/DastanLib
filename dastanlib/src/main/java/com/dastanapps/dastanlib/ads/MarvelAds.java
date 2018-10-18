package com.dastanapps.dastanlib.ads;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dastanapps.dastanlib.DastanApp;
import com.dastanapps.dastanlib.utils.SPUtils;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.videoeditor.kruso.lib.ads.IFBNativeAds;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by dastaniqbal on 17/03/2017.
 * 17/03/2017 4:45
 */

public class MarvelAds {
    private AppLovin appLovin;
    private StartAppAds startAppAd;
    private FacebookAudience facebookAudience = new FacebookAudience();
    private ViewGroup hscrollContatiner;
    private Object interstialAd;

    private StartAppAds getStartAppAds() {
        if (startAppAd == null) {
            startAppAd = new StartAppAds();
        }
        startAppAd.init();
        return startAppAd;
    }

    private AppLovin getAppLovin() {
        if (appLovin == null)
            appLovin = new AppLovin();
        appLovin.init();
        return appLovin;
    }

    public void showBannerAds(ViewGroup bannerContainer) {
        String adNetwork = SPUtils.readString("ads");
        if (adNetwork.equals("fb")) {
            facebookAudience.showBanner(bannerContainer);
        }
    }

    public MarvelAds loadInterstialAds(Context activity, String tag) {
        String adNetwork = SPUtils.readString("ads");
        if (adNetwork.equals("startapp")) {
            getStartAppAds().setInterstialAds(new StartAppAd(DastanApp.getInstance()));
        } else if (adNetwork.equals("applovin")) {
            // getAppLovin().initInterstialAds(activity);
        } else if (adNetwork.equals("fb")) {
            //Don't forget to setInterstialFBListener to call showInterstialAds method later
            // call before loadInterstialAds
            facebookAudience.loadInterstitial(tag);
        }
        return this;
    }

    public MarvelAds setInterstialFBListener(String tag) {
        setAdsListener(new IFBNativeAds() {

            @Override
            public void addDisplayed() {

            }

            @Override
            public void adDismissed() {
            }

            @Override
            public void adError(@NotNull String error) {
            }

            @Override
            public void adLoaded(@NotNull Object adLoaded) {
                interstialAd = adLoaded;
            }
        }, tag);
        return this;
    }

    public void showInterstialAds() {
        String adNetwork = SPUtils.readString("ads");
        if (adNetwork.equals("fb") && interstialAd != null) {
            ((InterstitialAd) interstialAd).show();
        }
    }

    public void showExitsAds(Activity activity, String tag) {
        String adNetwork = SPUtils.readString("ads");
        if (adNetwork.equals("startapp")) {
            getStartAppAds().backPressed(new StartAppAd(activity));
        } else if (adNetwork.equals("applovin")) {
            getAppLovin().showInterstialAds(activity);
        } else if (adNetwork.equals("fb")) {
            //Don't forget to setInterstialFBListener to call showInterstialAds method later
            // call before loadInterstialAds
            facebookAudience.loadInterstitial(tag);
        }
    }

    public void setNativeAds(String tag) {
        String adNetwork = SPUtils.readString("ads");
        if (adNetwork.equals("startapp")) {
            getStartAppAds().setupNativeAds();
        } else if (adNetwork.equals("applovin")) {
            //getAppLovin().;
            getStartAppAds().setupNativeAds();
        } else if (adNetwork.equals("fb")) {
            facebookAudience.showNativeAd(tag);
        }
    }

    public void setupNativeAds(int num) {
        String adNetwork = SPUtils.readString("ads");
        if (adNetwork.equals("startapp")) {
            getStartAppAds().setupNativeAds(num);
        } else if (adNetwork.equals("applovin")) {
            getStartAppAds().setupNativeAds(num);
        } else if (adNetwork.equals("fb")) {
            if (hscrollContatiner == null)
                throw new RuntimeException("You have to set hScrollContainer");
            facebookAudience.showNativeAdInHScroll(num, hscrollContatiner);
        }
    }

    public void setAdsListener(IMarvelAds marvelAdsListener, String tag) {
        if (marvelAdsListener instanceof IStartApp) {
            getStartAppAds().setIStartApp((IStartApp) marvelAdsListener, tag);
        }
        if (marvelAdsListener instanceof IAppLovin) {
            getAppLovin().setIAppLovin((IAppLovin) marvelAdsListener, tag);
        }
        if (marvelAdsListener instanceof IFBNativeAds) {
            facebookAudience.setNativeAdsListener((IFBNativeAds) marvelAdsListener, tag);
        }
    }

    public void onDestory(String tag) {
//        getStartAppAds().onDestroy(tag);
//        getAppLovin().onDestroy(tag);
        facebookAudience.onDestroy(tag);
    }

    public void setHscrollContatiner(ViewGroup hscrollContatiner) {
        this.hscrollContatiner = hscrollContatiner;
    }

    public void setFBAdIcon(NativeAd nativeAd, ImageView imageView) {
        facebookAudience.setAdIcon(nativeAd, imageView);
    }

    public void setFBMedia(@NotNull NativeAd adLoaded, @Nullable MediaView nativeAdMedia) {
        facebookAudience.setFBMedia(adLoaded, nativeAdMedia);
    }

    public void setFBAdChoices(@NotNull NativeAd adLoaded, ViewGroup adChoicesContaincer) {
        facebookAudience.enableAdChoiceIcon(adLoaded, adChoicesContaincer);
    }

    public void fbCallToAction(NativeAd nativeAd, List<? extends View> clickableLists, ViewGroup clickContainer) {
        facebookAudience.setCallToAction(nativeAd, clickableLists, clickContainer);
    }

    public void fbCallToAction(NativeAd nativeAd, View clickContainer) {
        facebookAudience.setCallToAction(nativeAd, clickContainer);
    }
}
