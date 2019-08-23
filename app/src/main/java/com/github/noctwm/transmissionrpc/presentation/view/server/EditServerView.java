package com.github.noctwm.transmissionrpc.presentation.view.server;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.github.noctwm.transmissionrpc.app.server.Server;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface EditServerView extends MvpView {

    void setTitle(int title);

    void setServerData(Server server);

    void showServerNameError();

    void showHostError();

    void showPortError();

    void showRpcPathError();

    @StateStrategyType(SkipStrategy.class)
    void finishView();
}
