package com.github.noctwm.transmissionrpc.rpc;

import com.github.noctwm.transmissionrpc.rpc.model.RpcRequest;
import com.github.noctwm.transmissionrpc.rpc.model.RpcResponse;
import com.github.noctwm.transmissionrpc.rpc.model.arguments.TorrentInfoResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TransmissionApi {

    @POST("./")
    Observable<RpcResponse<TorrentInfoResponse>> getTorrents(@Body RpcRequest rpcRequest);

}
