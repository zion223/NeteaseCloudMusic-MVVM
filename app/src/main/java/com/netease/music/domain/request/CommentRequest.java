package com.netease.music.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.lib_api.model.playlist.PlayListCommentEntity;
import com.netease.lib_api.model.song.PlayListCommentBean;
import com.netease.lib_network.ApiEngine;
import com.netease.lib_network.ExceptionHandle;
import com.netease.lib_network.SimpleObserver;
import com.netease.music.data.config.TypeEnum;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public class CommentRequest extends BaseRequest {


    private MutableLiveData<List<PlayListCommentEntity>> mCommentLiveData;
    private MutableLiveData<Integer> mCommentSizeLiveData;


    public LiveData<List<PlayListCommentEntity>> getCommentLiveData() {
        if (mCommentLiveData == null) {
            mCommentLiveData = new MutableLiveData<>();
        }
        return mCommentLiveData;
    }

    public MutableLiveData<Integer> getCommentSizeLiveData() {
        if (mCommentSizeLiveData == null) {
            mCommentSizeLiveData = new MutableLiveData<>();
        }
        return mCommentSizeLiveData;
    }

    public void requestCommentData(TypeEnum type, String id) {

        Single<PlayListCommentBean> commentObservable = null;

        switch (type) {
            case SONG:
                commentObservable = ApiEngine.getInstance().getApiService().getMusicComment(id);
                break;
            case ALBUM:
                commentObservable = ApiEngine.getInstance().getApiService().getAlbumComment(id);
                break;
            case PLAYLIST:
                commentObservable = ApiEngine.getInstance().getApiService().getPlayListComment(id);
                break;
            default:
                break;
        }
        if (commentObservable != null) {
            commentObservable.compose(ApiEngine.getInstance().applySchedulers())
                    .subscribe(new SimpleObserver<PlayListCommentBean>() {

                        @Override
                        public void onSuccess(@NonNull @NotNull PlayListCommentBean commentBean) {
                            mCommentSizeLiveData.postValue(commentBean.getTotal());
                            final ArrayList<PlayListCommentEntity> entities = new ArrayList<>();

                            entities.add(new PlayListCommentEntity("精彩评论"));
                            if (commentBean.getHotComments() != null && commentBean.getHotComments().size() > 0) {
                                for (int i = 0; i < commentBean.getHotComments().size(); i++) {
                                    entities.add(new PlayListCommentEntity(commentBean.getHotComments().get(i)));
                                }
                            }
                            entities.add(new PlayListCommentEntity("最新评论", String.valueOf(commentBean.getTotal())));
                            for (int j = 0; j < commentBean.getComments().size(); j++) {
                                entities.add(new PlayListCommentEntity(commentBean.getComments().get(j)));
                            }
                            mCommentLiveData.postValue(entities);
                        }

                        @Override
                        protected void onFailed(ExceptionHandle.ResponseThrowable errorMsg) {

                        }
                    });
        }
    }

}
