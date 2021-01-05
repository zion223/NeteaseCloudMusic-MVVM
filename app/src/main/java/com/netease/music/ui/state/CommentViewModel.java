package com.netease.music.ui.state;


import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.netease.music.data.config.TypeEnum;
import com.netease.music.domain.request.CommentRequest;
import com.netease.music.ui.state.load.BaseLoadingViewModel;

public class CommentViewModel extends BaseLoadingViewModel {

    //当前类型 是 专辑 或者 歌单  歌曲
    public final ObservableField<TypeEnum> type = new ObservableField<>();

    //歌单 或者专辑ID
    public final ObservableField<String> listId = new ObservableField<>();
    //图片
    public final ObservableField<String> imgUrl = new ObservableField<>();
    //标题
    public final ObservableField<String> titile = new ObservableField<>();
    //创作者  歌手或用户
    public final ObservableField<CharSequence> createor = new ObservableField<>();

    //评论数量
    public final ObservableInt commentSize = new ObservableInt();


    public final CommentRequest request = new CommentRequest();

}
