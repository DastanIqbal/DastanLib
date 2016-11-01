package com.dastanapps.dastanapps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dastanapps.dastanlib.LibInit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new LibInit(this);
    }
}
