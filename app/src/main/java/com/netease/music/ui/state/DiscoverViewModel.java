package com.netease.music.ui.state;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.imooc.lib_api.model.banner.BannerBean;
import com.kunminx.binding_recyclerview.adapter.SimpleDataBindingAdapter;
import com.netease.music.domain.request.DiscoverRequest;

import java.util.List;

public class DiscoverViewModel extends ViewModel {


    //banner数据
    public final ObservableField<List<BannerBean.BannersBean>> banners = new ObservableField<>();
    //banner中的Pic
    public final ObservableField<List<String>> bannersPic = new ObservableField<>();

    //推荐歌单的Adapter
    public final ObservableField<SimpleDataBindingAdapter> playListAdapter = new ObservableField<>();
    //推荐歌单的spanCount 默认是3
    public final ObservableInt playListSpanCount = new ObservableInt();


    //数据访问
    public DiscoverRequest discoverRequest = new DiscoverRequest();

    {
        playListSpanCount.set(3);
    }
}
