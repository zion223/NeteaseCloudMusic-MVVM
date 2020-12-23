package com.netease.music.ui.state;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.netease.lib_api.model.user.LoginBean;
import com.netease.lib_common_ui.utils.GsonUtil;
import com.netease.lib_common_ui.utils.SharePreferenceUtil;
import com.kunminx.architecture.utils.Utils;
import com.netease.music.domain.request.DrawerRequest;


public class DrawerViewModel extends ViewModel {

    public final MutableLiveData<LoginBean> user = new MutableLiveData<>();

    // 定时停止播放剩余的时间
    public final MutableLiveData<String> leftPauseAudioTime = new MutableLiveData<>();
    public final DrawerRequest request = new DrawerRequest();
    {
        user.setValue(GsonUtil.fromJSON(SharePreferenceUtil.getInstance(Utils.getApp()).getUserInfo(""), LoginBean.class));
    }
}
