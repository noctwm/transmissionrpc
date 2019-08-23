package com.github.noctwm.transmissionrpc.ui.activity.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.github.noctwm.transmissionrpc.presentation.presenter.main.MainPresenter;
import com.github.noctwm.transmissionrpc.presentation.view.main.MainView;
import com.github.noctwm.transmissionrpc.app.server.Server;
import com.github.noctwm.transmissionrpc.ui.activity.server.EditServerActivity;
import com.github.noctwm.transmissionrpc.ui.activity.server.ManageServersActivity;
import com.github.noctwm.transmissionrpc.ui.activity.torrentdetails.TorrentDetailsActivity;
import com.github.noctwm.transmissionrpc.ui.adapter.ServersAdapter;
import com.github.noctwm.transmissionrpc.ui.adapter.TorrentAdapter;
import com.github.noctwm.transmissionrpc.common.Utils;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.noctwm.R;
import com.github.noctwm.transmissionrpc.app.rpc.model.Torrent;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MvpAppCompatActivity
        implements MainView {

    public static final String EXTRA_TORRENT_ID = "com.github.noctwm.transmissionrpc.TORRENT_ID";
    public static final String EXTRA_TORRENT_NAME = "com.github.noctwm.transmissionrpc.TORRENT_NAME";
    public static final String EXTRA_MESSAGE_SERVER_ID = "com.github.noctwm.transmissionrpc.SERVER_ID";

    @InjectPresenter
    MainPresenter presenter;

    private TorrentAdapter torrentAdapter;
    private ListView lvMain;
    private Button btServers;
    private LinearLayout llNavServers;
    private LinearLayout llNavMenu;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ServersAdapter serversAdapter;
    private ListView lvServers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
                presenter.onDrawerToggleClick();
                super.onDrawerStateChanged(newState);
            }
        });
        toggle.syncState();

        llNavMenu = navigationView.findViewById(R.id.ll_nav_menu);
        llNavServers = navigationView.findViewById(R.id.ll_nav_servers);

        lvServers = navigationView.findViewById(R.id.lv_servers_main);
        serversAdapter = new ServersAdapter(this, new ArrayList<>());
        lvServers.setAdapter(serversAdapter);
        lvServers.setOnItemClickListener((parent, view, position, id) -> {
            drawer.closeDrawer(GravityCompat.START);
            Server server = (Server) serversAdapter.getItem(position);
            presenter.onListItemServersClicked(server);
        });

        Button btSettings = navigationView.findViewById(R.id.bt_settings_main);
        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btServers = navigationView.findViewById(R.id.bt_servers);
        btServers.setOnClickListener(v -> {
            presenter.onBtServersClick();
        });

        Button btAddServer = navigationView.findViewById(R.id.bt_add_server_main);
        btAddServer.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            presenter.onAddServerClick();
        });

        Button btManageServers = navigationView.findViewById(R.id.bt_manage_servers_main);
        btManageServers.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            presenter.onManageServersClick();
        });

        lvMain = findViewById(R.id.lv_torrents_main);
        torrentAdapter = new TorrentAdapter(this, new ArrayList<>());
        lvMain.setAdapter(torrentAdapter);
        lvMain.setVerticalScrollBarEnabled(false);
        lvMain.setOnItemClickListener((parent, view, position, id) ->
                presenter.listItemTorrentsClicked(id, torrentAdapter.getItemById(id).getName()));

    }

    @Override
    public void highlightActiveServer(Server server) {
        int position = serversAdapter.getItemPosition(server);
        lvServers.setSelection(position);
    }

    //tmp
    @Override
    public void showError(String error) {
        Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void updateTorrentsList(List<Torrent> torrents) {
        torrentAdapter.update(torrents);
        torrentAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateServersList(List<Server> servers) {
        serversAdapter.update(servers);
        serversAdapter.notifyDataSetChanged();
    }

    @Override
    public void setServerName(String serverName) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(serverName);
        }
        toolbar.setTitle(serverName);
        btServers.setText(serverName);
    }

    @Override
    public void clearServerName() {
        toolbar.setTitle(getResources().getString(R.string.app_name));
        btServers.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
        btServers.setText("");
        /*isBtServersPressed = false;
        llNavServers.setVisibility(View.GONE);
        llNavMenu.setVisibility(View.VISIBLE);*/
    }

    @Override
    public void setServerSpeed(long downloadSpeed, long uploadSpeed, boolean visible) {
        if (visible) {
            String s = "\u25bc " +
                    Utils.formatByteSpeed(this, downloadSpeed) +
                    "  \u25b2 " +
                    Utils.formatByteSpeed(this, uploadSpeed);
            toolbar.setSubtitle(s);
        } else {
            toolbar.setSubtitle("");
        }
    }

    @Override
    public void startDetailsActivity(long torrentId, String torrentName) {
        Intent intent = new Intent(this, TorrentDetailsActivity.class);
        intent.putExtra(EXTRA_TORRENT_ID, torrentId);
        intent.putExtra(EXTRA_TORRENT_NAME, torrentName);
        startActivity(intent);
    }

    @Override
    public void startAddServerActivity(Server server) {
        Intent intent = new Intent(this, EditServerActivity.class);
        intent.putExtra(EXTRA_MESSAGE_SERVER_ID, server.getId().toString());
        startActivity(intent);
    }

    @Override
    public void startManageServersActivity() {
        Intent intent = new Intent(this, ManageServersActivity.class);
        startActivity(intent);
    }

    @Override
    public void showServersList() {
        btServers.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_up, 0);
        llNavServers.setVisibility(View.VISIBLE);
        llNavMenu.setVisibility(View.GONE);
        presenter.serversListVisible();
    }

    @Override
    public void hideServersList() {
        btServers.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
        llNavServers.setVisibility(View.GONE);
        llNavMenu.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if (!isChangingConfigurations()) {
            presenter.stopPeriodicUpdate();
        }
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        presenter.startPeriodicUpdate();
    }

}
