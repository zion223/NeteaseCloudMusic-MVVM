package com.netease.music.domain.request;

import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.lib_api.model.mv.MvBean;
import com.netease.lib_api.model.mv.MvTopBean;
import com.netease.lib_api.model.playlist.PlayListCommentEntity;
import com.netease.lib_api.model.search.SingerSongSearchBean;
import com.netease.lib_network.ApiEngine;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

public class MvRequest extends BaseRequest {


    private MutableLiveData<MvTopBean> mvTopBeanLiveData;
    private MutableLiveData<MvBean> mAllMvLiveData;
    private MutableLiveData<MvBean.MvDetailBean> mMvDetailLiveData;
    private MutableLiveData<SingerSongSearchBean> mArtistInfoLiveData;
    private MutableLiveData<ArrayList<PlayListCommentEntity>> mMvCommentLiveData;
    private MutableLiveData<Boolean> mChangeLikeStatusLiveData;

    public MutableLiveData<MvTopBean> getMvTopBeanLiveData() {
        if (mvTopBeanLiveData == null) {
            mvTopBeanLiveData = new MutableLiveData<>();
        }
        return mvTopBeanLiveData;
    }

    public MutableLiveData<MvBean> getAllMvLiveData() {
        if (mAllMvLiveData == null) {
            mAllMvLiveData = new MutableLiveData<>();
        }
        return mAllMvLiveData;
    }

    public MutableLiveData<MvBean.MvDetailBean> getMvDetailLiveData() {
        if (mMvDetailLiveData == null) {
            mMvDetailLiveData = new MutableLiveData<>();
        }
        return mMvDetailLiveData;
    }

    public MutableLiveData<ArrayList<PlayListCommentEntity>> getMvCommentLiveData() {
        if (mMvCommentLiveData == null) {
            mMvCommentLiveData = new MutableLiveData<>();
        }
        return mMvCommentLiveData;
    }

    public MutableLiveData<SingerSongSearchBean> getArtistInfoLiveData() {
        if (mArtistInfoLiveData == null) {
            mArtistInfoLiveData = new MutableLiveData<>();
        }
        return mArtistInfoLiveData;
    }

    public MutableLiveData<Boolean> getChangeLikeStatusLiveData() {
        if (mChangeLikeStatusLiveData == null) {
            mChangeLikeStatusLiveData = new MutableLiveData<>();
        }
        return mChangeLikeStatusLiveData;
    }

    //请求MV详情
    public void requestMvDetail(String mvId) {
        Disposable requestMvDetail = ApiEngine.getInstance().getApiService().getMvDetail(mvId)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(mvDetail -> mMvDetailLiveData.postValue(mvDetail.getData()));
    }

    //请求MV评论
    public void requestMvComment(String mvId) {
        Disposable requestMvComment = ApiEngine.getInstance().getApiService().getMvComment(mvId)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(mvTopBean -> {
                    final ArrayList<PlayListCommentEntity> entities = new ArrayList<>();
                    if (mvTopBean.getHotComments().size() > 0) {
                        entities.add(new PlayListCommentEntity("精彩评论"));
                        for (int i = 0; i < mvTopBean.getHotComments().size(); i++) {
                            entities.add(new PlayListCommentEntity(mvTopBean.getHotComments().get(i)));
                        }
                    }
                    entities.add(new PlayListCommentEntity("最新评论", String.valueOf(mvTopBean.getComments().size())));
                    for (int j = 0; j < mvTopBean.getComments().size(); j++) {
                        entities.add(new PlayListCommentEntity(mvTopBean.getComments().get(j)));
                    }
                    mMvCommentLiveData.postValue(entities);
                });
    }

    //请求歌手信息
    public void requestArtistInfo(String artisdId) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getSingerHotSong(artisdId)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(artistInfo -> mArtistInfoLiveData.postValue(artistInfo));
    }

    //请求MV排行榜
    public void requestMvTop() {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getMvTop()
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(mvTopBean -> mvTopBeanLiveData.postValue(mvTopBean));
    }

    //请求全部MV
    public void requestAllMv(String area, String type, String order, int limit) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getAllMv(area, type, order, limit)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(mvTopBean -> mAllMvLiveData.postValue(mvTopBean));
    }

    //给MV点赞
    public void requestLikeMv(String mvId, boolean like) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().likeResource(mvId, like ? 1 : 0, 1)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(commentLikeBean -> {
                    mChangeLikeStatusLiveData.setValue(commentLikeBean.getCode() == 200);
                });
    }
}
