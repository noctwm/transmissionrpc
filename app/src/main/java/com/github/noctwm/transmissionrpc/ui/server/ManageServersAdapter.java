package com.github.noctwm.transmissionrpc.ui.server;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.github.noctwm.R;
import com.github.noctwm.transmissionrpc.server.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ManageServersAdapter extends BaseAdapter {

    private List<Server> servers = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private UUID activeServerId;

    ManageServersAdapter(Context context) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    void updateServers(List<Server> servers) {
        if (servers == null) {
            this.servers = new ArrayList<>();
        } else {
            this.servers = servers;
        }
    }

    void setActiveServerId(UUID id) {
        activeServerId = id;
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
            view = layoutInflater.inflate(R.layout.item_lv_manage_servers, parent, false);
        }
        Server server = (Server) getItem(position);
        if (server.getId().equals(activeServerId)) {
            ((RadioButton)view.findViewById(R.id.rb_active_server)).setChecked(true);
        }
        ((TextView)view.findViewById(R.id.tv_server_name_manage_servers)).setText(server.getName());
        return view;
    }
}
