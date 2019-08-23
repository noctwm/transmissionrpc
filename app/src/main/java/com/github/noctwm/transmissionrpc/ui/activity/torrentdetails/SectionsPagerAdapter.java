package com.github.noctwm.transmissionrpc.ui.activity.torrentdetails;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.github.noctwm.R;
import com.github.noctwm.transmissionrpc.ui.fragment.torrentdetails.TorrentInfoFragment;
import com.github.noctwm.transmissionrpc.ui.fragment.torrentdetails.TabFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private long torrentId;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.torrent_info_tab_details, R.string.torrent_info_tab};
    private final Context context;

    public SectionsPagerAdapter(Context context, FragmentManager fm, long torrentId) {
        super(fm);
        this.context = context;
        this.torrentId = torrentId;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0: return TorrentInfoFragment.getInstance(torrentId);
            case 1: return new TabFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }


}
