package com.github.noctwm.transmissionrpc.presentation.view.torrentdetails;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.github.noctwm.transmissionrpc.app.rpc.model.Torrent;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface TorrentInfoView extends MvpView {

    void update(Torrent torrent);

    void showError(String error);
}
