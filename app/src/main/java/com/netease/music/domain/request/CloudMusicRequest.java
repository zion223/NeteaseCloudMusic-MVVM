package com.netease.music.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.lib_api.model.notification.UserCloudBean;
import com.netease.lib_network.ApiEngine;
import com.netease.lib_network.ExceptionHandle;
import com.netease.lib_network.SimpleObserver;

import org.jetbrains.annotations.NotNull;

import io.reactivex.annotations.NonNull;

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
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(new SimpleObserver<UserCloudBean>() {
                    @Override
                    public void onSuccess(@NonNull @NotNull UserCloudBean userCloudBean) {
                        mUserCloudLiveData.postValue(userCloudBean);
                    }

                    @Override
                    protected void onFailed(ExceptionHandle.ResponseThrowable errorMsg) {

                    }
                });
    }

}
