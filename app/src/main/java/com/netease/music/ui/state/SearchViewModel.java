package com.netease.music.ui.state;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.netease.lib_api.model.search.HotSearchDetailBean;
import com.netease.music.domain.request.SearchRequest;

import java.util.List;

public class SearchViewModel extends ViewModel {


    //搜索框中搜索内容  双向绑定
    public final ObservableField<String> searchKeyword = new ObservableField<>();

    public final ObservableField<List<HotSearchDetailBean.DataBean>> list = new ObservableField<>();
    //热搜列表适配器
    public final ObservableField<BaseQuickAdapter> adapter = new ObservableField<>();

    public final SearchRequest searchRequest = new SearchRequest();

}
