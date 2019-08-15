package com.github.noctwm.transmissionrpc.ui.torrentdetails;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.noctwm.R;
import com.github.noctwm.transmissionrpc.rpc.model.Torrent;
import com.github.noctwm.transmissionrpc.ui.main.MainActivity;

public class DetailsActivity extends AppCompatActivity implements DetailsContract.DetailsView {

    private DetailsPresenter presenter;
    private SectionsPagerAdapter adapter;

    private long torrentId;
    private ViewPager viewPager;
    private Torrent cachedTorrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torrent_details);


        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            getSupportActionBar().setElevation(0);
        }


        viewPager = findViewById(R.id.view_pager);
        adapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabs = findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        torrentId = intent.getLongExtra(MainActivity.EXTRA_TORRENT_ID, 0);
        attachPresenter();
    }

    @Override
    public void update(Torrent torrent) {
        BaseFragment fragment = adapter.getCurrentFragment();
        if (fragment != null) {
            fragment.update(torrent);
        } else {
            cachedTorrent = torrent;
        }

    }

    //for test
    public Torrent getUpdate() {
        return cachedTorrent;
    }

    @Override
    public void showError(String error) {
        Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public long getTorrentID() {
        return torrentId;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.viewIsReady();
    }

    @Override
    protected void onPause() {
        presenter.viewIsPaused();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (isChangingConfigurations()) {
            presenter.detachView();
        } else {
            presenter.destroy();
        }
        super.onDestroy();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    private void attachPresenter() {
        presenter = (DetailsPresenter) getLastCustomNonConfigurationInstance();
        if (presenter == null) {
            presenter = new DetailsPresenter();
        }
        presenter.attachView(this);
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
