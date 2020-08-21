package com.netease.music.ui.state;

import android.graphics.drawable.Drawable;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableLong;
import androidx.lifecycle.ViewModel;

import com.imooc.lib_api.model.user.UserDetailBean;
import com.kunminx.architecture.utils.Utils;
import com.netease.music.R;
import com.netease.music.domain.request.UserDetailRequest;

public class UserDetailViewModel extends ViewModel {


    //当前用户Id
    public ObservableLong userId = new ObservableLong();

    //当前用户
    public final ObservableField<UserDetailBean> user = new ObservableField<>();

    //是否关注用户 抽取出来 方便状态变化
    public final ObservableBoolean followed = new ObservableBoolean();

    //发送私信左边的图片
    public final ObservableField<Drawable> sendMsgDrawable = new ObservableField<>();

    public final UserDetailRequest request = new UserDetailRequest();

    {
        Drawable drawable = Utils.getApp().getResources().getDrawable(R.drawable.ic_notification_msg_white);
        drawable.setBounds(5, 0, 50, 50);
        sendMsgDrawable.set(drawable);
    }
}
