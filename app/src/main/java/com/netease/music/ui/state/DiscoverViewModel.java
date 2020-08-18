package com.netease.music.ui.state;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.imooc.lib_api.model.banner.BannerBean;
import com.netease.music.domain.request.DiscoverRequest;

import java.util.List;

public class DiscoverViewModel extends ViewModel {


    //banner数据
    public final ObservableField<List<BannerBean.BannersBean>> banners = new ObservableField<>();
    //banner中的Pic
    public final ObservableField<List<String>> bannersPic = new ObservableField<>();

    public DiscoverRequest discoverRequest = new DiscoverRequest();
}
