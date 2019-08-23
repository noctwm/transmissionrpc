package com.github.noctwm.transmissionrpc.presentation.view.server;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.github.noctwm.transmissionrpc.app.server.Server;

import java.util.List;
import java.util.UUID;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ManageServerView extends MvpView {

    void updateServersList(List<Server> servers);

    void setActiveServerId(UUID id);

    @StateStrategyType(SkipStrategy.class)
    void startAddServerActivity(Server server);

    void showDialog();

    void hideDialog();

    void finishActionMode();
}
