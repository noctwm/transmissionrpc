package com.github.noctwm.transmissionrpc.ui.server;

import com.github.noctwm.transmissionrpc.server.Server;
import com.github.noctwm.transmissionrpc.ui.BaseContract;

public interface EditServerContract {

    interface EditServerView extends BaseContract.MvpView {

        String getServerId();

        void setTitle(int title);

        void setServerData(Server server);

        Server getServer();

        void showServerNameError();

        void showHostError();

        void showPortError();

        void showRpcPathError();

        void finishView();

    }

    interface EditServerViewPresenter extends BaseContract.Presenter<EditServerView>{
        void doneClicked();
    }

}
