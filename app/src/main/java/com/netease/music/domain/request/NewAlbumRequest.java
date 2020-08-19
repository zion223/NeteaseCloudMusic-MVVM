package com.netease.music.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.imooc.lib_api.model.search.AlbumSearchBean;
import com.imooc.lib_network.ApiEngine;
import com.kunminx.architecture.domain.request.BaseRequest;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewAlbumRequest extends BaseRequest {


    //Banner数据
    private MutableLiveData<AlbumSearchBean.ResultBean> mAlbumLiveData;

    public LiveData<AlbumSearchBean.ResultBean> getAlbumLiveData() {
        if (mAlbumLiveData == null) {
            mAlbumLiveData = new MutableLiveData<>();
        }
        return mAlbumLiveData;
    }


    public void requestAlbumData() {
        ApiEngine.getInstance().getApiService().getNewAlbum()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AlbumSearchBean.ResultBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AlbumSearchBean.ResultBean resultBean) {
                        mAlbumLiveData.postValue(resultBean);
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
