package com.netease.music.ui.state;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.lib_api.model.mv.MvBean;
import com.netease.lib_api.model.playlist.PlayListCommentEntity;
import com.netease.music.domain.request.MvRequest;
import com.netease.music.ui.state.load.BaseLoadingViewModel;

public class MvDetailViewModel extends BaseLoadingViewModel {


    //MV详情
    public final ObservableField<MvBean.MvDetailBean> mvData = new ObservableField<>();

    public final ObservableField<String> artistName = new ObservableField<>();
    public final ObservableField<String> artistPic = new ObservableField<>();

    //视频点赞数据 和收藏数据单独抽取出来方便做状态变更
    public final ObservableBoolean isParised = new ObservableBoolean();
    //是否收藏
    public final ObservableBoolean isSubscribed = new ObservableBoolean();
    //是否关注歌手
    public final ObservableBoolean isFollowed = new ObservableBoolean();

    public final ObservableField<BaseSectionQuickAdapter<PlayListCommentEntity, BaseViewHolder>> commentAdapter = new ObservableField<>();

    public final MvRequest request = new MvRequest();
}
