package com.example.newvpn;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newvpn.adapter.ServerListPremiumRVAdapter;
import com.example.newvpn.adapter.ServerListRVAdapter;
import com.example.newvpn.interfaces.ChangeServer;
import com.example.newvpn.interfaces.NavItemClickListener;

import com.example.newvpn.model.Server;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


import java.util.ArrayList;

public class CountryList extends AppCompatActivity implements NavItemClickListener, View.OnClickListener {
    ColorStateList def;
    TextView item1;
    TextView item2;
    TextView item3;
    TextView select;
    private RecyclerView serverListRv, serverListRv1;
    private ArrayList<Server> serverLists;
    VpnConnect vpnconnect;
    private EditText editsearch;
    ImageView btback;
    private ServerListRVAdapter serverListRVAdapter;
    private ServerListPremiumRVAdapter serverListPremiumRVAdapter;
    private ChangeServer changeServer;
    AdView madView;
    TextView premium_txt, free_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleractivity);
        madView = findViewById(R.id.recycler_admob_banner);
        editsearch = findViewById(R.id.edit_search);
        btback = findViewById(R.id.bt_back);
        premium_txt = findViewById(R.id.premium_txt);
        free_txt = findViewById(R.id.free_txt);
        initializeAll();
        item1 = findViewById(R.id.item1);
        item2 = findViewById(R.id.item2);
        item3 = findViewById(R.id.item3);
        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        item3.setOnClickListener(this);
        select = findViewById(R.id.select);
        def = item2.getTextColors();
        if (serverLists != null) {
            serverListRVAdapter = new ServerListRVAdapter(serverLists, this);
            serverListRv.setAdapter(serverListRVAdapter);

        } if (serverLists != null) {
            serverListPremiumRVAdapter = new ServerListPremiumRVAdapter(serverLists, this);
            serverListRv1.setAdapter(serverListPremiumRVAdapter);

        }

        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CountryList.this, VpnConnect.class);
                startActivity(intent);
                finish();
//                AdManager.getInstance(CountryList.this).showInterstitialAd(CountryList.this);
            }
        });

    }

    private void initializeAll() {

        serverListRv = findViewById(R.id.serverListRv);
        serverListRv.setHasFixedSize(true);
    serverListRv1 = findViewById(R.id.serverListRv1);
        serverListRv1.setHasFixedSize(true);

        vpnconnect = new VpnConnect();
        serverListRv.setLayoutManager(new LinearLayoutManager(this));
        serverListRv1.setLayoutManager(new LinearLayoutManager(this));

        serverLists = getServerList();
        changeServer = (ChangeServer) vpnconnect;

    }

    /*"us.ovpn",
                "freeopenvpn",
                "416248023"*/
    private ArrayList getServerList() {

        ArrayList<Server> servers = new ArrayList<>();

        servers.add(new
                Server("Azerbaijan", Utils.getImgURL(R.drawable.azerbaijan), "azerbaijan.ovpn", "vpn", "vpn"));
        servers.add(new Server("Canada", Utils.getImgURL(R.drawable.canada), "canada.ovpn", "vpn", "vpn"));
        servers.add(new Server("China", Utils.getImgURL(R.drawable.china), "china.ovpn", "vpn", "vpn"));
        servers.add(new Server("Japan", Utils.getImgURL(R.drawable.japan), "japan.ovpn", "vpn", "vpn"));
        servers.add(new Server("Korea", Utils.getImgURL(R.drawable.korea), "korea.ovpn", "vpn", "vpn"));
        servers.add(new Server("Netherlands", Utils.getImgURL(R.drawable.natherland), "natherlands.ovpn", "vpn", "vpn"));
        servers.add(new Server("Romania", Utils.getImgURL(R.drawable.romania), "romania.ovpn", "vpn", "vpn"));
        servers.add(new Server("Russian", Utils.getImgURL(R.drawable.russia), "russian.ovpn", "vpn", "vpn"));
        servers.add(new Server("Singapore", Utils.getImgURL(R.drawable.singapur), "singapur.ovpn", "vpn", "vpn"));
        servers.add(new Server("United State", Utils.getImgURL(R.drawable.united_state), "unitedstate.ovpn", "vpn", "vpn"));
        servers.add(new Server("Veit Nam", Utils.getImgURL(R.drawable.veitnam), "veitnam.ovpn", "vpn", "vpn"));

        return servers;
    }

    @Override
    public void clickedItem(Server server) {
        SharedPreference sharedPreference = new SharedPreference(this);
        sharedPreference.saveServer(server);
        vpnconnect.stopVpn();
        Intent intent = new Intent(CountryList.this, VpnConnect.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("activity", "recycler");
        startActivity(intent);
        finish();
//        AdManager.getInstance(CountryList.this).showInterstitialAd(CountryList.this);
    }

    private void filter(String country) {
        ArrayList<Server> filterdNames = new ArrayList<>();
        //looping through existing elements
        for (Server s : serverLists) {
            //if the existing elements contains the search input
            if (s.getCountry().toLowerCase().contains(country.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }
        serverListRVAdapter.filterList(filterdNames);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.item1){
            select.animate().x(0).setDuration(100);
            item1.setTextColor(Color.BLACK);
            item2.setTextColor(def);
            free_txt.setVisibility(View.VISIBLE);
            premium_txt.setVisibility(View.VISIBLE);
            serverListRv.setVisibility(View.VISIBLE);
            serverListRv1.setVisibility(View.VISIBLE);

            item3.setTextColor(def);
        } else if (view.getId() == R.id.item2){
            item1.setTextColor(def);
            item2.setTextColor(Color.BLACK);
            item3.setTextColor(def);
            serverListRv.setVisibility(View.VISIBLE);
            serverListRv1.setVisibility(View.GONE);
            free_txt.setVisibility(View.VISIBLE);
            premium_txt.setVisibility(View.GONE);
            int size = item2.getWidth();
            select.animate().x(size).setDuration(100);
        } else if (view.getId() == R.id.item3){
            item1.setTextColor(def);
            item3.setTextColor(Color.BLACK);
            serverListRv.setVisibility(View.GONE);
            serverListRv1.setVisibility(View.VISIBLE);
            free_txt.setVisibility(View.GONE);
            premium_txt.setVisibility(View.VISIBLE);
            item2.setTextColor(def);
            int size = item2.getWidth() * 2;
            select.animate().x(size).setDuration(100);
        }
    }
}