package com.netease.music.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.imooc.lib_api.model.notification.UserCloudBean;
import com.imooc.lib_network.ApiEngine;
import com.kunminx.architecture.domain.request.BaseRequest;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CloudMusicRequest extends BaseRequest {

    private MutableLiveData<UserCloudBean> mUserCloudLiveData;

    public LiveData<UserCloudBean> getUserCloudLiveData() {
        if (mUserCloudLiveData == null) {
            mUserCloudLiveData = new MutableLiveData<>();
        }
        return mUserCloudLiveData;
    }

    public void requestUserCloudData() {
        ApiEngine.getInstance().getApiService().getUserCloudMusic()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserCloudBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserCloudBean userCloudBean) {
                        mUserCloudLiveData.postValue(userCloudBean);
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
