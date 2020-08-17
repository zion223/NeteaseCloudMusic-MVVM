package com.netease.music.ui.base.binding_adapter;

import androidx.databinding.BindingAdapter;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;

public class RefreshBindingAdapter {


    //TODO 参数不变时 第二次不会触发
    @BindingAdapter(value = {"reloadRefreshState"})
    public static void reloadRefreshLayoutState(SmartRefreshLayout refreshLayout, Boolean success) {
        RefreshState state = refreshLayout.getState();
        if (state.isFooter && state.isOpening) {
            refreshLayout.finishLoadMore(success);
        } else if (state.isHeader && state.isOpening) {
            refreshLayout.finishRefresh(success);
        }
    }

    @BindingAdapter(value = {"enableRefresh"})
    public static void setEnableRefresh(SmartRefreshLayout refreshLayout, Boolean enable) {
        refreshLayout.setEnableRefresh(enable);
    }
}
