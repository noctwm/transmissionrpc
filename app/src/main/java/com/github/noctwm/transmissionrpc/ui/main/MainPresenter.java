package com.github.noctwm.transmissionrpc.ui.main;

import com.github.noctwm.transmissionrpc.rpc.model.Torrent;
import com.github.noctwm.transmissionrpc.rpc.RequestHelper;
import com.github.noctwm.transmissionrpc.rpc.TransmissionService;
import com.github.noctwm.transmissionrpc.server.Server;
import com.github.noctwm.transmissionrpc.server.ServerManager;
import com.github.noctwm.transmissionrpc.ui.PresenterBase;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends PresenterBase<MainContract.MainView> implements MainContract.MainViewPresenter {

    private CompositeDisposable compositeDisposable;
    private Disposable periodicUpdate;

    private AtomicBoolean isUpdatePaused = new AtomicBoolean(false);

    private List<Torrent> cachedTorrents;

    MainPresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void viewIsReady() {

        getView().updateServersList(ServerManager.getInstance().getAll());

        Server activeServer = ServerManager.getInstance().getActiveServer();
        isUpdatePaused.set(false);

        if (activeServer != null) {
            getView().setServerName(activeServer.getName());
            if (periodicUpdate == null || periodicUpdate.isDisposed() ) {
                startPeriodicUpdate();
            } else {
                if (cachedTorrents != null) {
                    getView().updateTorrentsList(cachedTorrents);
                    updateTotalSpeed(cachedTorrents);
                }
            }

        } else {
            //todo show "add server" message, if servers list empty
        }


    }

    @Override
    public void viewIsPaused() {
        isUpdatePaused.set(true);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void destroy() {
        compositeDisposable.dispose();
        super.destroy();
    }

    @Override
    public void listItemTorrentsClicked(long torrentId) {
        getView().startDetailsActivity(torrentId);
    }

    @Override
    public void listItemServersClicked(Server server) {
        ServerManager.getInstance().setActiveServer(server);
        getView().setServerName(server.getName());
        compositeDisposable.clear();
        startPeriodicUpdate();

    }

    private void startPeriodicUpdate() {


        if (TransmissionService.getApi() != null) {
            periodicUpdate = Observable.interval(0, 2, TimeUnit.SECONDS, Schedulers.io())
                    .filter(aLong -> !isUpdatePaused.get())
                    .flatMap(r -> TransmissionService.getApi().getTorrents(RequestHelper.getTorrentInfo()))
                    //.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {

                                List<Torrent> torrents = response.getArguments().getTorrents();
                                if (isViewAttached()) {
                                    getView().updateTorrentsList(torrents);
                                    updateTotalSpeed(torrents);
                                }
                                cachedTorrents = torrents;
                            },
                            throwable -> getView().showError("Connection error")
                    );
            compositeDisposable.add(periodicUpdate);
        }
    }

    private void updateTotalSpeed(List<Torrent> torrents) {
        long downloadSpeed = 0;
        long uploadSpeed = 0;
        for (Torrent t : torrents) {
            downloadSpeed += t.getRateDownload();
            uploadSpeed += t.getRateUpload();
        }
        getView().setServerSpeed(downloadSpeed, uploadSpeed, true);
    }


}
