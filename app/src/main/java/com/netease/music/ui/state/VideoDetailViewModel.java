package com.netease.music.ui.state;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.netease.lib_api.model.mv.VideoBean;
import com.netease.music.domain.request.VideoRequest;
import com.netease.music.ui.state.load.BaseLoadingViewModel;

public class VideoDetailViewModel extends BaseLoadingViewModel {

    //视频详情
    public final ObservableField<VideoBean.Data> video = new ObservableField<>();
    //视频点赞数据 和收藏数据单独抽取出来方便做状态变更
    public final ObservableBoolean isParised = new ObservableBoolean();
    //是否收藏
    public final ObservableBoolean isSubscribed = new ObservableBoolean();
    //是否关注用户
    public final ObservableBoolean isFollowed = new ObservableBoolean();

    public final ObservableField<BaseSectionQuickAdapter> commentAdapter = new ObservableField<>();

    public final VideoRequest request = new VideoRequest();
}

