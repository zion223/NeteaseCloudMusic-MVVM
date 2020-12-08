package com.netease.music.ui.page.discover.daily;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.material.appbar.AppBarLayout;
import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.utils.BarUtils;
import com.kunminx.architecture.utils.Utils;
import com.netease.lib_api.model.song.AudioBean;
import com.netease.lib_api.model.song.SongDetailBean;
import com.netease.lib_audio.app.AudioHelper;
import com.netease.lib_common_ui.appbar.AppBarStateChangeListener;
import com.netease.lib_common_ui.utils.StatusBarUtil;
import com.netease.lib_image_loader.app.ImageLoaderManager;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.state.DailyRecommendViewModel;

import java.util.ArrayList;
import java.util.List;

//每日推荐界面
public class DailyRecommendActivity extends BaseActivity {

    private DailyRecommendViewModel mViewModel;
    private int minDistance;
    private int deltaDistance;

    @Override
    protected void initViewModel() {
        mViewModel = getActivityScopeViewModel(DailyRecommendViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_daily_recommend, BR.vm, mViewModel)
                .addBindingParam(BR.offsetListener, listener)
                .addBindingParam(BR.proxy, new ClickProxy());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);

        minDistance = StatusBarUtil.dip2px(Utils.getApp(), 55);
        deltaDistance = StatusBarUtil.dip2px(Utils.getApp(), 200) - minDistance;

        DailyRecommendAdapter dailyRecommendAdapter = new DailyRecommendAdapter(null);
        dailyRecommendAdapter.setOnItemClickListener((adapter, view, position) -> {
            //播放音乐
            SongDetailBean.SongsBean entity = (SongDetailBean.SongsBean) adapter.getItem(position);
            AudioHelper.addAudio(AudioBean.convertSongToAudioBean(entity));
        });
        mViewModel.recommendRequest.getDailRecommendLiveData().observe(this, dailyRecommendBean -> {
            dailyRecommendAdapter.addData(dailyRecommendBean.getData().getDailySongs());
            mViewModel.adapter.set(dailyRecommendAdapter);
            //延迟1s显示加载动画
            new Handler().postDelayed(() -> mViewModel.loadingVisible.set(false), 1000);
            mViewModel.recommendMusic.set(dailyRecommendBean.getData().getDailySongs());

        });
        //请求数据
        mViewModel.recommendRequest.requestDailyRecommendMusic();

    }

    AppBarStateChangeListener listener = new AppBarStateChangeListener() {
        @Override
        public void onStateChanged(AppBarLayout appBarLayout, State state) {
            if (state == State.COLLAPSED) {
                mViewModel.leftTitleAlpha.set(255f);
                mViewModel.leftTitleVisiable.set(true);
            }
        }

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout) {
            View playView = appBarLayout.getRootView().findViewById(R.id.rl_play);
            float alphaPercent = (float) (playView.getTop() - minDistance) / (float) deltaDistance;
            int alpha = (int) (alphaPercent * 255);
            //背景图片
            mViewModel.coverImgAlpha.set(alpha);
            //日期
            mViewModel.textAlpha.set(alphaPercent);
            if (alphaPercent < 0.2f) {
                float leftTitleAlpha = (1.0f - alphaPercent / 0.2f);
                mViewModel.leftTitleAlpha.set(leftTitleAlpha);
                mViewModel.leftTitleVisiable.set(true);
            } else {
                mViewModel.leftTitleAlpha.set(0f);
                mViewModel.leftTitleVisiable.set(true);
            }
        }
    };

    public class ClickProxy {
        //返回
        public void back() {
            finish();
        }

        //播放全部音乐
        public void playAll() {
            if (mViewModel.recommendMusic.get() != null && mViewModel.recommendMusic.get().size() > 0) {
                final ArrayList<AudioBean> audioList = new ArrayList<>();
                int size = mViewModel.recommendMusic.get().size();
                for (int i = 0; i < size; i++) {
                    audioList.add(AudioBean.convertSongToAudioBean(mViewModel.recommendMusic.get().get(i)));
                }
                AudioHelper.addAudio(audioList);
            }
        }
    }


    private static class DailyRecommendAdapter extends BaseQuickAdapter<SongDetailBean.SongsBean, BaseViewHolder> {

        private ImageLoaderManager manager;

        DailyRecommendAdapter(@Nullable List<SongDetailBean.SongsBean> data) {
            super(R.layout.item_top_song, data);
            manager = ImageLoaderManager.getInstance();
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, SongDetailBean.SongsBean item) {
            manager.displayImageForCorner(helper.getView(R.id.iv_song_img), item.getAl().getPicUrl());
            helper.setText(R.id.viewpager_list_toptext, item.getName());
            helper.setText(R.id.viewpager_list_bottom_text, item.getAr().get(0).getName() + " - " + item.getAl().getName());
            //推荐原因
            if (TextUtils.isEmpty(item.getRecommendReason())) {
                helper.setVisible(R.id.viewpager_list_reason, false);
            } else {
                helper.setText(R.id.viewpager_list_reason, item.getRecommendReason());
            }
        }
    }
}
