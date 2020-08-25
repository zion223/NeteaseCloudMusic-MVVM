package com.netease.music.ui.page.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.netease.music.data.config.CHANNEL;
import com.netease.music.ui.page.cloud.CloudVillageFragment;
import com.netease.music.ui.page.discover.DiscoverFragment;
import com.netease.music.ui.page.mine.MineFragment;
import com.netease.music.ui.page.video.VideoTabFragment;

import org.jetbrains.annotations.NotNull;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private CHANNEL[] data;

    //https://www.jianshu.com/p/cfe74762edcc Androidx懒加载方案 在onResume()方法中进行网络请求
    public HomePagerAdapter(FragmentManager fm, CHANNEL[] channels) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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
