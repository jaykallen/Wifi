package com.jaykallen.appwifi.models;

// Created by Jay Kallen on 11/7/2017:  Designed to hold all the wifi parameters
// SSID: HP-Print-e0-LaserJet 400 color, BSSID: d8:5d:e2:2d:8b:e0, capabilities: , level: -69, frequency: 2462, timestamp: 958900558000

public class WifiModel {
    private static WifiModel mInstance = null;
    private String ssid;
    private String bssid;
    private String capabilities;
    private String level;
    private String frequency;
    private String timestamp;
    private boolean isSelected;

    public static WifiModel getInstance(){
        if(mInstance == null) {
            mInstance = new WifiModel();
        }
        return mInstance;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
