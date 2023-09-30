package com.example.newvpn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Settings extends AppCompatActivity {
    ImageView bt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        bt_back=findViewById(R.id.bt_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, VpnConnect.class));
            }
        });
    }

    public void premium(View view) {
        startActivity(new Intent(Settings.this, PremiumActivity.class));


    }
}