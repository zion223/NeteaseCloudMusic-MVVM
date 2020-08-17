package com.netease.music.ui.state;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.imooc.lib_api.model.notification.UserCloudBean;
import com.netease.music.domain.request.CloudMusicRequest;

public class CloudMusicViewModel extends ViewModel {

    //是否显示加载动画
    public final ObservableBoolean loadingVisible = new ObservableBoolean();
    //云盘数据
    public final ObservableField<UserCloudBean> userCloud = new ObservableField<>();
    //云盘当前容量和剩余容量
    public final ObservableField<String> cloudSize = new ObservableField<>();
    //音乐列表Adapter
    public final ObservableField<BaseQuickAdapter> adapter = new ObservableField<>();

    public final CloudMusicRequest musicRequest = new CloudMusicRequest();


    {
        loadingVisible.set(true);
    }
}
