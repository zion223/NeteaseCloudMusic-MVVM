package com.netease.music.ui.page.discover;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.imooc.lib_api.model.banner.BannerBean;
import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.page.adapter.RecommendPlayListAdapter;
import com.netease.music.ui.state.DiscoverViewModel;

import java.util.ArrayList;

public class DiscoverFragment extends BaseFragment {


    private DiscoverViewModel mDiscoverViewModel;


    @Override
    protected void initViewModel() {
        mDiscoverViewModel = getFragmentViewModel(DiscoverViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_discover, BR.vm, mDiscoverViewModel)
                .addBindingParam(BR.bannerListener, bannerListener);
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

        //请求Banner数据
        mDiscoverViewModel.discoverRequest.requestBannerData();
        //请求歌单数据
        mDiscoverViewModel.discoverRequest.requestRecommendPlaylistData();

    }


    //banner组件的点击事件
    private OnItemClickListener bannerListener = position -> {
        if (mDiscoverViewModel.banners.get() != null) {
            BannerBean.BannersBean item = mDiscoverViewModel.banners.get().get(position);
            //歌曲
            if (item.getTargetType() == 4 || item.getTargetType() == 1) {
                long songId = item.getTargetId();
//                RequestCenter.getSongDetail(String.valueOf(songId), new DisposeDataListener() {
//                    @Override
//                    public void onSuccess(Object responseObj) {
//                        SongDetailBean songBean = (SongDetailBean) responseObj;
//                        //只有一首歌
//                        SongDetailBean.SongsBean bean = songBean.getSongs().get(0);
//                        String songPlayUrl = HttpConstants.getSongPlayUrl(bean.getId());
//                        AudioHelper.addAudio(getProxyActivity(), new AudioBean(String.valueOf(bean.getId()), songPlayUrl, bean.getName(), bean.getAr().get(0).getName(), bean.getAl().getName(), bean.getAl().getName(), bean.getAl().getPicUrl(), TimeUtil.getTimeNoYMDH(bean.getDt())));
//                    }
//
//                    @Override
//                    public void onFailure(Object reasonObj) {
//
//                    }
//                });
            } else if (item.getTargetType() == 10) {
                //专辑
                //getParentDelegate().getSupportDelegate().start(SongListDetailDelegate.newInstance(ALBUM, item.getTargetId()));
            }
            //网页
            if (item.getUrl() != null) {
                //getParentDelegate().getSupportDelegate().start(WebContainerDelegate.newInstance(item.getUrl()));
            }
        }

    };


}
