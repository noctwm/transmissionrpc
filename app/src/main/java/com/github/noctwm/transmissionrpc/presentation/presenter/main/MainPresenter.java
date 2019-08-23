package com.github.noctwm.transmissionrpc.presentation.presenter.main;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.github.noctwm.transmissionrpc.app.rpc.api.RequestHelper;
import com.github.noctwm.transmissionrpc.app.rpc.api.TransmissionService;
import com.github.noctwm.transmissionrpc.app.rpc.model.Torrent;
import com.github.noctwm.transmissionrpc.app.server.Server;
import com.github.noctwm.transmissionrpc.app.server.ServerManager;
import com.github.noctwm.transmissionrpc.presentation.view.main.MainView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private CompositeDisposable compositeDisposable;
    private Disposable periodicUpdate;

    private boolean isServersListVisible = false;

    public MainPresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(MainView view) {
        super.attachView(view);

        getViewState().updateServersList(ServerManager.getInstance().getAll());

        Server activeServer = ServerManager.getInstance().getActiveServer();

        if (activeServer != null) {
            getViewState().setServerName(activeServer.getName());
            //getViewState().highlightActiveServer(activeServer);
        } else {
            getViewState().clearServerName();
            getViewState().startAddServerActivity(new Server());
        }
    }

    public void onBtServersClick() {
        if (isServersListVisible) {
            getViewState().hideServersList();
        } else {
            getViewState().showServersList();
            Server server = ServerManager.getInstance().getActiveServer();
            if (server != null) {
                getViewState().highlightActiveServer(server);
            }
        }
        isServersListVisible = !isServersListVisible;
    }

    public void onDrawerToggleClick() {
        if (isServersListVisible) {
            Server server = ServerManager.getInstance().getActiveServer();
            if (server != null) {
                getViewState().highlightActiveServer(server);
            }
        }
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    public void listItemTorrentsClicked(long torrentId, String torrentName) {
        getViewState().startDetailsActivity(torrentId, torrentName);
    }

    public void onListItemServersClicked(Server server) {
        ServerManager.getInstance().setActiveServer(server);
        getViewState().setServerName(server.getName());
        getViewState().highlightActiveServer(server);
        getViewState().hideDrawer();
        compositeDisposable.clear();
        startPeriodicUpdate();
    }

    public void onAddServerClick() {
        getViewState().hideDrawer();
        getViewState().startAddServerActivity(new Server());
    }

    public void onManageServersClick() {
        getViewState().startManageServersActivity();
    }

    public void serversListVisible() {
        getViewState().highlightActiveServer(ServerManager.getInstance().getActiveServer());
    }

    public void startPeriodicUpdate() {
        if (periodicUpdate == null || periodicUpdate.isDisposed()) {
            if (TransmissionService.getApi() != null) {
                periodicUpdate = Observable.interval(0, 2, TimeUnit.SECONDS, Schedulers.io())
                        .flatMap(r -> TransmissionService.getApi().getTorrents(RequestHelper.getTorrentInfo()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                                    List<Torrent> torrents = response.getArguments().getTorrents();
                                    getViewState().updateTorrentsList(torrents);
                                    updateTotalSpeed(torrents);
                                },
                                throwable -> {
                                    getViewState().showError("Connection error");
                                    getViewState().updateTorrentsList(new ArrayList<>());
                                    getViewState().setServerSpeed(0,0, false);
                                }
                        );
                compositeDisposable.add(periodicUpdate);
            }
        }

    }

    public void stopPeriodicUpdate() {
        compositeDisposable.clear();
    }

    private void updateTotalSpeed(List<Torrent> torrents) {
        long downloadSpeed = 0;
        long uploadSpeed = 0;
        for (Torrent t : torrents) {
            downloadSpeed += t.getRateDownload();
            uploadSpeed += t.getRateUpload();
        }
        getViewState().setServerSpeed(downloadSpeed, uploadSpeed, true);
    }


}
