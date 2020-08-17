package com.netease.music.ui.page.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.netease.music.data.config.CHANNEL;
import com.netease.music.ui.page.CloudVillageFragment;
import com.netease.music.ui.page.DiscoverFragment;
import com.netease.music.ui.page.MineFragment;
import com.netease.music.ui.page.VideoTabFragment;

import org.jetbrains.annotations.NotNull;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private CHANNEL[] data;

    public HomePagerAdapter(FragmentManager fm, CHANNEL[] channels) {
        super(fm);
        this.data = channels;
    }

    //初始化Fragment  在滑动时创建fragment 优化性能
    @NotNull
    @Override
    public Fragment getItem(int position) {
        int type = data[position].getValue();
        switch (type) {
            case CHANNEL.MINE_ID:
                return new MineFragment();
            case CHANNEL.DISCORY_ID:
                return new DiscoverFragment();
            case CHANNEL.YUNCUN_ID:
                return new CloudVillageFragment();
            case CHANNEL.VIDEO_ID:
                return new VideoTabFragment();
        }
        throw new IllegalArgumentException("cannot find fragment");
    }

    @Override
    public int getCount() {
        return data.length;
    }
}
