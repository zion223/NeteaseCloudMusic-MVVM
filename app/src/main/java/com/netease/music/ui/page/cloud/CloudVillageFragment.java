package com.netease.music.ui.page.cloud;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.imooc.lib_api.HttpConstants;
import com.imooc.lib_api.model.playlist.DailyRecommendBean;
import com.imooc.lib_api.model.user.UserEventBean;
import com.imooc.lib_api.model.user.UserEventJsonBean;
import com.imooc.lib_common_ui.utils.GsonUtil;
import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.state.CloudVillageFragmentViewModel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class CloudVillageFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    private CloudVillageFragmentViewModel mViewModel;

    @Override
    protected void initViewModel() {
        mViewModel = getFragmentViewModel(CloudVillageFragmentViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_friend, BR.vm, mViewModel)
                .addBindingParam(BR.refreshListener, this)
                .addBindingParam(BR.loadMoreListener, this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel.eventRequest.getEventLiveData().observe(this, mainEventBean -> {

            EventAdapter eventAdapter = new EventAdapter(mainEventBean.getEvent());
            eventAdapter.addChildClickViewIds(R.id.rl_share, R.id.avatr_view);
            eventAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                if (view.getId() == R.id.rl_share) {
                    UserEventBean.EventsBean event = (UserEventBean.EventsBean) adapter.getItem(position);
                    UserEventJsonBean jsonBean = GsonUtil.fromJSON(event.getJson(), UserEventJsonBean.class);
                    if (jsonBean.getSong() != null && !TextUtils.isEmpty(jsonBean.getSong().getName())) {
                        //播放歌曲
                        DailyRecommendBean.RecommendBean item = jsonBean.getSong();
                        String songPlayUrl = HttpConstants.getSongPlayUrl(item.getId());
                        showLongToast("添加歌曲" + jsonBean.getSong().getName());
                        //AudioHelper.addAudio(new AudioBean(String.valueOf(item.getId()), songPlayUrl, item.getName(), item.getArtists().get(0).getName(), item.getAlbum().getName(), item.getAlbum().getName(), item.getAlbum().getPicUrl(), TimeUtil.getTimeNoYMDH(item.getDuration())));
                    } else if (jsonBean.getAlbum() != null) {
                        //查看专辑
                        //mDelegate.getSupportDelegate().start(SongListDetailDelegate.newInstance(ALBUM, jsonBean.getAlbum().getId()));
                        showLongToast("查看专辑" + jsonBean.getAlbum().getName());
                    } else if (jsonBean.getPlaylist() != null) {
                        //查看歌单
                        showLongToast("查看歌单" + jsonBean.getPlaylist().getName());

                        //mDelegate.getSupportDelegate().start(SongListDetailDelegate.newInstance(PLAYLIST, jsonBean.getPlaylist().getId()));
                    }
                } else if (view.getId() == R.id.avatr_view) {
                    //跳入用户详情
                    //mDelegate.getSupportDelegate().start(UserDetailDelegate.newInstance(item.getUser().getUserId()));

                }
            });
            mViewModel.adapter.set(eventAdapter);
            //停止显示加载动画
            mViewModel.loadingVisible.set(false);
        });

        mViewModel.eventRequest.requestUserEventData();

    }

    //默认都加载成功
    @SuppressLint("CheckResult")
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(aLong -> mViewModel.reloadState.set(true));
    }

    @SuppressLint("CheckResult")
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(aLong -> mViewModel.reloadState.set(true));
    }
}
