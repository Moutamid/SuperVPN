package com.example.newvpn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int DELAY_MILLIS = 1700;
        new Handler(Looper.getMainLooper()).postDelayed(() ->{
//            if(AdManager.getInstance(this).getAdMobInterstitialAd().isLoaded() ||
//                    AdManager.getInstance(this).getFBInterstitial().isAdLoaded()) {
//                startActivity(new Intent(SplashActivity.this, VpnConnect.class));
//                finish();
//                AdManager.getInstance(this).showInterstitialAd(this);
//            } else {
                startActivity(new Intent(SplashActivity.this, VpnConnect.class));
                finish();
//            }
        }, DELAY_MILLIS);

    }
}