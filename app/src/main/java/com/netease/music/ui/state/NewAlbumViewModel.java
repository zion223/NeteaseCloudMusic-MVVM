package com.netease.music.ui.state;

import androidx.databinding.ObservableInt;

import com.netease.music.domain.request.NewAlbumRequest;
import com.netease.music.ui.state.load.BaseLoadingViewModel;

public class NewAlbumViewModel extends BaseLoadingViewModel {


    public final ObservableInt spanCount = new ObservableInt();

    public final NewAlbumRequest request = new NewAlbumRequest();

    {
        spanCount.set(2);
    }
}
