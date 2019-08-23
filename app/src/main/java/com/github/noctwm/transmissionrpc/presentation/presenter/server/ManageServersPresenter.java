package com.github.noctwm.transmissionrpc.presentation.presenter.server;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.github.noctwm.transmissionrpc.app.server.Server;
import com.github.noctwm.transmissionrpc.app.server.ServerManager;
import com.github.noctwm.transmissionrpc.presentation.view.server.ManageServerView;

import java.util.List;

@InjectViewState
public class ManageServersPresenter extends MvpPresenter<ManageServerView> {

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().updateServersList(ServerManager.getInstance().getAll());
        Server activeServer = ServerManager.getInstance().getActiveServer();
        if (activeServer != null) {
            getViewState().setActiveServerId(activeServer.getId());
        }
    }

    public void onMenuItemDelete() {
        getViewState().showDialog();
    }

    public void onDialogDismiss() {
        getViewState().hideDialog();
        getViewState().finishActionMode();
    }


    public void onFabClicked() {
        getViewState().finishActionMode();
        getViewState().startAddServerActivity(new Server());
    }


    public void deleteServers(List<Server> servers) {
        ServerManager.getInstance().delete(servers);
        getViewState().hideDialog();
        getViewState().updateServersList(ServerManager.getInstance().getAll());
    }


    public void listItemClicked(Server server) {
        getViewState().startAddServerActivity(server);
    }
}
