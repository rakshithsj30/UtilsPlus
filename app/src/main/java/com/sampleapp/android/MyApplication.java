package com.sampleapp.android;

import android.app.Application;
import android.content.res.Configuration;

import com.github.rrsystems.utilsplus.android.UtilsPlus;

/**
 * Created by Clear Bits on 4/21/2017.
 */
public class MyApplication extends Application {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        UtilsPlus.initialize(getApplicationContext(), "myapp");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}