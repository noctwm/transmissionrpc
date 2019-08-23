package com.github.noctwm.transmissionrpc.ui.activity.torrentdetails;


import android.os.Bundle;
import android.view.MenuItem;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.github.noctwm.transmissionrpc.presentation.presenter.torrentdetails.TorrentDetailsPresenter;
import com.github.noctwm.transmissionrpc.presentation.view.torrentdetails.TorrentDetailsView;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;

import com.github.noctwm.R;

import static com.github.noctwm.transmissionrpc.ui.activity.main.MainActivity.EXTRA_TORRENT_ID;
import static com.github.noctwm.transmissionrpc.ui.activity.main.MainActivity.EXTRA_TORRENT_NAME;

public class TorrentDetailsActivity extends MvpAppCompatActivity implements TorrentDetailsView {

    @InjectPresenter
    TorrentDetailsPresenter presenter;

    private SectionsPagerAdapter adapter;

    private ViewPager viewPager;
    private String torrentName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torrent_details);


        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            getSupportActionBar().setElevation(0);
        }

        long torrentId = getIntent().getLongExtra(EXTRA_TORRENT_ID, 0);
        torrentName = getIntent().getStringExtra(EXTRA_TORRENT_NAME);
        adapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), torrentId);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);

        TabLayout tabs = findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void setTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(torrentName);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
