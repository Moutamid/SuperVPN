package com.example.newvpn;

import android.app.Application;

import com.facebook.ads.AudienceNetworkAds;

public class Vpn extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AudienceNetworkAds.initialize(this);
    }
}
