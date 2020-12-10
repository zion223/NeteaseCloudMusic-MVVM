package com.netease.music.ui.state;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;
import androidx.viewpager.widget.PagerAdapter;


public class MessageTabViewModel extends ViewModel {

    // tab 标题栏
    public final ObservableField<String[]> tabTitles = new ObservableField<>();
    public final ObservableField<PagerAdapter> adapter = new ObservableField<>();
    public final ObservableField<String> title = new ObservableField<>();

    {
        tabTitles.set(new String[]{"私信", "评论", "@我", "通知"});
        title.set("我的消息");
    }
}
