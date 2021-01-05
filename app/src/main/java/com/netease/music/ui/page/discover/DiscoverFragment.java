package com.netease.music.ui.page.discover;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.netease.lib_api.model.banner.BannerBean;
import com.netease.lib_api.model.song.AudioBean;
import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.lib_audio.app.AudioHelper;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.data.config.TypeEnum;
import com.netease.music.ui.page.adapter.AlbumSongAdapter;
import com.netease.music.ui.page.adapter.RecommendPlayListAdapter;
import com.netease.music.ui.page.discover.album.NewAlbumActivity;
import com.netease.music.ui.page.discover.daily.DailyRecommendActivity;
import com.netease.music.ui.page.discover.radio.RadioActivity;
import com.netease.music.ui.page.discover.square.detail.SongListDetailActivity;
import com.netease.music.ui.state.DiscoverViewModel;

import java.util.ArrayList;

public class DiscoverFragment extends BaseFragment {


    private DiscoverViewModel mDiscoverViewModel;


    @Override
    protected void initViewModel() {
        mDiscoverViewModel = getFragmentScopeViewModel(DiscoverViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_discover, BR.vm, mDiscoverViewModel)
                .addBindingParam(BR.bannerListener, bannerListener)
                .addBindingParam(BR.proxy, new ClickProxy())
                .addBindingParam(BR.albumSongAdapter, new AlbumSongAdapter(getContext()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDiscoverViewModel.discoverRequest.getBannerLiveData().observe(this, bannerBean -> {
            mDiscoverViewModel.banners.set(bannerBean.getBanners());
            final ArrayList<String> bannerPic = new ArrayList<>();
            for (BannerBean.BannersBean item : bannerBean.getBanners()) {
                bannerPic.add(item.getPic());
            }
            mDiscoverViewModel.bannersPic.set(bannerPic);
        });

        //推荐歌单数据
        mDiscoverViewModel.discoverRequest.getRecommendPlaylistLiveData().observe(this, recommendBeans -> {
            final RecommendPlayListAdapter playListAdapter = new RecommendPlayListAdapter(getContext());
            playListAdapter.submitList(recommendBeans);
            mDiscoverViewModel.playListAdapter.set(playListAdapter);
        });
        //新歌和新碟的数据 新碟在前三个 新歌在后三个
        mDiscoverViewModel.discoverRequest.getAlbumOrSongLiveData().observe(this, albumOrSongBeans -> {
            //获取全部的数据
            mDiscoverViewModel.albumOrSongLiveData.set(albumOrSongBeans);
            //前三位是新碟的数据
            mDiscoverViewModel.currentAlbumOrSongLiveData.set(albumOrSongBeans.subList(0, 3));
        });
        //Banner中的歌曲
        mDiscoverViewModel.discoverRequest.getSongDetailLiveData().observe(this, songDetailBean -> AudioHelper.addAudio(AudioBean.convertSongToAudioBean(songDetailBean)));
        //请求Banner数据
        mDiscoverViewModel.discoverRequest.requestBannerData();
        //请求歌单数据
        mDiscoverViewModel.discoverRequest.requestRecommendPlaylistData();
        //请求新碟上架和新歌速递数据
        mDiscoverViewModel.discoverRequest.requestAlbumAndSongData();

    }


    //banner组件的点击事件
    private OnItemClickListener bannerListener = position -> {
        if (mDiscoverViewModel.banners.get() != null) {
            BannerBean.BannersBean item = mDiscoverViewModel.banners.get().get(position);
            //歌曲
            if (item.getTargetType() == 4 || item.getTargetType() == 1) {
                long songId = item.getTargetId();
                mDiscoverViewModel.discoverRequest.requestSongDetailData(songId);
            } else if (item.getTargetType() == 10) {
                //专辑
                SongListDetailActivity.startActivity(getContext(), TypeEnum.ALBUM_ID, item.getTargetId(), "");
            }
            //网页
            if (item.getUrl() != null) {
                //TODO
                //getParentDelegate().getSupportDelegate().start(WebContainerDelegate.newInstance(item.getUrl()));
            }
        }

    };


    public class ClickProxy {

        public void changeAlbumOrSong() {
            if (mDiscoverViewModel.type.get().getValue() == TypeEnum.ALBUM.getValue()) {
                //切换到新歌
                mDiscoverViewModel.type.set(TypeEnum.SONG);
                mDiscoverViewModel.currentAlbumOrSongLiveData.set(mDiscoverViewModel.albumOrSongLiveData.get().subList(3, 6));
            } else {
                //切换到新碟
                mDiscoverViewModel.type.set(TypeEnum.ALBUM);
                mDiscoverViewModel.currentAlbumOrSongLiveData.set(mDiscoverViewModel.albumOrSongLiveData.get().subList(0, 3));
            }
        }

        public void clickAlbumOrSongDetail() {
            //新歌推荐 TODO
            if (mDiscoverViewModel.type.get() == TypeEnum.SONG) {

            } else {
                //新碟上架
                startActivity(new Intent(getContext(), NewAlbumActivity.class));
            }
        }

        public void dailyRecommend() {
            startActivity(new Intent(getContext(), DailyRecommendActivity.class));
        }

        public void radio() {
            startActivity(new Intent(getContext(), RadioActivity.class));
        }
    }


}
