package com.dastanapps.dastanlib.ads;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.startapp.android.publish.adsCommon.StartAppAd;

public class BaseAdsActivity extends AppCompatActivity {
    protected StartAppAd startAppAd = new StartAppAd(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        startAppAd.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        startAppAd.onSaveInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAppAd.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        startAppAd.onPause();
    }

}
