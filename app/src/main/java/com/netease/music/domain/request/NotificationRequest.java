package com.netease.music.domain.request;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.lib_api.model.notification.ForwardsMeBean;
import com.netease.lib_api.model.notification.PrivateCommentBean;
import com.netease.lib_api.model.notification.PrivateMsgBean;
import com.netease.lib_api.model.notification.PrivateNoticeBean;
import com.netease.lib_network.ApiEngine;

import java.util.List;

@SuppressLint("CheckResult")
public class NotificationRequest extends BaseRequest {

    // 评论
    private MutableLiveData<List<PrivateCommentBean.Comment>> mCommentLiveData;
    private MutableLiveData<List<PrivateMsgBean.Msg>> mPrivateLetterLiveData;
    private MutableLiveData<List<ForwardsMeBean.ForwardsMeData>> mForwardsMeLiveData;
    private MutableLiveData<List<PrivateNoticeBean.Notice>> mNoticeLiveData;


    public MutableLiveData<List<PrivateCommentBean.Comment>> getPrivateCommentLiveData() {
        if (mCommentLiveData == null) {
            mCommentLiveData = new MutableLiveData<>();
        }
        return mCommentLiveData;
    }

    public MutableLiveData<List<PrivateMsgBean.Msg>> getPrivateLetterLiveData() {
        if (mPrivateLetterLiveData == null) {
            mPrivateLetterLiveData = new MutableLiveData<>();
        }
        return mPrivateLetterLiveData;
    }

    public MutableLiveData<List<ForwardsMeBean.ForwardsMeData>> getForwardsMeLiveData() {
        if (mForwardsMeLiveData == null) {
            mForwardsMeLiveData = new MutableLiveData<>();
        }
        return mForwardsMeLiveData;
    }

    public MutableLiveData<List<PrivateNoticeBean.Notice>> getNoticeLiveData() {
        if (mNoticeLiveData == null) {
            mNoticeLiveData = new MutableLiveData<>();
        }
        return mNoticeLiveData;
    }

    // 请求评论
    public void requestPirvateCommnet(int uid) {
        ApiEngine.getInstance().getApiService().getPrivateComment(uid)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(privateCommentBean -> {
                    if (privateCommentBean.getCode() == 200) {
                        mCommentLiveData.postValue(privateCommentBean.getComments());
                    }
                });
    }

    // 请求私信
    public void requestPirvateLetter() {
        ApiEngine.getInstance().getApiService().getPrivateLetter(30)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(notificationBean -> {
                    if (notificationBean.getCode() == 200) {
                        mPrivateLetterLiveData.postValue(notificationBean.getMsgs());
                    }
                });
    }

    // 请求转发的数据
    public void requestForwardsMe() {
        ApiEngine.getInstance().getApiService().getPrivateFowards()
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(notificationBean -> {
                    if (notificationBean.getCode() == 200) {
                        mForwardsMeLiveData.postValue(notificationBean.getForwards());
                    }
                });
    }

    // 请求通知
    public void requestNotice() {
        ApiEngine.getInstance().getApiService().getPrivateNotice()
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(notificationBean -> {
                    if (notificationBean.getCode() == 200) {
                        mNoticeLiveData.postValue(notificationBean.getNotices());
                    }
                });
    }

}
