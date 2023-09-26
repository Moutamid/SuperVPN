package com.example.newvpn;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newvpn.adapter.ServerListRVAdapter;
import com.example.newvpn.interfaces.ChangeServer;
import com.example.newvpn.interfaces.NavItemClickListener;

import com.example.newvpn.model.Server;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


import java.util.ArrayList;

public class CountryList extends AppCompatActivity implements NavItemClickListener {
    private RecyclerView serverListRv;
    private ArrayList<Server> serverLists;
    VpnConnect vpnconnect;
    private EditText editsearch;
    ImageView btback;
    private ServerListRVAdapter serverListRVAdapter;
    private ChangeServer changeServer;
    AdView madView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleractivity);
        madView = findViewById(R.id.recycler_admob_banner);
        editsearch = findViewById(R.id.edit_search);
        btback = findViewById(R.id.bt_back);
        initializeAll();

        if (serverLists != null) {
            serverListRVAdapter = new ServerListRVAdapter(serverLists, this);
            serverListRv.setAdapter(serverListRVAdapter);

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
                AdManager.getInstance(CountryList.this).showInterstitialAd(CountryList.this);
            }
        });

    }

    private void initializeAll() {

        serverListRv = findViewById(R.id.serverListRv);
        serverListRv.setHasFixedSize(true);

        vpnconnect = new VpnConnect();
        serverListRv.setLayoutManager(new LinearLayoutManager(this));

        serverLists = getServerList();
        changeServer = (ChangeServer) vpnconnect;

    }

    /*"us.ovpn",
                "freeopenvpn",
                "416248023"*/
    private ArrayList getServerList() {

        ArrayList<Server> servers = new ArrayList<>();

        servers.add(new Server("Azerbaijan", Utils.getImgURL(R.drawable.azerbaijan), "azerbaijan.ovpn", "vpn", "vpn"));
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
        AdManager.getInstance(CountryList.this).showInterstitialAd(CountryList.this);
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
}