package com.github.noctwm.transmissionrpc.app.rpc.api;

import androidx.annotation.Nullable;

import com.github.noctwm.transmissionrpc.app.server.Server;
import com.github.noctwm.transmissionrpc.app.server.ServerManager;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransmissionService implements ServerManager.OnActiveServerChangedListener {

    private static TransmissionService service;
    private static TransmissionApi api;
    private Gson gson;
    private Server activeServer;




    private TransmissionService(Gson gson) {

        this.gson = gson;

        ServerManager.getInstance().addOnActiveServerChangedListener(this);
        activeServer = ServerManager.getInstance().getActiveServer();
        buildApi();

    }

    public static void initInstance(Gson gson) {
        if (service == null) {
            service = new TransmissionService(gson);
        }
    }


    @Nullable
    public static TransmissionApi getApi() {
        return api;
    }

    private void buildApi() {

        if (activeServer != null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            if (activeServer.isUseAuthentication()) {
                builder.addInterceptor(new BasicAuthInterceptor(activeServer.getLogin(), activeServer.getPassword()));
            }
            builder.addInterceptor(new SessionIdInterceptor())
                    .callTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS);

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
            OkHttpClient client = builder.build();

            StringBuilder sb = new StringBuilder();
            sb.append("http://")
                    .append(activeServer.getHost())
                    .append(':')
                    .append(activeServer.getPort());
            if (!activeServer.getRpcPath().startsWith("/")) {
                sb.append('/');
            }
            sb.append(activeServer.getRpcPath());
            if (!activeServer.getRpcPath().endsWith("/")) {
                sb.append('/');
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(sb.toString())
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            api = retrofit.create(TransmissionApi.class);
        }
    }

    @Override
    public void serverChanged(Server newServer) {
        this.activeServer = ServerManager.getInstance().getActiveServer();
        buildApi();
    }
}
