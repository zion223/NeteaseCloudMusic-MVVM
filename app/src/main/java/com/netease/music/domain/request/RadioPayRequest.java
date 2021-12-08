package com.netease.music.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.lib_api.model.dj.DjPaygiftBean;
import com.netease.lib_network.ApiEngine;
import com.netease.lib_network.ExceptionHandle;
import com.netease.lib_network.SimpleObserver;

import org.jetbrains.annotations.NotNull;

import io.reactivex.annotations.NonNull;

public class RadioPayRequest extends BaseRequest {

    private MutableLiveData<DjPaygiftBean> mRadioPayLiveData;

    public LiveData<DjPaygiftBean> getRadioPayLiveData() {
        if (mRadioPayLiveData == null) {
            mRadioPayLiveData = new MutableLiveData<>();
        }
        return mRadioPayLiveData;
    }

    public void requestRadioPayData(int limit, int offset) {
        ApiEngine.getInstance().getApiService().getDjPaygift(limit, offset)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(new SimpleObserver<DjPaygiftBean>() {

                    @Override
                    public void onSuccess(@NonNull @NotNull DjPaygiftBean djPaygiftBean) {
                        mRadioPayLiveData.postValue(djPaygiftBean);
                    }

                    @Override
                    protected void onFailed(ExceptionHandle.ResponseThrowable errorMsg) {

                    }
                });
    }

}
