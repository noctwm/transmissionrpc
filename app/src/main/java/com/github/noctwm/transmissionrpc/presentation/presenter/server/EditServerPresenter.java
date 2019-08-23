package com.github.noctwm.transmissionrpc.presentation.presenter.server;

import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.github.noctwm.R;
import com.github.noctwm.transmissionrpc.app.server.Server;
import com.github.noctwm.transmissionrpc.app.server.ServerManager;
import com.github.noctwm.transmissionrpc.presentation.view.server.EditServerView;

import java.util.UUID;

@InjectViewState
public class EditServerPresenter extends MvpPresenter<EditServerView> {


    private UUID serverId;

    public EditServerPresenter(String uuid) {
        serverId = UUID.fromString(uuid);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Server server = ServerManager.getInstance().getServerByUuid(serverId);
        if (server == null) {
            getViewState().setTitle(R.string.add_server);
        } else {
            getViewState().setTitle(R.string.edit_server);
            getViewState().setServerData(server);
        }
    }

    public void doneClicked(Server server) {
        boolean isValid = true;

        if (TextUtils.isEmpty(server.getName())) {
            getViewState().showServerNameError();
            isValid = false;
        }
        if (TextUtils.isEmpty(server.getHost())) {
            getViewState().showHostError();
            isValid = false;
        }
        if (TextUtils.isEmpty(server.getRpcPath())) {
            getViewState().showRpcPathError();
            isValid = false;
        }
        if (server.getPort() == 0) {
            getViewState().showPortError();
            isValid = false;
        }
        if (isValid) {
            server.setId(serverId);
            ServerManager.getInstance().updateOrAdd(server);
            getViewState().finishView();
        }
    }
}
