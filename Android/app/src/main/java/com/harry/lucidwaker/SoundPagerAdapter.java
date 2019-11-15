package com.harry.lucidwaker;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SoundPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.ringtones, R.string.my_music};
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final Context mContext;

    public SoundPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        mFragmentList.add(0, new RingtonesFragment());
        mFragmentList.add(1, new MyMusicFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
