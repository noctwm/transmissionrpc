package com.github.noctwm.transmissionrpc.ui.main;

import com.github.noctwm.R;
import com.github.noctwm.transmissionrpc.App;
import com.github.noctwm.transmissionrpc.rpc.RequestHelper;
import com.github.noctwm.transmissionrpc.rpc.TransmissionService;
import com.github.noctwm.transmissionrpc.rpc.model.Torrent;
import com.github.noctwm.transmissionrpc.server.Server;
import com.github.noctwm.transmissionrpc.server.ServerManager;
import com.github.noctwm.transmissionrpc.ui.PresenterBase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends PresenterBase<MainContract.MainView> implements MainContract.MainViewPresenter {

    private CompositeDisposable compositeDisposable;
    private Disposable periodicUpdate;

    private List<Torrent> cachedTorrents;

    MainPresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void viewIsReady() {

        getView().updateServersList(ServerManager.getInstance().getAll());

        Server activeServer = ServerManager.getInstance().getActiveServer();

        if (activeServer != null) {
            getView().setServerName(activeServer.getName());
            getView().setActiveServer(activeServer);
            if (periodicUpdate == null || periodicUpdate.isDisposed()) {
                startPeriodicUpdate();
            } else {
                if (cachedTorrents != null) {
                    getView().updateTorrentsList(cachedTorrents);
                    updateTotalSpeed(cachedTorrents);
                }
            }

        } else {
            getView().clearServerName();
        }


    }

    @Override
    public void viewIsPaused() {
        //isUpdatePaused.set(true);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void destroy() {
        compositeDisposable.clear();
        super.destroy();
    }

    @Override
    public void listItemTorrentsClicked(long torrentId) {
        getView().startDetailsActivity(torrentId);
    }

    @Override
    public void listItemServersClicked(Server server) {
        ServerManager.getInstance().setActiveServer(server);
        //activeServer = ServerManager.getInstance().getActiveServer();
        getView().setServerName(server.getName());
        getView().setActiveServer(server);
        compositeDisposable.clear();
        startPeriodicUpdate();
    }

    @Override
    public void btAddServerClicked() {
        getView().startAddServerActivity(new Server());
    }

    @Override
    public void btManageServersClicked() {
        getView().startManageServersActivity();
    }

    @Override
    public void serversListVisible() {
        getView().setActiveServer(ServerManager.getInstance().getActiveServer());
    }

    private void startPeriodicUpdate() {


        if (TransmissionService.getApi() != null) {
            periodicUpdate = Observable.interval(0, 2, TimeUnit.SECONDS, Schedulers.io())
                    .flatMap(r -> TransmissionService.getApi().getTorrents(RequestHelper.getTorrentInfo()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {

                                List<Torrent> torrents = response.getArguments().getTorrents();
                                if (isViewAttached()) {
                                    getView().updateTorrentsList(torrents);
                                    updateTotalSpeed(torrents);
                                }
                                cachedTorrents = torrents;
                            },
                            throwable -> {
                                getView().showError("Connection error");
                                getView().updateTorrentsList(new ArrayList<>());
                                getView().setServerSpeed(0,0, false);
                            }
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
