package com.github.noctwm.transmissionrpc.presentation.view.main;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.github.noctwm.transmissionrpc.app.rpc.model.Torrent;
import com.github.noctwm.transmissionrpc.app.server.Server;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MainView extends MvpView {

    void updateTorrentsList(List<Torrent> torrents);

    void updateServersList(List<Server> servers);

    @StateStrategyType(SkipStrategy.class)
    void startDetailsActivity(long torrentId, String torrentName);

    @StateStrategyType(SkipStrategy.class)
    void startAddServerActivity(Server server);

    @StateStrategyType(SkipStrategy.class)
    void startManageServersActivity();

    void setServerName(String serverName);

    void clearServerName();

    void setServerSpeed(long downloadSpeed, long uploadSpeed, boolean visible);

    void highlightActiveServer(Server server);

    void showError(String error);

    void showServersList();

    void hideServersList();

    void hideDrawer();
}
