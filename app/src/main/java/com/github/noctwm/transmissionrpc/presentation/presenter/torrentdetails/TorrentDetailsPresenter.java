package com.github.noctwm.transmissionrpc.presentation.presenter.torrentdetails;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.github.noctwm.transmissionrpc.presentation.view.torrentdetails.TorrentDetailsView;

@InjectViewState
public class TorrentDetailsPresenter extends MvpPresenter<TorrentDetailsView> {

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().setTitle();
    }
}
