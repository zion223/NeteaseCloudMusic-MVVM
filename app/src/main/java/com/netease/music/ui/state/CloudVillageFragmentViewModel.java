package com.netease.music.ui.state;

import androidx.databinding.ObservableBoolean;

import com.netease.music.domain.request.CloudVillageRequest;
import com.netease.music.ui.state.load.BaseLoadingViewModel;

public class CloudVillageFragmentViewModel extends BaseLoadingViewModel {

    //上拉刷新 下拉加载更多 状态
    public ObservableBoolean reloadState = new ObservableBoolean();

    public CloudVillageRequest eventRequest = new CloudVillageRequest();


    {
        reloadState.set(false);
    }


}
