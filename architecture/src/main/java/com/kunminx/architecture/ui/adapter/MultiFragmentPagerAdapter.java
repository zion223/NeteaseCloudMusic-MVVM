package com.kunminx.architecture.ui.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MultiFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();

    public MultiFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void init(List<Fragment> fragmentList) {
        fragments.clear();
        fragments.addAll(fragmentList);
    }

    @Override
    public Fragment getItem(int i) {
        if (fragments != null && i < fragments.size()) {
            return fragments.get(i);
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

}

