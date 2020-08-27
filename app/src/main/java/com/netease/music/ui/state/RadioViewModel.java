package com.netease.music.ui.state;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.netease.lib_api.model.dj.DjRecommendBean;
import com.netease.music.domain.request.RadioRequest;
import com.netease.music.ui.state.load.BaseLoadingViewModel;

import java.util.List;

public class RadioViewModel extends BaseLoadingViewModel {

    //电台界面的banner数据
    public final ObservableField<List<String>> bannersPic = new ObservableField<>();
    //推荐电台数据
    public final ObservableField<List<DjRecommendBean.DjRadiosBean>> recommendRadio = new ObservableField<>();
    //当前显示的推荐推荐
    public final ObservableField<List<DjRecommendBean.DjRadiosBean>> currentRecommendRadio = new ObservableField<>();
    //当前推荐电台索引
    public final ObservableInt currentIndex = new ObservableInt();


    public final RadioRequest request = new RadioRequest();

    {
        currentIndex.set(0);
    }
}
