package com.github.noctwm.transmissionrpc.presentation.presenter.torrentdetails;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.github.noctwm.transmissionrpc.app.rpc.api.RequestHelper;
import com.github.noctwm.transmissionrpc.app.rpc.api.TransmissionService;
import com.github.noctwm.transmissionrpc.app.rpc.model.Torrent;
import com.github.noctwm.transmissionrpc.presentation.view.torrentdetails.TorrentInfoView;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class TorrentInfoPresenter extends MvpPresenter<TorrentInfoView> {

    private CompositeDisposable compositeDisposable;
    private Disposable periodicUpdate;

    private long torrentId;
    private AtomicBoolean stopUpdate = new AtomicBoolean(false);

    public TorrentInfoPresenter(long torrentId) {
        this.torrentId = torrentId;
        compositeDisposable = new CompositeDisposable();
    }


    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        startPeriodicUpdate();
    }

    public void startPeriodicUpdate() {
        stopUpdate.set(false);
        if (periodicUpdate == null || periodicUpdate.isDisposed()) {
            if (TransmissionService.getApi() != null) {
                periodicUpdate = Observable.interval(0, 2, TimeUnit.SECONDS, Schedulers.io())
                        .flatMap(r -> TransmissionService.getApi().getTorrents(RequestHelper.getTorrentDetails(torrentId)))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                                    Torrent torrent = response.getArguments().getTorrents().get(0);
                                    getViewState().update(torrent);
                                    if (stopUpdate.get()) {
                                        periodicUpdate.dispose();
                                        Log.d("","disposed!");
                                    }
                                },
                                throwable -> getViewState().showError("Connection error")
                        );
                compositeDisposable.add(periodicUpdate);
            }
        }
    }

    public void stopPeriodicUpdate() {
        //compositeDisposable.clear();
        stopUpdate.set(true);
    }


}
