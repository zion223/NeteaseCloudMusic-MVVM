package com.netease.music.ui.state;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.netease.music.domain.request.CloudVillageRequest;

public class CloudVillageFragmentViewModel extends ViewModel {

    //上拉刷新 下拉加载更多 状态
    public ObservableBoolean reloadState = new ObservableBoolean();

    public CloudVillageRequest eventRequest = new CloudVillageRequest();

    //加载动画
    public final ObservableBoolean loadingVisible = new ObservableBoolean();

    public final ObservableField<BaseQuickAdapter> adapter = new ObservableField<>();

    {
        reloadState.set(false);
        loadingVisible.set(true);
    }


}
