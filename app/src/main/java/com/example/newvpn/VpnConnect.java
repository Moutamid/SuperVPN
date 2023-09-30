package com.example.newvpn;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.VpnService;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.ads.MediaView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.InterstitialAd;
import com.bumptech.glide.Glide;
import com.example.newvpn.interfaces.ChangeServer;
import com.example.newvpn.model.Server;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.material.navigation.NavigationView;
import com.jackandphantom.circularprogressbar.CircleProgressbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.blinkt.openvpn.OpenVpnApi;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.OpenVPNThread;
import de.blinkt.openvpn.core.VpnStatus;
import de.hdodenhof.circleimageview.CircleImageView;

public class VpnConnect extends AppCompatActivity implements ChangeServer {
    ImageView btconnect;
    CircleImageView serverflag;
    boolean vpnStart = false;
    private Server mserver;
    TextView logtv, tvtimer, namecountry;
    private LinearLayout country;
    private CheckInternetConnection connection;
    private OpenVPNThread vpnThread = new OpenVPNThread();
    private OpenVPNService vpnService = new OpenVPNService();
    private SharedPreference preference;
    LottieAnimationView connecting, disconnect, connected;
    TextView upload, download;
    ImageView menu;
    NavigationView drawer;
    ImageView settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpnconnect);
        logtv = findViewById(R.id.serverStatus);
        btconnect = findViewById(R.id.serverConnect);
        connected = findViewById(R.id.connected);
        namecountry = findViewById(R.id.elapse);
        disconnect = findViewById(R.id.disconnect);
        connecting = findViewById(R.id.connecting);
        upload = findViewById(R.id.upload);
        download = findViewById(R.id.download);
        country = findViewById(R.id.homeBtnChooseCountry);
        serverflag = findViewById(R.id.serverFlag);
        menu = findViewById(R.id.menu);
        drawer = findViewById(R.id.my_drawer_layout);
        settings = findViewById(R.id.settings1);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        tvtimer = findViewById(R.id.tvtimer);
        final InterstitialAd[] countryinterstitial = new InterstitialAd[1];
        final InterstitialAd[] fbinterstitial = new InterstitialAd[1];
        final com.google.android.gms.ads.InterstitialAd[] interstitialAdadmob = new com.google.android.gms.ads.InterstitialAd[1];
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawer.setVisibility(View.VISIBLE);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VpnConnect.this, Settings.class));
            }
        });
//        AdManager.getInstance(this).showBannerAd(this);
//        AdManager.getInstance(VpnConnect.this).loadNativeAd(this);


        initializeAll();
        Intent intent = getIntent();
        String a = intent.getStringExtra("activity");
        if (a != null) {
            int animationDuration = 10000; // 2500ms = 2,5s

            prepareVpn();
        } else {

            isServiceRunning();
            VpnStatus.initLogCache(this.getCacheDir());
            if (logtv.getText().equals("Connected")) {
                btconnect.setVisibility(View.INVISIBLE);
                connected.setVisibility(View.VISIBLE);
                disconnect.setVisibility(View.INVISIBLE);
                connecting.setVisibility(View.INVISIBLE);
            }
        }
        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(VpnConnect.this, CountryList.class);
                startActivity(intent1);

//                AdManager.getInstance(VpnConnect.this).showInterstitialAd(VpnConnect.this);

            }
        });
//        btconnect_txt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btconnect_txt.setVisibility(View.GONE);
//                disconnect_serverConnect_text.setVisibility(View.VISIBLE);
////                AdManager.getInstance(VpnConnect.this).showInterstitialAd(VpnConnect.this);
//                if (vpnStart) {
//                    confirmDisconnect();
//                } else {
//
//                    circleProgressbar.enabledTouch(false);
//                    circleProgressbar.setProgress(0);
//                    int animationDuration = 10000; // 2500ms = 2,5s
//                    circleProgressbar.setProgressWithAnimation(100, animationDuration);
//                    prepareVpn();
//                    circleProgressbar.setProgress(100);
//                }
//            }
//        });
//        disconnect_serverConnect_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                disconnect_serverConnect_text.setVisibility(View.GONE);
//                btconnect_txt.setVisibility(View.VISIBLE);
//
////                AdManager.getInstance(VpnConnect.this).showInterstitialAd(VpnConnect.this);
//                if (vpnStart) {
//                    confirmDisconnect();
//                } else {
//
//                    circleProgressbar.enabledTouch(false);
//                    circleProgressbar.setProgress(0);
//                    int animationDuration = 10000; // 2500ms = 2,5s
//                    circleProgressbar.setProgressWithAnimation(100, animationDuration);
//                    prepareVpn();
//                    circleProgressbar.setProgress(100);
//                }
//            }
//        });

    }


    private void initializeAll() {
        preference = new SharedPreference(this);
        mserver = preference.getServer();
        updateCurrentServerIcon(mserver.getFlagUrl());
        updateservername(mserver.getCountry());
        connection = new CheckInternetConnection();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("connectionState"));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            startVpn();
        } else {
            showToast("Permission Deny !! ");
        }
    }

    @Override
    public void onResume() {
        if (mserver == null) {
            mserver = preference.getServer();
            updateCurrentServerIcon(mserver.getFlagUrl());
            updateservername(mserver.getCountry());
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        if (mserver != null) {
            preference.saveServer(mserver);
        }

        super.onStop();
    }

    @Override
    public void newServer(Server server) {
        VpnConnect.this.mserver = server;
        Toast.makeText(this, "" + server.getCountry(), Toast.LENGTH_SHORT).show();
        updateCurrentServerIcon(server.getFlagUrl());
        if (vpnStart) {
            stopVpn();
        }

        prepareVpn();
    }

    public boolean getInternetStatus() {
        return connection.netCheck(this);
    }

    public void isServiceRunning() {
        setStatus(vpnService.getStatus());
    }

    public void startVpn() {
        try {
            InputStream conf = this.getAssets().open(mserver.getOvpn());
            InputStreamReader isr = new InputStreamReader(conf);
            BufferedReader br = new BufferedReader(isr);
            String config = "";
            String line;

            while (true) {
                line = br.readLine();
                if (line == null) break;
                config += line + "\n";
            }

            br.readLine();
            OpenVpnApi.startVpn(this, config, mserver.getCountry(), mserver.getOvpnUserName(), mserver.getOvpnUserPassword());

            // Update log
            logtv.setText("Connecting...");
            btconnect.setVisibility(View.INVISIBLE);
            connected.setVisibility(View.INVISIBLE);
            disconnect.setVisibility(View.INVISIBLE);
            connecting.setVisibility(View.VISIBLE);

            vpnStart = true;

        } catch (IOException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setStatus(String connectionState) {
        if (connectionState != null)
            switch (connectionState) {
                case "DISCONNECTED":
                    //status("connect");
                    vpnStart = false;
                    vpnService.setDefaultStatus();
                    logtv.setText("Not Connected");
                    btconnect.setVisibility(View.VISIBLE);
                    connected.setVisibility(View.INVISIBLE);
                    disconnect.setVisibility(View.INVISIBLE);
                    connecting.setVisibility(View.INVISIBLE);
//                    disconnect_serverConnect_text.setVisibility(View.INVISIBLE);

                    break;
                case "CONNECTED":
                    vpnStart = true;// it will use after restart this activity
                    //status("connected");
                    logtv.setText("Connected");
                    btconnect.setVisibility(View.INVISIBLE);
                    connected.setVisibility(View.VISIBLE);
                    disconnect.setVisibility(View.INVISIBLE);
//                    disconnect_serverConnect_text.setVisibility(View.VISIBLE);

                    connecting.setVisibility(View.INVISIBLE);

                    break;
                case "WAIT":
                    logtv.setText("waiting for server connection!!");
                    btconnect.setVisibility(View.INVISIBLE);
                    connected.setVisibility(View.INVISIBLE);
                    disconnect.setVisibility(View.INVISIBLE);
                    connecting.setVisibility(View.VISIBLE);
//                    disconnect_serverConnect_text.setVisibility(View.VISIBLE);

                    break;
                case "AUTH":
                    logtv.setText("server authenticating!!");
                    btconnect.setVisibility(View.INVISIBLE);
                    connected.setVisibility(View.INVISIBLE);
                    disconnect.setVisibility(View.INVISIBLE);
                    connecting.setVisibility(View.VISIBLE);
//                    disconnect_serverConnect_text.setVisibility(View.VISIBLE);


                    break;
                case "RECONNECTING":
                    //status("connecting");
                    logtv.setText("Reconnecting...");
                    btconnect.setVisibility(View.INVISIBLE);
                    connected.setVisibility(View.INVISIBLE);
                    disconnect.setVisibility(View.INVISIBLE);
                    connecting.setVisibility(View.VISIBLE);

//                    disconnect_serverConnect_text.setVisibility(View.VISIBLE);

                    break;
                case "NONETWORK":
                    logtv.setText("No network connection");
                    btconnect.setVisibility(View.INVISIBLE);
                    connected.setVisibility(View.INVISIBLE);
                    disconnect.setVisibility(View.VISIBLE);
                    connecting.setVisibility(View.INVISIBLE);
//                    disconnect_serverConnect_text.setVisibility(View.VISIBLE);
                    break;
            }

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                setStatus(intent.getStringExtra("state"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                String duration = intent.getStringExtra("duration");
                String lastPacketReceive = intent.getStringExtra("lastPacketReceive");
                String byteIn = intent.getStringExtra("byteIn");
                String byteOut = intent.getStringExtra("byteOut");

                if (duration == null) duration = "00:00:00";
                if (lastPacketReceive == null) lastPacketReceive = "0";
                if (byteIn == null) byteIn = " ";
                if (byteOut == null) byteOut = " ";
                updateConnectionStatus(duration, lastPacketReceive, byteIn, byteOut);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    public void updateConnectionStatus(String duration, String lastPacketReceive, String byteIn, String byteOut) {
//        tvtimer.setText("" + duration+" "+ lastPacketReceive+" "+ byteIn+" "+ byteOut);
        upload.setText(byteOut);
        download.setText(byteIn);
        tvtimer.setText("" + duration);
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void updateCurrentServerIcon(String serverIcon) {
        Glide.with(VpnConnect.this)
                .load(serverIcon)
                .into(serverflag);
    }

    public void updateservername(String name) {
        namecountry.setText(name);
    }

    public void confirmDisconnect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(this.getString(R.string.connection_close_confirm));

        builder.setPositiveButton(this.getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
//                circleProgressbar.enabledTouch(false);
//
//                circleProgressbar.setProgress(0);
                stopVpn();
            }
        });
        builder.setNegativeButton(this.getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void prepareVpn() {
        if (!vpnStart) {
            if (getInternetStatus()) {

                Intent intent = VpnService.prepare(this);
                if (intent != null) {
                    startActivityForResult(intent, 1);
                } else startVpn();//have already permission

            } else {

                showToast("you have no internet connection !!");
            }

        } else if (stopVpn()) {

            showToast("Disconnect Successfully");
        }
    }

    public boolean stopVpn() {
        try {
            vpnThread.stop();

            //status("connect");
            vpnStart = false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        drawer.setVisibility(View.GONE);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_messbox);
        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyshow = prefs.getBoolean("dialogpref", false);
        if (!previouslyshow) {
            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        } else {
//            AdManager.getInstance(VpnConnect.this).showNativeAd(this);
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (ratingBar.getRating() > 0 && ratingBar.getRating() <= 3) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"sajjadsaleem573@example.com"});
                    startActivity(emailIntent);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putBoolean("dialogpref", Boolean.TRUE);
                    edit.apply();
                    dialog.dismiss();
                } else if (ratingBar.getRating() > 3) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.android.chrome")));
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putBoolean("dialogpref", Boolean.TRUE);
                    edit.apply();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Please give rating", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });
    }

    private void inflateAd(NativeAd nativeAd) {

        nativeAd.unregisterView();
        NativeAdLayout nativeAdLayout;
        LinearLayout adView;
        nativeAdLayout = findViewById(R.id.native_ad_container);
        // adView=findViewById(R.id.ad_unit);

        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        LayoutInflater inflater = LayoutInflater.from(VpnConnect.this);
        adView = (LinearLayout) inflater.inflate(R.layout.ad_unified, nativeAdLayout, false);
        nativeAdLayout.addView(adView);


        // Add the AdOptionsView
        LinearLayout adChoicesContainer = findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(VpnConnect.this, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView, nativeAdMedia, nativeAdIcon, clickableViews);
    }


}