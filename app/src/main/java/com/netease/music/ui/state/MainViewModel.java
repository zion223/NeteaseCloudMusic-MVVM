package com.netease.music.ui.state;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.netease.music.data.bean.TestAlbum;
import com.netease.music.data.config.CHANNEL;
import com.netease.music.domain.request.MusicRequest;

import java.util.List;

public class MainViewModel extends ViewModel {

    public final ObservableBoolean initTabAndPage = new ObservableBoolean();

    public final ObservableField<String> pageAssetPath = new ObservableField<>();

    //头部数据
    public final ObservableField<CHANNEL[]> channelArray = new ObservableField<>();
    //offsetLimt
    public final ObservableInt limit = new ObservableInt();


    public final MutableLiveData<List<TestAlbum.TestMusic>> list = new MutableLiveData<>();

    public final MutableLiveData<Boolean> notifyCurrentListChanged = new MutableLiveData<>();

    //TODO 将 request 作为 ViewModel 的成员暴露给 Activity/Fragment，
    // 如此便于语义的明确，以及实现多个 request 在 ViewModel 中的组合和复用。

    public final MusicRequest musicRequest = new MusicRequest();

    {
        limit.set(4);
        initTabAndPage.set(true);
        pageAssetPath.set("summary.html");
        channelArray.set(new CHANNEL[]{CHANNEL.MY, CHANNEL.DISCORY, CHANNEL.YUNCUN, CHANNEL.VIDEO});
    }

}
