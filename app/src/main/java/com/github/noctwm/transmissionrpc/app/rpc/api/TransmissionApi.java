package com.github.noctwm.transmissionrpc.app.rpc.api;

import com.github.noctwm.transmissionrpc.app.rpc.model.base.RpcRequest;
import com.github.noctwm.transmissionrpc.app.rpc.model.base.RpcResponse;
import com.github.noctwm.transmissionrpc.app.rpc.model.arguments.TorrentInfoResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TransmissionApi {

    @POST("./")
    Observable<RpcResponse<TorrentInfoResponse>> getTorrents(@Body RpcRequest rpcRequest);

}
