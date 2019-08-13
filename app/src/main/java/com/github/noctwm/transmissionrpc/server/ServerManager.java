package com.github.noctwm.transmissionrpc.server;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.noctwm.transmissionrpc.App;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ServerManager {

    private static final String SERVERS_FILE_NAME = "servers";
    private static final String ACTIVE_SERVER_FILE_NAME = "active_server";
    private static final String KEY_SERVERS = "servers";
    private static final String KEY_ACTIVE_SERVER = "active_server";

    private static ServerManager serverManager = null;
    private Gson gson;
    private List<Server> servers = new ArrayList<>();
    private Server activeServer;
    private List<OnActiveServerChangedListener> activeServerListeners = new LinkedList<>();


    /*private ServerManager() {
    }*/

    private ServerManager(Gson gson) {
        this.gson = gson;
        loadServers();
        loadActiveServer();
    }

    public static void initInstance(Gson gson) {
        if (serverManager == null) {
            serverManager = new ServerManager(gson);
        }
    }

    public static ServerManager getInstance() {
        return serverManager;
    }

    public List<Server> getAll() {
        return servers;
    }

    @Nullable
    public Server getServerByUuid(UUID id) {
        for (Server s : servers) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    public void updateOrAdd(Server server) {
        int i = servers.indexOf(server);
        if (i < 0) {
            server.setPositionInList(servers.size());
            servers.add(server);
        } else {
            servers.set(i, server);
        }
        saveServers();
        if (activeServer == null) {
            setActiveServer(server);
        }
    }

    public void deleteServer(Server server) {
        servers.remove(server);
        for (int i = 0; i < servers.size(); i++) {
            servers.get(i).setPositionInList(i);
        }
        saveServers();
    }

    @Nullable
    public Server getActiveServer() {
        return activeServer;
    }

    public void setActiveServer(Server newServer) {
        if (activeServer != null) {
            if (activeServer.equals(newServer) &&
                    activeServer.getPort() == newServer.getPort() &&
                    activeServer.getHost().equals(newServer.getHost()) &&
                    activeServer.getRpcPath().equals(newServer.getRpcPath()) &&
                    activeServer.isUseAuthentication() == newServer.isUseAuthentication() &&
                    activeServer.getLogin().equals(newServer.getLogin()) &&
                    activeServer.getPassword().equals(newServer.getPassword())) {
                return;
            }
        }

        this.activeServer = newServer;
        saveActiveServer();
        fireActiveServerChangedEvent();
    }

    private void saveServers() {
        Set<String> serversInJson = new HashSet<>();
        for (Server s : servers) {
            serversInJson.add(gson.toJson(s));
        }

        App.get().getSharedPreferences(SERVERS_FILE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putStringSet(KEY_SERVERS, serversInJson)
                .apply();
    }

    private void loadServers() {
        SharedPreferences sp = App.get().getSharedPreferences(SERVERS_FILE_NAME, Context.MODE_PRIVATE);
        Set<String> serversInJson = sp.getStringSet(KEY_SERVERS, Collections.emptySet());

        servers.clear();
        if (serversInJson != null) {
            for (String s : serversInJson) {
                servers.add(gson.fromJson(s, Server.class));
            }
            Collections.sort(servers, (o1, o2) -> o1.getPositionInList() - o2.getPositionInList());
        } else {
            servers = new ArrayList<>();
        }
    }

    private void saveActiveServer() {
        String serverInJson = gson.toJson(activeServer);
        App.get().getSharedPreferences(ACTIVE_SERVER_FILE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_ACTIVE_SERVER, serverInJson)
                .apply();
    }

    private void loadActiveServer() {
        SharedPreferences sp = App.get().getSharedPreferences(ACTIVE_SERVER_FILE_NAME, Context.MODE_PRIVATE);
        String serverInJson = sp.getString(KEY_ACTIVE_SERVER, null);
        activeServer = gson.fromJson(serverInJson, Server.class);
        if (activeServer == null && servers.size() > 0) {
            setActiveServer(servers.get(0));
        }
    }

    public interface OnActiveServerChangedListener {
        void serverChanged(Server newServer);
    }

    public void addOnActiveServerChangedListener(@NonNull OnActiveServerChangedListener listener) {
        if (!activeServerListeners.contains(listener)) {
            activeServerListeners.add(listener);
        }
    }

    public void removeOnActiveServerChangedListener(@NonNull OnActiveServerChangedListener listener) {
        activeServerListeners.remove(listener);
    }

    private void fireActiveServerChangedEvent() {
        for (OnActiveServerChangedListener listener : activeServerListeners) {
            listener.serverChanged(activeServer);
        }
    }
}
