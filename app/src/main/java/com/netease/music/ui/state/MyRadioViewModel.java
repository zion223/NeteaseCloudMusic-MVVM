package com.netease.music.ui.state;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;

import com.netease.lib_api.model.dj.DjSubListBean;
import com.netease.music.domain.request.MineRequest;

import java.util.List;

public class MyRadioViewModel extends ViewModel {


    //订阅的电台的数据
    public final ObservableField<List<DjSubListBean.DjRadios>> subRadioList = new ObservableField<>();
    //订阅的电台数量
    public final ObservableInt subRadioSize = new ObservableInt();
    //创建的电台数据  TODO
    public final ObservableField<List<DjSubListBean.DjRadios>> createRadioList = new ObservableField<>();
    public final ObservableInt createRadioSize = new ObservableInt();


    public final MineRequest request = new MineRequest();

}
