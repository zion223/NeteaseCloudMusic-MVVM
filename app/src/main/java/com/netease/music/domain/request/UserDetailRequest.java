package com.netease.music.domain.request;

import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.lib_api.model.user.FollowBean;
import com.netease.lib_api.model.user.UserDetailBean;
import com.netease.lib_api.model.user.UserEventBean;
import com.netease.lib_api.model.user.UserFollowedBean;
import com.netease.lib_api.model.user.UserFollowerBean;
import com.netease.lib_api.model.user.UserPlaylistBean;
import com.netease.lib_api.model.user.UserPlaylistEntity;
import com.netease.lib_network.ApiEngine;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class UserDetailRequest extends BaseRequest {

    //用户详情
    private MutableLiveData<UserDetailBean> userDeatailLiveData;
    //关注或取消关注用户
    private MutableLiveData<FollowBean> userFollowLiveData;
    //用户的关注列表
    private MutableLiveData<UserFollowerBean> userFollowerLiveData;
    //用户的粉丝列表
    private MutableLiveData<UserFollowedBean> userFollowedLiveData;
    //用户动态
    private MutableLiveData<UserEventBean> userEventLiveData;
    //用户歌单  创建的歌单和收藏的歌单
    private MutableLiveData<List<UserPlaylistEntity>> userPlayListLiveData;
    //用户喜欢的歌单数据 单独
    private MutableLiveData<UserPlaylistBean.PlaylistBean> userLikePlayListLiveData;

    public MutableLiveData<UserDetailBean> getUserDeatailLiveData() {
        if (userDeatailLiveData == null) {
            userDeatailLiveData = new MutableLiveData<>();
        }
        return userDeatailLiveData;
    }

    public MutableLiveData<UserEventBean> getUserEventLiveData() {
        if (userEventLiveData == null) {
            userEventLiveData = new MutableLiveData<>();
        }
        return userEventLiveData;
    }

    public MutableLiveData<FollowBean> getUserFollowLiveData() {
        if (userFollowLiveData == null) {
            userFollowLiveData = new MutableLiveData<>();
        }
        return userFollowLiveData;
    }

    public MutableLiveData<UserFollowerBean> getUserFollowerLiveData() {
        if (userFollowerLiveData == null) {
            userFollowerLiveData = new MutableLiveData<>();
        }
        return userFollowerLiveData;
    }

    public MutableLiveData<UserFollowedBean> getUserFollowedLiveData() {
        if (userFollowedLiveData == null) {
            userFollowedLiveData = new MutableLiveData<>();
        }
        return userFollowedLiveData;
    }

    public MutableLiveData<List<UserPlaylistEntity>> getUserPlayListLiveData() {
        if (userPlayListLiveData == null) {
            userPlayListLiveData = new MutableLiveData<>();
        }
        return userPlayListLiveData;
    }

    public MutableLiveData<UserPlaylistBean.PlaylistBean> getUserLikePlayListLiveData() {
        if (userLikePlayListLiveData == null) {
            userLikePlayListLiveData = new MutableLiveData<>();
        }
        return userLikePlayListLiveData;
    }

    public void requestUserDetail(long uid) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getUserDetail(uid)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(bean -> userDeatailLiveData.postValue(bean));
    }

    public void requestUserEvent(long uid) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getUserEvent(uid, 30, -1)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(bean -> userEventLiveData.postValue(bean));
    }

    public void requestUserFollow(long uid, boolean follow) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getUserFollow(uid, follow ? 1 : 0)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(bean -> {
                    if (bean.getCode() == 200) {
                        userFollowLiveData.postValue(bean);
                    }
                });
    }

    public void requestUserFollower(long uid) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getUserFollower(uid)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(bean -> userFollowerLiveData.postValue(bean));
    }

    public void requestUserFollowed(long uid) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getUserFollowed(uid)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(bean -> userFollowedLiveData.postValue(bean));
    }

    //查询用户歌单  解析数据
    public void requestUserPlaylist(long uid) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getUserPlaylist(uid)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(playList -> {

                    userLikePlayListLiveData.postValue(playList.getPlaylist().get(0));

                    int size = playList.getPlaylist().size();
                    //创建和收藏歌单的分界
                    int subIndex = 0;
                    for (int i = 0; i < size; i++) {
                        if (playList.getPlaylist().get(i).getCreator().getUserId() != uid) {
                            subIndex = i;
                            break;
                        }
                    }
                    //创建的歌单数量
                    int createPlaylistSize = subIndex - 1;
                    final List<UserPlaylistEntity> playListData = new ArrayList<>();
                    playListData.add(new UserPlaylistEntity("创建的歌单", createPlaylistSize));
                    //创建的歌单数量小于等于5个则直接全部显示出来
                    for (int i = 1; i < (createPlaylistSize <= 5 ? createPlaylistSize : 4); i++) {
                        playListData.add(new UserPlaylistEntity(UserPlaylistEntity.TYPE.TYPE_CREATE, playList.getPlaylist().get(i)));
                    }
                    //多余5个数量的歌单时显示 更多歌单
                    if (createPlaylistSize > 5) {
                        playListData.add(new UserPlaylistEntity("更多歌单"));
                    }

                    int collectPlayListSize = size - subIndex;
                    playListData.add(new UserPlaylistEntity("收藏的歌单", collectPlayListSize));
                    //收藏的歌单数量小于等于5个则直接全部显示出来
                    for (int i = subIndex; i < (collectPlayListSize <= 5 ? subIndex + collectPlayListSize : subIndex + 3); i++) {
                        playListData.add(new UserPlaylistEntity(UserPlaylistEntity.TYPE.TYPE_SUBSCRIBE, playList.getPlaylist().get(i)));
                    }
                    if (collectPlayListSize > 5) {
                        playListData.add(new UserPlaylistEntity("更多歌单"));
                    }
                    userPlayListLiveData.postValue(playListData);
                });
    }

}
