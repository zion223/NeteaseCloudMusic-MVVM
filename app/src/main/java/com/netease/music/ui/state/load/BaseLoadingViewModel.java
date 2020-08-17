package com.netease.music.ui.state.load;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.chad.library.adapter.base.BaseQuickAdapter;

public class BaseLoadingViewModel extends ViewModel {

    //加载动画
    public final ObservableBoolean loadingVisible = new ObservableBoolean();

    public final ObservableField<BaseQuickAdapter> adapter = new ObservableField<>();

    {
        loadingVisible.set(true);
    }
}
