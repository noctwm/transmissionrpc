package com.github.noctwm.transmissionrpc.ui.server;

import com.github.noctwm.transmissionrpc.server.Server;
import com.github.noctwm.transmissionrpc.server.ServerManager;
import com.github.noctwm.transmissionrpc.ui.PresenterBase;

import java.util.List;

public class ManageServersPresenter extends PresenterBase<ManageServersContract.ManageServersView>
        implements ManageServersContract.ManageServersViewPresenter {

    @Override
    public void attachView(ManageServersContract.ManageServersView view) {
        super.attachView(view);

    }

    @Override
    public void viewIsReady() {
        getView().updateServersList(ServerManager.getInstance().getAll());
        Server activeServer = ServerManager.getInstance().getActiveServer();
        if (activeServer != null) {
            getView().setActiveServerId(activeServer.getId());
        }

    }

    @Override
    public void viewIsPaused() {

    }

    @Override
    public void fabClicked() {
        getView().startAddServerActivity(new Server());
    }

    @Override
    public void deleteServers() {
        List<Server> servers = getView().getSelectedServers();
        ServerManager.getInstance().delete(servers);
    }

    @Override
    public void listItemClicked(Server server) {
        getView().startAddServerActivity(server);
    }
}
