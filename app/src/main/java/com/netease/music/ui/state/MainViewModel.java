package com.netease.music.ui.state;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;

import com.netease.music.data.config.CHANNEL;

public class MainViewModel extends ViewModel {

    public final ObservableBoolean initTabAndPage = new ObservableBoolean();


    //头部数据
    public final ObservableField<CHANNEL[]> channelArray = new ObservableField<>();
    //offsetLimt
    public final ObservableInt limit = new ObservableInt();
    //当前页为发现页
    public final ObservableInt currentItem = new ObservableInt();

    {
        limit.set(1);
        currentItem.set(1);
        initTabAndPage.set(true);
        channelArray.set(new CHANNEL[]{CHANNEL.MY, CHANNEL.DISCORY, CHANNEL.YUNCUN, CHANNEL.VIDEO});
    }

}
