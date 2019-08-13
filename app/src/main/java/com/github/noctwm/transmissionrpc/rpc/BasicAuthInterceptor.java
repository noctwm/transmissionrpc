package com.github.noctwm.transmissionrpc.rpc;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BasicAuthInterceptor implements Interceptor {
    private String credentials;

    public BasicAuthInterceptor(String login, String password) {
        this.credentials = Credentials.basic(login, password);
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest = request.newBuilder()
                .header("Authorization", credentials).build();
        return chain.proceed(newRequest);
    }
}
