package com.netease.music.ui.page.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.netease.music.data.config.DiscoveryChannelEnum;
import com.netease.music.ui.page.cloud.CloudVillageFragment;
import com.netease.music.ui.page.discover.DiscoverFragment;
import com.netease.music.ui.page.mine.MineFragment;
import com.netease.music.ui.page.video.VideoTabFragment;

import org.jetbrains.annotations.NotNull;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private DiscoveryChannelEnum[] data;

    //https://www.jianshu.com/p/cfe74762edcc Androidx懒加载方案 在onResume()方法中进行网络请求
    public HomePagerAdapter(FragmentManager fm, DiscoveryChannelEnum[] channels) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.data = channels;
    }

    //初始化Fragment  在滑动时创建fragment 优化性能
    @NotNull
    @Override
    public Fragment getItem(int position) {
        int type = data[position].getValue();
        switch (type) {
            case DiscoveryChannelEnum.MINE_ID:
                return new MineFragment();
            case DiscoveryChannelEnum.DISCORY_ID:
                return new DiscoverFragment();
            case DiscoveryChannelEnum.YUNCUN_ID:
                return new CloudVillageFragment();
            case DiscoveryChannelEnum.VIDEO_ID:
                return new VideoTabFragment();
            default:
                break;
        }
        throw new IllegalArgumentException("cannot find fragment");
    }

    @Override
    public int getCount() {
        return data.length;
    }
}
