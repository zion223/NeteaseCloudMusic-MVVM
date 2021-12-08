package com.netease.music.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.lib_api.model.search.AlbumSearchBean;
import com.netease.lib_network.ApiEngine;
import com.netease.lib_network.ExceptionHandle;
import com.netease.lib_network.SimpleObserver;

import org.jetbrains.annotations.NotNull;

import io.reactivex.annotations.NonNull;

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
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(new SimpleObserver<AlbumSearchBean.ResultBean>() {
                    @Override
                    public void onSuccess(@NonNull AlbumSearchBean.@NotNull ResultBean resultBean) {
                        mAlbumLiveData.postValue(resultBean);
                    }

                    @Override
                    protected void onFailed(ExceptionHandle.ResponseThrowable errorMsg) {

                    }
                });
    }
}
