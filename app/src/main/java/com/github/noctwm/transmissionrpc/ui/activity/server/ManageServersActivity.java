package com.github.noctwm.transmissionrpc.ui.activity.server;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.github.noctwm.R;
import com.github.noctwm.transmissionrpc.app.server.Server;
import com.github.noctwm.transmissionrpc.presentation.presenter.server.ManageServersPresenter;
import com.github.noctwm.transmissionrpc.presentation.view.server.ManageServerView;
import com.github.noctwm.transmissionrpc.ui.adapter.ManageServersAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.noctwm.transmissionrpc.ui.activity.main.MainActivity.EXTRA_MESSAGE_SERVER_ID;

public class ManageServersActivity extends MvpAppCompatActivity implements ManageServerView {

    @InjectPresenter
    public ManageServersPresenter presenter;
    private ManageServersAdapter serversAdapter;
    private ListView lvManageServers;
    private ActionMode actionMode;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_servers);

        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null) {
            toolbar.setDisplayHomeAsUpEnabled(true);
            toolbar.setTitle(R.string.title_servers);
        }

        lvManageServers = findViewById(R.id.lv_manage_servers);
        serversAdapter = new ManageServersAdapter(this);
        lvManageServers.setAdapter(serversAdapter);
        lvManageServers.setOnItemClickListener((parent, view, position, id) -> {
            Server server = (Server) serversAdapter.getItem(position);
            presenter.listItemClicked(server);
        });

        lvManageServers.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.activity_manage_servers_action_mode, menu);
                actionMode = mode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_item_select_all) {
                    for (int i = 0; i < serversAdapter.getCount(); i++) {
                        lvManageServers.setItemChecked(i, true);
                    }
                    return true;
                }
                if (id == R.id.menu_item_delete) {
                    /*RemoveServerFragment removeDialogFragment = RemoveServerFragment.getInstance(lvManageServers.getCheckedItemCount());
                    removeDialogFragment.show(getSupportFragmentManager(), "delete");*/
                    presenter.onMenuItemDelete();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab_manage_servers);
        fab.setOnClickListener(v -> presenter.onFabClicked());
    }

    @Override
    public void finishActionMode() {
        if (actionMode != null) {
            actionMode.finish();
        }
    }

    @Override
    public void showDialog() {
        int selectedCount = lvManageServers.getCheckedItemCount();
        dialog = new AlertDialog.Builder(ManageServersActivity.this)
                .setMessage(getResources().getQuantityString(R.plurals.remove_servers_message, selectedCount, selectedCount))
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> presenter.onDialogDismiss())
                .setPositiveButton(R.string.delete, (dialog, which) -> presenter.deleteServers(getSelectedServers())).show();
    }

    @Override
    public void hideDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void updateServersList(List<Server> servers) {
        serversAdapter.updateServers(servers);
        serversAdapter.notifyDataSetChanged();
    }

    @Override
    public void setActiveServerId(UUID id) {
        serversAdapter.setActiveServerId(id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startAddServerActivity(Server server) {
        Intent intent = new Intent(this, EditServerActivity.class);
        intent.putExtra(EXTRA_MESSAGE_SERVER_ID, server.getId().toString());
        startActivity(intent);
    }

   /* @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        presenter.deleteServers();
        if (actionMode != null) {
            actionMode.finish();
        }
    }*/

    private List<Server> getSelectedServers() {
        SparseBooleanArray selected = lvManageServers.getCheckedItemPositions();
        List<Server> selectedServers = new ArrayList<>();
        for (int i = 0; i < serversAdapter.getCount(); i++) {
            if (selected.get(i)) {
                Server server = (Server) serversAdapter.getItem(i);
                selectedServers.add(server);
            }
        }
        return selectedServers;
    }


}
