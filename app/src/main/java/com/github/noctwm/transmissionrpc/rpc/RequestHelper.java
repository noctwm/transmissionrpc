package com.github.noctwm.transmissionrpc.rpc;

import com.github.noctwm.transmissionrpc.rpc.model.RpcRequest;
import com.github.noctwm.transmissionrpc.rpc.model.arguments.RequestArgs;

import java.util.ArrayList;

public class RequestHelper {

    private static final String[] TORRENT_INFO_FIELDS = new String[]{"id", "name", "status", "sizeWhenDone", "leftUntilDone", "rateDownload", "rateUpload",
            "uploadedEver", "percentDone", "eta"};

    private static final String[] TORRENT_DETAILS_FIELDS = new String[]{"name", "status", "totalSize", "sizeWhenDone", "rateDownload", "rateUpload", "leftUntilDone", "uploadedEver", "eta",
            "downloadDir", "creator", "comment", "dateCreated", "activityDate", "addedDate", "doneDate"};


    public static RpcRequest getTorrentInfo() {
        RequestArgs args = new RequestArgs();

        args.setFields(TORRENT_INFO_FIELDS);

        return new RpcRequest(args, "torrent-get");
    }

    public static RpcRequest getTorrentDetails(long id) {
        RequestArgs args = new RequestArgs();

        ArrayList<Long> ids = new ArrayList<>(1);
        ids.add(id);
        args.setIds(ids);

        args.setFields(TORRENT_DETAILS_FIELDS);

        return new RpcRequest(args, "torrent-get");
    }


}
