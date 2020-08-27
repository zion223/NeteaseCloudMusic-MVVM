package com.netease.music.ui.state;

import androidx.databinding.ObservableField;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.netease.lib_api.model.mv.VideoBean;
import com.netease.music.domain.request.VideoRequest;
import com.netease.music.ui.state.load.BaseLoadingViewModel;

public class VideoDetailViewModel extends BaseLoadingViewModel {

    public final ObservableField<VideoBean.Data> video = new ObservableField<>();


    public final ObservableField<BaseSectionQuickAdapter> commentAdapter = new ObservableField<>();

    public final VideoRequest request = new VideoRequest();
}

