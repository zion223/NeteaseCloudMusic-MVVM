package com.netease.music.ui.state;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.ui.adapter.SimpleDataBindingAdapter;
import com.netease.lib_api.model.album.AlbumOrSongBean;
import com.netease.lib_api.model.banner.BannerBean;
import com.netease.lib_api.model.playlist.MainRecommendPlayListBean;
import com.netease.music.data.config.TypeEnum;
import com.netease.music.databinding.ItemDiscoverGedanBinding;
import com.netease.music.domain.request.DiscoverRequest;

import java.util.List;

public class DiscoverViewModel extends ViewModel {


    //banner数据
    public final ObservableField<List<BannerBean.BannersBean>> banners = new ObservableField<>();
    //banner中的Pic
    public final ObservableField<List<String>> bannersPic = new ObservableField<>();

    //推荐歌单的Adapter
    public final ObservableField<SimpleDataBindingAdapter<MainRecommendPlayListBean.RecommendBean, ItemDiscoverGedanBinding>> playListAdapter = new ObservableField<>();

    //所有的新歌和新碟数据
    public final ObservableField<List<AlbumOrSongBean>> albumOrSongLiveData = new ObservableField<>();
    //当前显示的数据
    public final ObservableField<List<AlbumOrSongBean>> currentAlbumOrSongLiveData = new ObservableField<>();

    //当前选中的是新歌还是新碟
    public final ObservableField<TypeEnum> type = new ObservableField<>();

    //数据访问
    public DiscoverRequest discoverRequest = new DiscoverRequest();

    {
        //默认初始选中的是专辑
        type.set(TypeEnum.ALBUM);
    }
}
