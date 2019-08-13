package com.github.noctwm.transmissionrpc.ui.server;

import android.text.TextUtils;

import com.github.noctwm.R;
import com.github.noctwm.transmissionrpc.server.Server;
import com.github.noctwm.transmissionrpc.server.ServerManager;
import com.github.noctwm.transmissionrpc.ui.PresenterBase;

import java.util.UUID;

public class EditServerPresenter extends PresenterBase<EditServerContract.EditServerView>
        implements EditServerContract.EditServerViewPresenter{

    private ServerManager serverManager;
    private UUID serverId;

    EditServerPresenter() {
        serverManager = ServerManager.getInstance();
    }

    @Override
    public void viewIsReady() {
        serverId = UUID.fromString(getView().getServerId());
        Server server = serverManager.getServerByUuid(serverId);
        if (server == null) {
            getView().setTitle(R.string.add_server);
        } else {
            getView().setTitle(R.string.edit_server);
            getView().setServerData(server);
        }
    }

    @Override
    public void doneClicked() {
        boolean isValid = true;
        Server server = getView().getServer();
        if (TextUtils.isEmpty(server.getName())) {
            getView().showServerNameError();
            isValid = false;
        }
        if (TextUtils.isEmpty(server.getHost())) {
            getView().showHostError();
            isValid = false;
        }
        if (TextUtils.isEmpty(server.getRpcPath())) {
            getView().showRpcPathError();
            isValid = false;
        }
        if (server.getPort() == 0) {
            getView().showPortError();
            isValid = false;
        }
        if (isValid) {
            server.setId(serverId);
            serverManager.updateOrAdd(server);
            getView().finishView();
        }
    }

    @Override
    public void viewIsPaused() {

    }


}
