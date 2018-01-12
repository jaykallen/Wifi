package com.jaykallen.appwifi.utils;

// Created by Jay Kallen on 11/7/2017:

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.greenrobot.eventbus.EventBus;

import timber.log.Timber;

public class WifiReceiver extends BroadcastReceiver {
    static boolean isConnected;
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            Timber.d("Wifi has been connected");
            if (!isConnected) {
                isConnected = true;
                Timber.d("Notify the UI that the Wifi has been connected *********** ");
                EventBus.getDefault().post(new EventbusTrigger("Wifi is Connected"));
            }
        } else {
            Timber.d("Wifi is not connected");
        }

    }
}
