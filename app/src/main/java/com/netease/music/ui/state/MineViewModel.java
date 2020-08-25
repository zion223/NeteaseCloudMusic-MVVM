package com.netease.music.ui.state;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.imooc.lib_api.model.user.LoginBean;
import com.imooc.lib_common_ui.utils.GsonUtil;
import com.imooc.lib_common_ui.utils.SharePreferenceUtil;
import com.kunminx.architecture.utils.Utils;
import com.netease.music.domain.request.MineRequest;

public class MineViewModel extends ViewModel {

    public final ObservableField<LoginBean> user = new ObservableField<>();

    public final ObservableField<BaseQuickAdapter> playlistAdapter = new ObservableField<>();


    public final MineRequest mineRequest = new MineRequest();

    {
        user.set(GsonUtil.fromJSON(SharePreferenceUtil.getInstance(Utils.getApp()).getUserInfo(""), LoginBean.class));

    }
}
