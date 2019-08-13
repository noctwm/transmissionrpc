package com.github.noctwm.transmissionrpc.rpc;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class SessionIdInterceptor implements Interceptor {

    private static final String SESSION_ID_HEADER = "X-Transmission-Session-Id";

    private static String sessionId;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request;

        if (sessionId == null) {
            request = chain.request();
        } else {
            request = chain.request()
                    .newBuilder()
                    .addHeader(SESSION_ID_HEADER, sessionId)
                    .build();
        }

        Response response = chain.proceed(request);

        if (response.code() == HttpURLConnection.HTTP_CONFLICT) {
            sessionId = response.header(SESSION_ID_HEADER);
            Request newRequest = request.newBuilder()
                    .removeHeader(SESSION_ID_HEADER)
                    .addHeader(SESSION_ID_HEADER, sessionId)
                    .build();
            return chain.proceed(newRequest);
        }
        return response;
    }
}
