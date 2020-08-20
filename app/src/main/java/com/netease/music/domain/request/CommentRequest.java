package com.netease.music.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.imooc.lib_api.model.song.PlayListCommentBean;
import com.imooc.lib_network.ApiEngine;
import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.music.data.config.TYPE;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommentRequest extends BaseRequest {


    private MutableLiveData<PlayListCommentBean> mCommentLiveData;


    public LiveData<PlayListCommentBean> getCommentLiveData() {
        if (mCommentLiveData == null) {
            mCommentLiveData = new MutableLiveData<>();
        }
        return mCommentLiveData;
    }


    public void requestCommentData(TYPE type, String id) {

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
                        public void onNext(PlayListCommentBean playListCommentBean) {
                            mCommentLiveData.postValue(playListCommentBean);
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
