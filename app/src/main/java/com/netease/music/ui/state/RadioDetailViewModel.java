package com.netease.music.ui.state;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.netease.lib_api.model.dj.DjDetailBean;
import com.netease.lib_api.model.dj.DjProgramBean;
import com.netease.music.domain.request.RadioRequest;

import java.util.List;

public class RadioDetailViewModel extends ViewModel {

    //是否订阅该电台
    public ObservableBoolean isSub = new ObservableBoolean();
    //当前节目排序方式
    public ObservableBoolean asc = new ObservableBoolean();
    //电台Id
    public ObservableField<String> radioId = new ObservableField<>();
    public ObservableInt currentItem = new ObservableInt();
    //电台的节目
    public ObservableField<List<DjProgramBean.ProgramsBean>> programList = new ObservableField<>();
    //电台节目Adapter
    public ObservableField<BaseQuickAdapter> programAdapter = new ObservableField<>();
    //热门评论Adapter
    public ObservableField<BaseQuickAdapter> hotCommentAdapter = new ObservableField<>();

    //电台信息
    public ObservableField<DjDetailBean.DjRadioBean> radio = new ObservableField<>();

    public ObservableField<CharSequence[]> indicatorTitle = new ObservableField<>();

    public final RadioRequest request = new RadioRequest();

    {
        currentItem.set(1);
        //默认是新到老
        asc.set(false);
    }
}
