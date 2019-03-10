package com.dastanapps.dastanlib.ads;

import com.startapp.android.publish.ads.nativead.NativeAdDetails;

import java.util.ArrayList;

/**
 * Created by dastaniqbal on 17/03/2017.
 * 17/03/2017 5:03
 */

public interface IStartApp extends IMarvelAds{
    void onReceived(ArrayList<NativeAdDetails> ads);

    void onFailed();
}
