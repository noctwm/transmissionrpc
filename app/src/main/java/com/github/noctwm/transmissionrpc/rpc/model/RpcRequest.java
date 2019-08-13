package com.github.noctwm.transmissionrpc.rpc.model;

import com.github.noctwm.transmissionrpc.rpc.model.arguments.RequestArgs;

public class RpcRequest {

    private RequestArgs arguments;

    private String method;

    private int tag;

    public RpcRequest(RequestArgs arguments, String method) {
        this.arguments = arguments;
        this.method = method;
    }

    public RpcRequest() {
    }

    public RequestArgs getArguments() {
        return arguments;
    }

    public void setArguments(RequestArgs arguments) {
        this.arguments = arguments;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

}