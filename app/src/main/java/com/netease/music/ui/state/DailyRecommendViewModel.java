package com.netease.music.ui.state;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableFloat;
import androidx.databinding.ObservableInt;

import com.netease.lib_api.model.song.DailyRecommendSongsBean;
import com.netease.lib_api.model.song.SongDetailBean;
import com.netease.lib_api.model.user.LoginBean;
import com.netease.lib_common_ui.utils.GsonUtil;
import com.netease.lib_common_ui.utils.SharePreferenceUtil;
import com.kunminx.architecture.utils.Utils;
import com.netease.music.domain.request.DailyRecommendRequest;
import com.netease.music.ui.state.load.BaseLoadingViewModel;
import com.netease.music.util.TimeUtil;

import java.util.List;

public class DailyRecommendViewModel extends BaseLoadingViewModel {

    //标题透明度
    public ObservableFloat leftTitleAlpha = new ObservableFloat();
    //日期和背景图片透明度
    public ObservableFloat textAlpha = new ObservableFloat();
    //背景透明度
    public ObservableInt coverImgAlpha = new ObservableInt();

    public ObservableBoolean leftTitleVisiable = new ObservableBoolean();

    public ObservableField<String> day = new ObservableField<>();
    public ObservableField<String> month = new ObservableField<>();

    //背景图片
    public final ObservableField<String> backgroundImgUrl = new ObservableField<>();
    //音乐数据
    public final ObservableField<List<DailyRecommendSongsBean>> recommendMusic = new ObservableField<>();

    public final ObservableInt backgroundImgRadis = new ObservableInt();

    public final DailyRecommendRequest recommendRequest = new DailyRecommendRequest();

    {
        day.set(TimeUtil.getDay(System.currentTimeMillis()));
        month.set(TimeUtil.getMonth(System.currentTimeMillis()));
        leftTitleAlpha.set(0);
        leftTitleVisiable.set(false);
        backgroundImgUrl.set(GsonUtil.fromJSON(SharePreferenceUtil.getInstance(Utils.getApp()).getUserInfo(""), LoginBean.class).getProfile().getBackgroundUrl());
        backgroundImgRadis.set(125);

    }

}
