package com.netease.music.domain.request;

import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.lib_api.model.mv.VideoBean;
import com.netease.lib_api.model.mv.VideoDetailBean;
import com.netease.lib_api.model.mv.VideoGroupBean;
import com.netease.lib_api.model.mv.VideoRelatedBean;
import com.netease.lib_api.model.mv.VideoUrlBean;
import com.netease.lib_api.model.playlist.PlayListCommentEntity;
import com.netease.lib_network.ApiEngine;
import com.netease.lib_network.ExceptionHandle;
import com.netease.lib_network.SimpleObserver;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class VideoRequest extends BaseRequest {


    private MutableLiveData<String[]> mTitleLiveData = new MutableLiveData<>();
    private MutableLiveData<long[]> mVideoTabIdLiveData = new MutableLiveData<>();
    private MutableLiveData<VideoBean> mVideoLiveData = new MutableLiveData<>();
    private MutableLiveData<VideoDetailBean> mVideoDetailLiveData = new MutableLiveData<>();
    private MutableLiveData<VideoRelatedBean> mVideoRelatedLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<PlayListCommentEntity>> mVideoCommentLiveData = new MutableLiveData<>();
    private MutableLiveData<VideoUrlBean> mVideoUrlLiveData = new MutableLiveData<>();

    //给视频点赞  收藏视频 状态   关注或取消关注用户
    private MutableLiveData<Boolean> mChangeLikeStatusLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mChangeSubscribeStatusLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mChangeFollowStatusLiveData = new MutableLiveData<>();

    public MutableLiveData<long[]> getVideoTabIdLiveData() {
        if (mVideoTabIdLiveData == null) {
            mVideoTabIdLiveData = new MutableLiveData<>();
        }
        return mVideoTabIdLiveData;
    }

    public MutableLiveData<String[]> getTitleLiveData() {
        if (mTitleLiveData == null) {
            mTitleLiveData = new MutableLiveData<>();
        }
        return mTitleLiveData;
    }


    public MutableLiveData<VideoBean> getVideoLiveData() {
        if (mVideoLiveData == null) {
            mVideoLiveData = new MutableLiveData<>();
        }
        return mVideoLiveData;
    }

    public MutableLiveData<VideoDetailBean> getVideoDetailLiveData() {
        if (mVideoDetailLiveData == null) {
            mVideoDetailLiveData = new MutableLiveData<>();
        }
        return mVideoDetailLiveData;
    }

    public MutableLiveData<VideoRelatedBean> getVideoRelatedLiveData() {
        if (mVideoRelatedLiveData == null) {
            mVideoRelatedLiveData = new MutableLiveData<>();
        }
        return mVideoRelatedLiveData;
    }

    public MutableLiveData<ArrayList<PlayListCommentEntity>> getVideoCommentLiveData() {
        if (mVideoCommentLiveData == null) {
            mVideoCommentLiveData = new MutableLiveData<>();
        }
        return mVideoCommentLiveData;
    }

    public MutableLiveData<VideoUrlBean> getVideoUrlLiveData() {
        if (mVideoUrlLiveData == null) {
            mVideoUrlLiveData = new MutableLiveData<>();
        }
        return mVideoUrlLiveData;
    }

    public MutableLiveData<Boolean> getSubscribeStatusLiveData() {
        if (mChangeSubscribeStatusLiveData != null) {
            mChangeSubscribeStatusLiveData = new MutableLiveData<>();
        }
        return mChangeSubscribeStatusLiveData;
    }

    public MutableLiveData<Boolean> getLikeStatusLiveData() {
        if (mChangeLikeStatusLiveData != null) {
            mChangeLikeStatusLiveData = new MutableLiveData<>();
        }
        return mChangeLikeStatusLiveData;
    }

    public MutableLiveData<Boolean> getFollowStatusLiveData() {
        if (mChangeFollowStatusLiveData == null) {
            mChangeFollowStatusLiveData = new MutableLiveData<>();
        }
        return mChangeFollowStatusLiveData;
    }

    public void requestVideoGroup() {
        ApiEngine.getInstance().getApiService().getVideoGroup()
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(new SimpleObserver<VideoGroupBean>() {

                    @Override
                    protected void onFailed(ExceptionHandle.ResponseThrowable errorMsg) {

                    }

                    @Override
                    public void onSuccess(@NonNull @NotNull VideoGroupBean videoGroupBean) {
                        final List<VideoGroupBean.Data> data = videoGroupBean.getData();

                        // 根据ID查询视频 name为标题 tab数量最多为20个
                        int tabSize = data.size();
                        if (tabSize >= 20) {
                            tabSize = 20;
                        }
                        String[] mTitleData = new String[tabSize];
                        long[] mVideoTabId = new long[tabSize];
                        mVideoTabId[0] = 9999;
                        mTitleData[0] = "推荐";
                        for (int i = 1; i < tabSize; i++) {
                            VideoGroupBean.Data tab = data.get(i);
                            String name = tab.getName();
                            mTitleData[i] = name;
                            mVideoTabId[i] = tab.getId();
                        }
                        mTitleLiveData.postValue(mTitleData);
                        mVideoTabIdLiveData.postValue(mVideoTabId);
                    }


                });
    }

    //请求标签页下面的视频数据
    public void requestVideoTab(long videoId) {
        Single<VideoBean> videoObservable = null;
        if ("9999".equals(String.valueOf(videoId))) {
            //推荐
            videoObservable = ApiEngine.getInstance().getApiService().getVideoRecommend();
        } else {
            //普通数据
            videoObservable = ApiEngine.getInstance().getApiService().getVideoTab(videoId);
        }
        videoObservable.compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(new SimpleObserver<VideoBean>() {
                    @Override
                    public void onSuccess(@NonNull @NotNull VideoBean videoBean) {
                        mVideoLiveData.postValue(videoBean);
                    }

                    @Override
                    protected void onFailed(ExceptionHandle.ResponseThrowable errorMsg) {

                    }
                });
    }

    //请求视频详情
    public void requestVideoDeatail(String videoId) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getVideoDetail(videoId)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(videoDetailBean -> mVideoDetailLiveData.postValue(videoDetailBean));
    }

    //请求相关视频
    public void requestRelatedVideo(String videoId) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getVideoRelated(videoId)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(videoDetailBean -> mVideoRelatedLiveData.postValue(videoDetailBean));
    }

    //请求视频评论
    public void requestVideoComment(String videoId) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getVideoComment(videoId)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(commentBean -> {
                    final ArrayList<PlayListCommentEntity> entities = new ArrayList<>();
                    if (commentBean.getHotComments().size() > 0) {
                        entities.add(new PlayListCommentEntity("精彩评论"));
                        for (int i = 0; i < commentBean.getHotComments().size(); i++) {
                            entities.add(new PlayListCommentEntity(commentBean.getHotComments().get(i)));
                        }
                    }
                    entities.add(new PlayListCommentEntity("最新评论", String.valueOf(commentBean.getComments().size())));
                    for (int j = 0; j < commentBean.getComments().size(); j++) {
                        entities.add(new PlayListCommentEntity(commentBean.getComments().get(j)));
                    }
                    mVideoCommentLiveData.postValue(entities);
                });
    }

    //请求视频播放地址
    public void requestVideoUrl(String videoId) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getVideoUrl(videoId)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(videoUrlBean -> {
                    mVideoUrlLiveData.postValue(videoUrlBean);
                });
    }

    //给视频点赞
    public void requestLikeVideo(String videoId, boolean like) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().likeResource(videoId, like ? 1 : 0, 5)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(commentLikeBean -> {
                    mChangeLikeStatusLiveData.setValue(commentLikeBean.getCode() == 200);
                });
    }

    //收藏或取消收藏
    public void requestSubscribeVideo(String videoId, boolean subscribe) {
        Disposable dis = ApiEngine.getInstance().getApiService().subscribeVideo(videoId, subscribe ? 1 : 0)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(commentLikeBean -> {
                    mChangeSubscribeStatusLiveData.setValue(commentLikeBean.getCode() == 200);
                });
    }


    public void requestFollowUser(long userid, boolean followed) {
        Disposable dis = ApiEngine.getInstance().getApiService().getUserFollow(userid, followed ? 1 : 0)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(commentLikeBean -> {
                    mChangeFollowStatusLiveData.setValue(commentLikeBean.getCode() == 200);
                });
    }
}
