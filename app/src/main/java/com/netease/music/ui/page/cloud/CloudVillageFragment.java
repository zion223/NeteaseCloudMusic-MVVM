package com.netease.music.ui.page.cloud;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
        mViewModel = getFragmentScopeViewModel(CloudVillageFragmentViewModel.class);
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

            mViewModel.adapter.set(new EventAdapter(getContext(), mainEventBean.getEvent()));
            //停止显示加载动画
            mViewModel.loadingVisible.set(false);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
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
