package com.netease.music.ui.state;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.netease.lib_api.model.dj.DjPaygiftBean;
import com.netease.music.domain.request.RadioPayRequest;
import com.netease.music.ui.state.load.BaseLoadingViewModel;

import java.util.List;

public class RadioPayViewModel extends BaseLoadingViewModel {


    //当前请求的数据
    public ObservableInt limit = new ObservableInt();
    public ObservableInt offset = new ObservableInt();
    //电台数据
    public ObservableField<List<DjPaygiftBean.DataBean.ListBean>> radioList = new ObservableField<>();

    public final RadioPayRequest request = new RadioPayRequest();

    {
        limit.set(10);
        offset.set(0);
    }
}
