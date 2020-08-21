package com.netease.music.domain.request;

import androidx.lifecycle.MutableLiveData;

import com.imooc.lib_api.model.user.FollowBean;
import com.imooc.lib_api.model.user.UserDetailBean;
import com.imooc.lib_api.model.user.UserEventBean;
import com.imooc.lib_api.model.user.UserFollowedBean;
import com.imooc.lib_api.model.user.UserFollowerBean;
import com.imooc.lib_network.ApiEngine;
import com.kunminx.architecture.domain.request.BaseRequest;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

    public void requestUserDetail(long uid) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getUserDetail(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> userDeatailLiveData.postValue(bean));
    }

    public void requestUserEvent(long uid) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getUserEvent(uid, 30, -1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> userEventLiveData.postValue(bean));
    }

    public void requestUserFollow(long uid, boolean follow) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getUserFollow(uid, follow ? 1 : 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean.getCode() == 200) {
                        userFollowLiveData.postValue(bean);
                    }
                });
    }

    public void requestUserFollower(long uid) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getUserFollower(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> userFollowerLiveData.postValue(bean));
    }

    public void requestUserFollowed(long uid) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getUserFollowed(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> userFollowedLiveData.postValue(bean));
    }

}
