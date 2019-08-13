package com.github.noctwm.transmissionrpc.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.noctwm.R;
import com.github.noctwm.transmissionrpc.rpc.model.Torrent;
import com.github.noctwm.transmissionrpc.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.String.format;

public class TorrentAdapter extends BaseAdapter {

    private List<Torrent> torrents;
    private LayoutInflater layoutInflater;
    private Context context;

    public TorrentAdapter(Context context, List<Torrent> torrents) {
        this.torrents = torrents;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return torrents.size();
    }

    @Override
    public Object getItem(int position) {
        return torrents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return torrents.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_lv_torrents_main, parent, false);
        }

        Torrent torrent = (Torrent) getItem(position);
        ((TextView)view.findViewById(R.id.tv_name)).setText(torrent.getName());
        //todo change arrows
        ((TextView)view.findViewById(R.id.tv_speed_download)).setText(Utils.formatByteSpeed(context, torrent.getRateDownload()));   //"\u25bc " +
        ((TextView)view.findViewById(R.id.tv_speed_upload)).setText(Utils.formatByteSpeed(context, torrent.getRateUpload()));       //"\u25b2 " +

        String sizeCompleted;
        if (torrent.getLeftUntilDone() > 0) {

            view.findViewById(R.id.ll_eta).setVisibility(View.VISIBLE);

            float f = Math.round(torrent.getPercentDone() * 1000f) / 10f;
            String percentDone;
            if(f % 1 == 0) {
                percentDone = String.format(Locale.US, "%.0f", f) + " %";
            } else {
                percentDone = format(Locale.US, "%.1f", f) + " %";
            }

           // String percentDone = Math.round(torrent.getPercentDone() * 1000f) / 10f + " %";
            ((TextView)view.findViewById(R.id.tv_percent)).setText(percentDone);
            ((TextView)view.findViewById(R.id.tv_eta)).setText(Utils.formatDuration(context, torrent.getEta()));

            long downloaded = torrent.getSizeWhenDone() - torrent.getLeftUntilDone();
            sizeCompleted = format(context.getResources().getString(R.string.completed_string),
                    Utils.formatByteSize(context,downloaded), Utils.formatByteSize(context, torrent.getSizeWhenDone()));
        } else {
            view.findViewById(R.id.ll_eta).setVisibility(View.GONE);
            sizeCompleted = Utils.formatByteSize(context, torrent.getSizeWhenDone());
        }

        ((TextView)view.findViewById(R.id.tv_size_downloaded)).setText(sizeCompleted);
        ((TextView)view.findViewById(R.id.tv_size_uploaded)).setText(Utils.formatByteSize(context, torrent.getUploadedEver()));

        ((ProgressBar)view.findViewById(R.id.progress_bar)).setProgress((int)(torrent.getPercentDone() * 10000));


        return view;
    }

    void update(List<Torrent> torrents) {
        if (torrents == null) {
            this.torrents = new ArrayList<>();
        } else {
            this.torrents = torrents;
        }
    }
}
