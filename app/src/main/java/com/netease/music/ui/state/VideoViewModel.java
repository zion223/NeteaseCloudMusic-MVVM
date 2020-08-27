package com.netease.music.ui.state;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.netease.lib_api.model.mv.VideoBean;
import com.netease.music.domain.request.VideoRequest;

import java.util.List;

public class VideoViewModel extends ViewModel {


    public ObservableField<BaseQuickAdapter> adpter = new ObservableField<>();

    public ObservableField<List<VideoBean.VideoEntity>> list = new ObservableField<>();

    public VideoRequest request = new VideoRequest();
}
