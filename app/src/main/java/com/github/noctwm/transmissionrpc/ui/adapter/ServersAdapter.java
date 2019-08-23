package com.github.noctwm.transmissionrpc.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.noctwm.R;
import com.github.noctwm.transmissionrpc.app.server.Server;

import java.util.ArrayList;
import java.util.List;

public class ServersAdapter extends BaseAdapter {

    private List<Server> servers;
    private LayoutInflater layoutInflater;

    public ServersAdapter(Context context, List<Server> servers) {

        this.servers = servers;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return servers.size();
    }

    @Override
    public Object getItem(int position) {
        return servers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_lv_servers_main, parent, false);
        }
        Server server = (Server) getItem(position);
        ((TextView)view.findViewById(R.id.tv_server_name)).setText(server.getName());
        ((TextView)view.findViewById(R.id.tv_server_ip)).setText(server.getHost());


        return view;
    }

    public void update(List<Server> servers) {
        if (servers == null) {
            this.servers = new ArrayList<>();
        } else {
            this.servers = servers;
        }
    }

    public int getItemPosition(Server server) {
        return servers.indexOf(server);
    }

   }
