package com.github.noctwm.transmissionrpc.ui.server;

import com.github.noctwm.transmissionrpc.server.Server;
import com.github.noctwm.transmissionrpc.ui.BaseContract;

import java.util.List;
import java.util.UUID;

public interface ManageServersContract {

    interface ManageServersView extends BaseContract.BaseView {
        void updateServersList(List<Server> servers);

        void setActiveServerId(UUID id);

        void startAddServerActivity(Server server);

        List<Server> getSelectedServers();
    }

    interface ManageServersViewPresenter extends BaseContract.Presenter<ManageServersView> {

        void fabClicked();

        void listItemClicked(Server server);

        void deleteServers();

    }
}
