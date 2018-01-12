package com.jaykallen.appwifi.utils;
// Created by Jay Kallen on 11/11/2017. 

import android.content.Context;
import android.content.IntentFilter;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.view.View;

import com.jaykallen.appwifi.models.WifiModel;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class WifiHelper {
    private static WifiManager wifiMgr;

    public static ArrayList <WifiModel> getWifi(Context context) {
        wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(context.WIFI_SERVICE);
        WifiReceiver wifiReceiver = new WifiReceiver();
        context.registerReceiver(wifiReceiver, new IntentFilter(android.net.wifi.WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiMgr.startScan();
        List wifiList = wifiMgr.getScanResults();

        ArrayList <WifiModel> wifiArrayList = new ArrayList<>();
        for (int i=0; i<wifiList.size(); i++) {
            wifiArrayList.add(parseLine("" + wifiList.get(i).toString()));
        }
        return wifiArrayList;
    }

    private static WifiModel parseLine(String dataLine) {
        // SSID: HP-Print-e0-LaserJet 400 color, BSSID: d8:5d:e2:2d:8b:e0, capabilities: , level: -69, frequency: 2462, timestamp: 958900558000
        WifiModel wifi = new WifiModel();
        String var;
        String data;
        while (dataLine.contains(":")) {
            int colon = dataLine.indexOf(":");
            int comma = dataLine.indexOf(",");
            var = dataLine.substring(0, colon);
            if (comma>0) {
                data = dataLine.substring(colon + 1, comma);
                dataLine = dataLine.substring(comma + 1, dataLine.length());
            } else {
                data = dataLine.substring(colon + 1, dataLine.length());
                dataLine = "";
            }
            wifi = populateModel(wifi, var, trimmer(data));
            wifi.setSelected(false);
        }
        return wifi;
    }

    private static WifiModel populateModel(WifiModel wifi, String var, String data) {
        switch (var.trim()) {
            case "BSSID" : wifi.setBssid(data); break;
            case "SSID" : wifi.setSsid(data); break;
            case "capabilities" : wifi.setCapabilities(data); break;
            case "level" : wifi.setLevel(data); break;
            case "frequency" : wifi.setFrequency(data); break;
            case "timestamp" : wifi.setTimestamp(data); break;
        }
        return wifi;
    }

    public static boolean WifiConnect (Context context, String ssid, String password) {
        Timber.d("Connecting to wifi network: \"" + ssid + "\" with password \"" + password + "\"");
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = "\"" + ssid + "\"";
        wifiConfig.preSharedKey = "\"" + password + "\"";
        //wifiConfig.SSID = "\"procatmtg\"";
        //wifiConfig.preSharedKey = "\"stockright\"";
        wifiConfig.status = WifiConfiguration.Status.ENABLED;
        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wifiMgr.setWifiEnabled(true);
        wifiMgr.addNetwork(wifiConfig);
        try {
            int netId = wifiMgr.addNetwork(wifiConfig);
            if (netId == -1) {
                netId = getExistingNetworkId(context, ssid);
            }
            wifiMgr.disconnect();
            wifiMgr.enableNetwork(netId, true);
            wifiMgr.reconnect();
            return true;
        } catch (Exception e) {
            Timber.e(e.getMessage());
            return false;
        }
    }

    private static int getExistingNetworkId(Context context, String ssid) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        if (configuredNetworks != null) {
            for (WifiConfiguration existingConfig : configuredNetworks) {
                if (existingConfig.SSID.equals(ssid)) {
                    return existingConfig.networkId;
                }
            }
        }
        return -1;
    }

    private static String trimmer(String string) {
        if (string != null) {
            return string.trim();
        } else {
            return "";
        }
    }


}
