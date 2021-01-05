package com.netease.music.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.netease.lib_api.model.playlist.PlayListCommentEntity;
import com.netease.lib_api.model.song.PlayListCommentBean;
import com.netease.lib_network.ApiEngine;
import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.music.data.config.TypeEnum;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

        Observable<PlayListCommentBean> commentObservable = null;

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
            commentObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<PlayListCommentBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(PlayListCommentBean commentBean) {
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
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

}
