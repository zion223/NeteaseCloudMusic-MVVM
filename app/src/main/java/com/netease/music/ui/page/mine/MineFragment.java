package com.netease.music.ui.page.mine;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.lib_api.model.room.AppDatabase;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.page.adapter.MultiplePlaylistAdapter;
import com.netease.music.ui.page.mine.radio.MyRadioActivity;
import com.netease.music.ui.state.MineViewModel;

public class MineFragment extends BaseFragment {


    private MineViewModel mMineViewModel;

    @Override
    protected void initViewModel() {
        mMineViewModel = getFragmentScopeViewModel(MineViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_mine, BR.vm, mMineViewModel)
                .addBindingParam(BR.proxy, new ClickProxy());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //最近播放的音乐数量  实时更新
        AppDatabase.getInstance(getContext()).getLatestSongDao().getRecentSongSize().observe(getViewLifecycleOwner(), size -> {
            mMineViewModel.recentPlaySongSize.set(size);
        });

        //TODO tip :
        // getViewLifeCycleOwner 是 2020 年新增的特性，
        // 主要是为了解决 getView() 的生命长度 比 fragment 短（仅存活于 onCreateView 之后和 onDestroyView 之前），
        // 导致某些时候 fragment 其他成员还活着，但 getView() 为 null 的 生命周期安全问题，
        // 也即，在 fragment 的场景下，请使用 getViewLifeCycleOwner 来作为 liveData 的观察者。
        // Activity 则不用改变。

        //用户歌单
        mMineViewModel.mineRequest.getUserPlaylistLiveData().observe(getViewLifecycleOwner(), playlistBeans -> {

            mMineViewModel.playlistAdapter.set(new MultiplePlaylistAdapter(getContext(), playlistBeans));
            mMineViewModel.loadingVisible.set(false);
        });

    }

    //https://www.jianshu.com/p/cfe74762edcc Androidx懒加载方案 在onResume()方法中进行网络请求
    @Override
    public void onResume() {
        super.onResume();
        mMineViewModel.mineRequest.requestUserPlaylist(mMineViewModel.user.get().getProfile().getUserId());
    }


    public class ClickProxy {

        //本地音乐
        public void localMusic() {
            showShortToast(R.string.function_not_completed);
        }


        //我的电台
        public void myRadio() {
            startActivity(new Intent(getContext(), MyRadioActivity.class));
        }


        //我的收藏
        public void myCollect() {
            showShortToast(R.string.function_not_completed);
        }

        //最近播放
        public void recentPlay() {
            showShortToast(R.string.function_not_completed);
        }
    }
}
