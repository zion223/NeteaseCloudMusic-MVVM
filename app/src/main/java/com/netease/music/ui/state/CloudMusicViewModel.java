package com.netease.music.ui.state;

import androidx.databinding.ObservableField;

import com.netease.lib_api.model.notification.UserCloudBean;
import com.netease.music.domain.request.CloudMusicRequest;
import com.netease.music.ui.state.load.BaseLoadingViewModel;

public class CloudMusicViewModel extends BaseLoadingViewModel {

    //云盘数据
    public final ObservableField<UserCloudBean> userCloud = new ObservableField<>();
    //云盘当前容量和剩余容量
    public final ObservableField<String> cloudSize = new ObservableField<>();

    public final CloudMusicRequest musicRequest = new CloudMusicRequest();

}
