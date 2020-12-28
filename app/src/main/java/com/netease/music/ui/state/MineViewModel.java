package com.netease.music.ui.state;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.netease.lib_api.model.user.LoginBean;
import com.netease.lib_common_ui.utils.GsonUtil;
import com.netease.lib_common_ui.utils.SharePreferenceUtil;
import com.kunminx.architecture.utils.Utils;
import com.netease.music.domain.request.MineRequest;
import com.netease.music.ui.state.load.BaseLoadingViewModel;
import com.netease.music.util.MusicUtils;

public class MineViewModel extends BaseLoadingViewModel {

    //当前用户
    public final ObservableField<LoginBean> user = new ObservableField<>();
    //本地音乐数量
    public final ObservableInt localMusicSize = new ObservableInt();
    //最近播放音乐数量
    public final ObservableInt recentPlaySongSize = new ObservableInt();

    public final ObservableField<BaseQuickAdapter> playlistAdapter = new ObservableField<>();


    public final MineRequest mineRequest = new MineRequest();

    {
        user.set(GsonUtil.fromJSON(SharePreferenceUtil.getInstance(Utils.getApp()).getUserInfo(""), LoginBean.class));

        localMusicSize.set(SharePreferenceUtil.getInstance(Utils.getApp()).getLocalMusicCount(-1) == -1 ? MusicUtils.queryMusicSize(Utils.getApp(), MusicUtils.START_FROM_LOCAL) : SharePreferenceUtil.getInstance(Utils.getApp()).getLocalMusicCount(-1));
    }
}
