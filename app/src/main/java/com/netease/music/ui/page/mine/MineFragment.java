package com.netease.music.ui.page.mine;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.page.adapter.MultiplePlaylistAdapter;
import com.netease.music.ui.state.MineViewModel;

public class MineFragment extends BaseFragment {


    private MineViewModel mMineViewModel;

    @Override
    protected void initViewModel() {
        mMineViewModel = getFragmentViewModel(MineViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_mine, BR.vm, mMineViewModel)
                .addBindingParam(BR.proxy, new ClickProxy());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //用户歌单
        mMineViewModel.mineRequest.getUserPlaylist().observe(this, playlistBeans -> {

            mMineViewModel.playlistAdapter.set(new MultiplePlaylistAdapter(getContext(), playlistBeans));
            mMineViewModel.loadingVisible.set(false);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mMineViewModel.mineRequest.requestUserPlaylist(mMineViewModel.user.get().getProfile().getUserId());
    }


    public class ClickProxy {

        //本地音乐
        public void localMusic() {

        }


        //我的电台
        public void myRadio() {

        }


        //我的收藏
        public void myCollect() {

        }
    }
}
