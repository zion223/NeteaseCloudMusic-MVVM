package com.netease.music.ui.state;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.netease.lib_api.model.playlist.TopListDetailBean;
import com.netease.music.domain.request.ArtistRequest;

import java.util.List;

public class ArtistSortViewModel extends ViewModel {

    public final ObservableField<List<TopListDetailBean.Artist>> artistData = new ObservableField<>();

    public final ArtistRequest request = new ArtistRequest();

}
