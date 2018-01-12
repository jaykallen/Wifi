package com.jaykallen.appwifi.utils;

// Created by Jay Kallen on 10/10/2017:

import android.app.Application;

import timber.log.Timber;

public class StartApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree() {
            @Override
            protected void log(int priority, String tag, String message, Throwable t) {
                super.log(priority, tag + "Jay", message, t);
            }
        });
    }

}
