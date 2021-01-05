package com.netease.music.ui.state;


import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.netease.music.data.config.TypeEnum;
import com.netease.music.domain.request.SongListDetailRequest;
import com.netease.music.ui.state.load.BaseLoadingViewModel;

public class SonglistDeatilViewModel extends BaseLoadingViewModel {

    //当前类型 是 专辑 或者 歌单
    public final ObservableField<TypeEnum> type = new ObservableField<>();

    //推荐原因  可以为空
    public final ObservableField<String> reason = new ObservableField<>();
    //歌单 或者专辑ID
    public final ObservableField<Long> listId = new ObservableField<>();
    //当前歌手或用户的Id
    public final ObservableField<Long> creatorId = new ObservableField<>();

    //标题
    public final ObservableField<String> title = new ObservableField<>();
    //描述
    public final ObservableField<String> desc = new ObservableField<>();
    //歌单创建者 或者歌手名称
    public final ObservableField<String> creator = new ObservableField<>();
    //播放数量
    public final ObservableField<String> playCount = new ObservableField<>();
    //评论数量
    public final ObservableField<String> commentCount = new ObservableField<>();
    //收藏数量
    public final ObservableField<String> collectCount = new ObservableField<>();
    //分享数量
    public final ObservableField<String> shareCount = new ObservableField<>();
    //歌曲数量
    public final ObservableField<String> songCount = new ObservableField<>();

    //是否已经收藏
    public final ObservableBoolean isCollected = new ObservableBoolean();

    //背景图片
    public final ObservableField<String> backgroundImgUrl = new ObservableField<>();
    //
    public final ObservableInt backgroundCoverImgRadis = new ObservableInt();

    public final ObservableInt backgroundImgRadis = new ObservableInt();
    //专辑或歌单封面
    public final ObservableField<String> coverImgUrl = new ObservableField<>();
    //作者或歌手图片
    public final ObservableField<String> creatorImgUrl = new ObservableField<>();


    //歌曲列表
    public final ObservableField<BaseQuickAdapter> adapter = new ObservableField<>();

    public final SongListDetailRequest songListRequest = new SongListDetailRequest();

    {
        playCount.set("0");
        collectCount.set("0");
        backgroundCoverImgRadis.set(55);
        backgroundImgRadis.set(175);
    }
}
