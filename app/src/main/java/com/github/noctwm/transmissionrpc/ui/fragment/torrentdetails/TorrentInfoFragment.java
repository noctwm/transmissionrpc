package com.github.noctwm.transmissionrpc.ui.fragment.torrentdetails;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.github.noctwm.R;
import com.github.noctwm.transmissionrpc.app.rpc.model.Torrent;
import com.github.noctwm.transmissionrpc.presentation.presenter.torrentdetails.TorrentInfoPresenter;
import com.github.noctwm.transmissionrpc.presentation.view.torrentdetails.TorrentInfoView;
import com.github.noctwm.transmissionrpc.common.Utils;

import static com.github.noctwm.transmissionrpc.ui.activity.main.MainActivity.EXTRA_TORRENT_ID;

public class TorrentInfoFragment extends MvpAppCompatFragment implements TorrentInfoView {

    private TextView tvCompleted;
    private Context context;
    private TextView tvUploaded;
    private TextView tvRateDownload;
    private TextView tvRateUpload;
    private TextView tvEta;
    private TextView tvTotalSize;
    private TextView tvLocation;
    private TextView tvCreator;
    private TextView tvDateCreated;
    private TextView tvActivityDate;
    private TextView tvComment;
    private TextView tvAddedDate;
    private TextView tvDoneDate;

    @InjectPresenter
    TorrentInfoPresenter presenter;

    @ProvidePresenter
    TorrentInfoPresenter provideDetailsPresenter() {
        if (getArguments() != null) {
            return new TorrentInfoPresenter(getArguments().getLong(EXTRA_TORRENT_ID, 0));
        } else {
            throw new IllegalArgumentException("Torrent ID must be provided");
        }
    }


    public static TorrentInfoFragment getInstance(long torrentId) {
        Bundle args = new Bundle();
        args.putLong(EXTRA_TORRENT_ID, torrentId);

        TorrentInfoFragment fragment = new TorrentInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_torrent_details, container, false);
        tvCompleted = view.findViewById(R.id.tv_completed_torrent_details);
        tvUploaded = view.findViewById(R.id.tv_uploaded_torrent_details);
        tvRateDownload = view.findViewById(R.id.tv_download_speed_torrent_details);
        tvRateUpload = view.findViewById(R.id.tv_upload_speed_torrent_details);
        tvEta = view.findViewById(R.id.tv_eta_torrent_details);
        tvActivityDate = view.findViewById(R.id.tv_last_activity_torrent_details);
        tvTotalSize = view.findViewById(R.id.tv_total_size_torrent_details);
        tvLocation = view.findViewById(R.id.tv_location_torrent_details);
        tvCreator = view.findViewById(R.id.tv_creator_torrent_details);
        tvDateCreated = view.findViewById(R.id.tv_date_created_torrent_details);
        tvComment = view.findViewById(R.id.tv_comment_torrent_details);
        tvAddedDate = view.findViewById(R.id.tv_added_date_torrent_details);
        tvDoneDate = view.findViewById(R.id.tv_done_date_torrent_details);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (presenter != null) {
            if (isVisibleToUser) {
                presenter.startPeriodicUpdate();
            } else {
                presenter.stopPeriodicUpdate();
            }

            //presenter.stopPeriodicUpdate();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.startPeriodicUpdate();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.stopPeriodicUpdate();
    }


    @Override
    public void onDetach() {
        context = null;
        super.onDetach();
    }

    @Override
    public void update(Torrent torrent) {
        String sizeCompleted;
        if (torrent.getLeftUntilDone() > 0) {
            long downloaded = torrent.getSizeWhenDone() - torrent.getLeftUntilDone();
            sizeCompleted = String.format(context.getResources().getString(R.string.completed_string),
                    Utils.formatByteSize(context, downloaded), Utils.formatByteSize(context, torrent.getSizeWhenDone()));
        } else {
            sizeCompleted = Utils.formatByteSize(context, torrent.getSizeWhenDone());
        }
        tvCompleted.setText(sizeCompleted);
        tvUploaded.setText(Utils.formatByteSize(context, torrent.getUploadedEver()));
        tvRateDownload.setText(Utils.formatByteSpeed(context, torrent.getRateDownload()));
        tvRateUpload.setText(Utils.formatByteSpeed(context, torrent.getRateUpload()));
        tvEta.setText(Utils.formatDuration(context, torrent.getEta()));
        tvActivityDate.setText(DateUtils.getRelativeTimeSpanString((torrent.getActivityDate() * 1000) - 5000));
        tvTotalSize.setText(Utils.formatByteSize(context, torrent.getTotalSize()));
        tvLocation.setText(torrent.getDownloadDir());
        tvCreator.setText(torrent.getCreator());
        if (torrent.getDateCreated() > 0) {
            tvDateCreated.setText(DateUtils.formatDateTime(context, torrent.getDateCreated() * 1000, DateUtils.FORMAT_SHOW_DATE |
                    DateUtils.FORMAT_SHOW_YEAR));
        }
        tvComment.setText(torrent.getComment());
        if (torrent.getAddedDate() > 0) {
            tvAddedDate.setText(DateUtils.formatDateTime(context, torrent.getAddedDate() * 1000, DateUtils.FORMAT_SHOW_DATE |
                    DateUtils.FORMAT_SHOW_YEAR));
        }
        if (torrent.getDoneDate() > 0) {
            tvDoneDate.setText(DateUtils.formatDateTime(context, torrent.getDoneDate() * 1000, DateUtils.FORMAT_SHOW_DATE |
                    DateUtils.FORMAT_SHOW_YEAR));
        }

    }

    @Override
    public void showError(String error) {
        //todo show error
    }
}
