package com.netease.music.ui.state;

import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModel;

import com.netease.music.domain.request.VideoRequest;

public class VideoTabViewModel extends ViewModel {


    public ObservableField<String[]> indicatorTitle = new ObservableField<>();
    public ObservableField<FragmentPagerAdapter> adapter = new ObservableField<>();


    public VideoRequest request = new VideoRequest();
}
