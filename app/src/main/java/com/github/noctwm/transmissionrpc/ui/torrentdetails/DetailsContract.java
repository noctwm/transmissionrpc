package com.github.noctwm.transmissionrpc.ui.torrentdetails;

import com.github.noctwm.transmissionrpc.rpc.model.Torrent;
import com.github.noctwm.transmissionrpc.ui.BaseContract;

public interface DetailsContract {

    interface DetailsView extends BaseContract.BaseView {

        void update(Torrent torrent);

        long getTorrentID();

        void showError(String error);
    }

    interface DetailsViewPresenter extends BaseContract.Presenter<DetailsContract.DetailsView> {

    }



}
