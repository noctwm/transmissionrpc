package com.github.noctwm.transmissionrpc.rpc.model.arguments;

import com.github.noctwm.transmissionrpc.rpc.model.Torrent;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TorrentInfoResponse {

    @SerializedName("torrents")
    private List<Torrent> torrents = null;

    public List<Torrent> getTorrents() {
        return torrents;
    }

    public void setTorrents(List<Torrent> torrents) {
        this.torrents = torrents;
    }
}
