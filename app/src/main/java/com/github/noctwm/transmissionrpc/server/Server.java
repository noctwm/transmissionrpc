package com.github.noctwm.transmissionrpc.server;

import java.util.Objects;
import java.util.UUID;

public class Server {

    public static final String DEFAULT_RPC_PATH = "/transmission/rpc";
    public static final int DEFAULT_RPC_PORT = 9091;

    private UUID id;
    private String name;
    private String host;
    private int port = DEFAULT_RPC_PORT;
    private boolean useAuthentication;
    private String login;
    private String password;
    private String rpcPath = DEFAULT_RPC_PATH;
    private int positionInList;



    public Server() {
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isUseAuthentication() {
        return useAuthentication;
    }

    public void setUseAuthentication(boolean useAuthentication) {
        this.useAuthentication = useAuthentication;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRpcPath() {
        return rpcPath;
    }

    public void setRpcPath(String rpcUrl) {
        this.rpcPath = rpcUrl;
    }

    public int getPositionInList() {
        return positionInList;
    }

    public void setPositionInList(int positionInList) {
        this.positionInList = positionInList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return id.equals(server.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
