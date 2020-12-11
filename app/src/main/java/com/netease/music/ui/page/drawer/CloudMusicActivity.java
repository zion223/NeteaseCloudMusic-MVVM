package com.netease.music.ui.page.drawer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.netease.lib_api.model.notification.UserCloudBean;
import com.netease.lib_api.model.song.DailyRecommendSongsBean;
import com.netease.lib_api.model.song.SongDetailBean;
import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.utils.Utils;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.page.adapter.PlayMusicListAdapter;
import com.netease.music.ui.state.CloudMusicViewModel;

import java.util.ArrayList;

public class CloudMusicActivity extends BaseActivity {

    private static final long GB = 1024 * 1024 * 1024;
    private CloudMusicViewModel mCloudMusicViewModel;

    @Override
    protected void initViewModel() {
        mCloudMusicViewModel = getActivityScopeViewModel(CloudMusicViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_user_cloud, BR.vm, mCloudMusicViewModel)
                .addBindingParam(BR.proxy, new ClickProxy());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //观察数据
        mCloudMusicViewModel.musicRequest.getUserCloudLiveData().observe(this, userCloudBean -> {
            //云盘已使用容量和全部容量
            double leftSize = (double) userCloudBean.getSize() / GB;

            mCloudMusicViewModel.cloudSize.set(String.format("%.1f", leftSize) + " G/ " + userCloudBean.getMaxSize() / GB + "G");
            mCloudMusicViewModel.userCloud.set(userCloudBean);
            //获取音乐数据
            ArrayList<UserCloudBean.CloudSong> data = userCloudBean.getData();
            ArrayList<DailyRecommendSongsBean> songData = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                songData.add(data.get(i).getSimpleSong());
            }

            View headerView = LayoutInflater.from(Utils.getApp()).inflate(R.layout.item_music_header, null, false);
            ((TextView) headerView.findViewById(R.id.play_all_number)).setText(userCloudBean.getCount());
            PlayMusicListAdapter adapter = new PlayMusicListAdapter(songData, true);
            adapter.setHeaderView(headerView);
            mCloudMusicViewModel.adapter.set(adapter);
            //停止显示加载按钮
            mCloudMusicViewModel.loadingVisible.set(false);
        });
        //请求数据
        mCloudMusicViewModel.musicRequest.requestUserCloudData();

    }


    public class ClickProxy {
        public void back() {
            finish();
        }
    }
}
