package com.netease.music.ui.state;

import android.graphics.drawable.Drawable;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableLong;
import androidx.lifecycle.ViewModel;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.netease.lib_api.model.user.UserDetailBean;
import com.netease.lib_api.model.user.UserPlaylistBean;
import com.kunminx.architecture.utils.Utils;
import com.netease.music.R;
import com.netease.music.domain.request.UserDetailRequest;

public class UserDetailViewModel extends ViewModel {


    //当前用户Id
    public ObservableLong userId = new ObservableLong();

    //当前用户
    public final ObservableField<UserDetailBean> user = new ObservableField<>();
    //喜欢的音乐 (单独抽取出来)
    public final ObservableField<UserPlaylistBean.PlaylistBean> likePlayList = new ObservableField<>();

    //是否关注用户 抽取出来 方便状态变化
    public final ObservableBoolean followed = new ObservableBoolean();

    //发送私信左边的图片
    public final ObservableField<Drawable> sendMsgDrawable = new ObservableField<>();

    public final UserDetailRequest request = new UserDetailRequest();
    // 主页  动态()
    public ObservableField<CharSequence[]> indicatorTitle = new ObservableField<>();

    //动态的Adapter
    public ObservableField<BaseQuickAdapter> eventAdapter = new ObservableField<>();
    //歌单的adapter
    public ObservableField<BaseQuickAdapter> playListAdapter = new ObservableField<>();

    {
        Drawable drawable = Utils.getApp().getResources().getDrawable(R.drawable.ic_notification_msg_white);
        drawable.setBounds(5, 0, 50, 50);
        sendMsgDrawable.set(drawable);
    }
}
