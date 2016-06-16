package com.adrian.foream.application;

import android.app.Application;

/**
 * Created by adrian on 16-6-8.
 */
public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication newInstance() {
        return instance;
    }
}
