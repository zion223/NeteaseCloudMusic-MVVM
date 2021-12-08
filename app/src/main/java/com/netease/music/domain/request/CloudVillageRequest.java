package com.netease.music.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.lib_api.model.user.MainEventBean;
import com.netease.lib_network.ApiEngine;
import com.netease.lib_network.ExceptionHandle;
import com.netease.lib_network.SimpleObserver;

import org.jetbrains.annotations.NotNull;

import io.reactivex.annotations.NonNull;

public class CloudVillageRequest extends BaseRequest {

    private MutableLiveData<MainEventBean> mEventLiveData;

    public LiveData<MainEventBean> getEventLiveData() {
        if (mEventLiveData == null) {
            mEventLiveData = new MutableLiveData<>();
        }
        return mEventLiveData;
    }

    public void requestUserEventData() {
        ApiEngine.getInstance().getApiService().getMainEvent()
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(new SimpleObserver<MainEventBean>() {
                    @Override
                    protected void onFailed(ExceptionHandle.ResponseThrowable errorMsg) {

                    }

                    @Override
                    public void onSuccess(@NonNull @NotNull MainEventBean mainEventBean) {
                        mEventLiveData.postValue(mainEventBean);
                    }

                });
    }

}
