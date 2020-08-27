package com.netease.music.ui.state;

import androidx.databinding.ObservableField;

import com.netease.lib_api.model.mv.MvBean;
import com.netease.music.domain.request.MvRequest;
import com.netease.music.ui.state.load.BaseLoadingViewModel;

import java.util.List;

public class MvSortViewModel extends BaseLoadingViewModel {

    public ObservableField<String> area = new ObservableField<>();
    public ObservableField<String> type = new ObservableField<>();
    public ObservableField<String> order = new ObservableField<>();
    //mv数据
    public ObservableField<List<MvBean.MvDetailBean>> data = new ObservableField<>();


    public final MvRequest request = new MvRequest();

    {
        area.set("全部");
        type.set("全部");
        order.set("上升最快");
    }
}
