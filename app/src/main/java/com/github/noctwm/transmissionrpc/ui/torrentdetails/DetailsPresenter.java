package com.github.noctwm.transmissionrpc.ui.torrentdetails;

import android.util.Log;

import com.github.noctwm.transmissionrpc.rpc.model.Torrent;
import com.github.noctwm.transmissionrpc.rpc.TransmissionService;
import com.github.noctwm.transmissionrpc.rpc.RequestHelper;
import com.github.noctwm.transmissionrpc.ui.PresenterBase;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailsPresenter extends PresenterBase<DetailsContract.DetailsView> {

    private CompositeDisposable compositeDisposable;
    private AtomicBoolean isUpdatePaused = new AtomicBoolean(false);
    private Disposable periodicUpdate;
    private Torrent cachedTorrent;


    DetailsPresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void viewIsReady() {
        isUpdatePaused.set(false);

        if (periodicUpdate == null || periodicUpdate.isDisposed()) {
            startPeriodicUpdate();
        } else {
            if (cachedTorrent != null) {
                getView().update(cachedTorrent);
            }
        }

    }

    @Override
    public void viewIsPaused() {
        Log.d("rpc", "paused");
        isUpdatePaused.set(true);
        //compositeDisposable.clear();
    }

    @Override
    public void destroy() {
        Log.d("rpc", "destroyed");
        compositeDisposable.dispose();
        super.destroy();
    }

    private void startPeriodicUpdate() {
        Log.d("rpc", "recreated");
        if (TransmissionService.getApi() != null) {

            long torrentId = getView().getTorrentID();
            periodicUpdate = Observable.interval(0, 2, TimeUnit.SECONDS, Schedulers.io())
                    .filter(aLong -> !isUpdatePaused.get())
                    .flatMap(r -> TransmissionService.getApi().getTorrents(RequestHelper.getTorrentDetails(torrentId)))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                                Torrent torrent = response.getArguments().getTorrents().get(0);
                                getView().update(torrent);
                                cachedTorrent = torrent;
                            },
                            throwable -> getView().showError("Connection error")
                    );
            compositeDisposable.add(periodicUpdate);
        }
    }


}
