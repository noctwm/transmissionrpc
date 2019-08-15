package com.github.noctwm.transmissionrpc.ui.main;

import com.github.noctwm.transmissionrpc.rpc.model.Torrent;
import com.github.noctwm.transmissionrpc.server.Server;
import com.github.noctwm.transmissionrpc.ui.BaseContract;

import java.util.List;

interface MainContract {

    interface MainView extends BaseContract.BaseView {

        void updateTorrentsList(List<Torrent> torrents);

        void updateServersList(List<Server> servers);

        void startDetailsActivity(long torrentId);

        void startAddServerActivity(Server server);

        void startManageServersActivity();

        void setServerName(String serverName);

        void clearServerName();

        void setServerSpeed(long downloadSpeed, long uploadSpeed, boolean visible);

        void setActiveServer(Server server);

        void showError(String error);
    }

    interface MainViewPresenter extends BaseContract.Presenter<MainView> {

        void listItemTorrentsClicked(long torrentId);

        void listItemServersClicked(Server server);

        void btAddServerClicked();

        void btManageServersClicked();

        void serversListVisible();

    }
}
