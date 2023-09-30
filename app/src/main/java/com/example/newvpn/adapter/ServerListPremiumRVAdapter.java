package com.example.newvpn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newvpn.R;
import com.example.newvpn.interfaces.NavItemClickListener;
import com.example.newvpn.model.Server;

import java.util.ArrayList;

public class ServerListPremiumRVAdapter extends RecyclerView.Adapter<ServerListPremiumRVAdapter.MyViewHolder> {

    private ArrayList<Server> serverLists;
    private Context mContext;
    private NavItemClickListener listener;

    public ServerListPremiumRVAdapter(ArrayList<Server> serverLists, Context context) {
        this.serverLists = serverLists;
        this.mContext = context;
        listener = (NavItemClickListener) context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.server_list_view_premium, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int pos = position+ 1;
        holder.serverCountry.setText(serverLists.get(position).getCountry());
        holder.position.setText("Server Premium " + pos);
        Glide.with(mContext)
                .load(serverLists.get(position).getFlagUrl())
                .into(holder.serverIcon);


        holder.serverItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clickedItem(getServer(holder.getAdapterPosition()));
                // ((Activity)mContext).finish();
            }
        });
    }

    public void filterList(ArrayList<Server> filteredCountry) {
        serverLists = filteredCountry;
        notifyDataSetChanged();
    }

    public Server getServer(int i) {
        return serverLists.get(i);
    }

    @Override
    public int getItemCount() {
        return serverLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout serverItemLayout;
        ImageView serverIcon;
        TextView serverCountry, position;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            serverItemLayout = itemView.findViewById(R.id.serverItemLayout);
            serverIcon = itemView.findViewById(R.id.iconImg);
            serverCountry = itemView.findViewById(R.id.countryTv);
            position = itemView.findViewById(R.id.position);
        }
    }
}
