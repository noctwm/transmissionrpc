package com.github.noctwm.transmissionrpc;

import android.app.Application;

import com.github.noctwm.transmissionrpc.rpc.TransmissionService;
import com.github.noctwm.transmissionrpc.server.ServerManager;
import com.google.gson.Gson;

public class App extends Application {
    private static App instance;
    private Gson gson;



    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        gson = new Gson();
        ServerManager.initInstance(gson);
        TransmissionService.initInstance(gson);

    }



    public static App get() {
        return instance;
    }
}
