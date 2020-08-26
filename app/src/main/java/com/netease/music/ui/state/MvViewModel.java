package com.netease.music.ui.state;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.netease.music.domain.request.MvRequest;

public class MvViewModel extends ViewModel {


    //更新时间
    public ObservableField<String> updateTime = new ObservableField<>();

    //mv封面
    public ObservableField<String> mvCoverImg = new ObservableField<>();


    public final MvRequest request = new MvRequest();
}

