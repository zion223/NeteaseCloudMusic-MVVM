package com.netease.music.domain.request;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.data.response.ResponseStatus;
import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.lib_api.model.notification.CommonMessageBean;
import com.netease.lib_network.ApiEngine;
import com.netease.lib_network.ExceptionHandle;
import com.netease.lib_network.SimpleObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DrawerRequest extends BaseRequest {

    private final MutableLiveData<DataResult<CommonMessageBean>> mLoginOutLiveData = new MutableLiveData<>();


    public LiveData<DataResult<CommonMessageBean>> getLoginOutLiveData() {
        return mLoginOutLiveData;
    }

    public void requestLoginOut() {
        ApiEngine.getInstance().getApiService().logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<CommonMessageBean>() {
                    @Override
                    protected void onSuccess(CommonMessageBean result) {
                        // 业务逻辑操作
                        ResponseStatus responseStatus = new ResponseStatus(String.valueOf(result.getCode()), result.getCode() == 200);
                        mLoginOutLiveData.postValue(new DataResult<>(result, responseStatus));
                    }

                    @Override
                    protected void onFailed(ExceptionHandle.ResponseThrowable errorMsg) {
                        mLoginOutLiveData.postValue(new DataResult<>(null, new ResponseStatus(String.valueOf(errorMsg.code), false)));
                    }
                });
    }
}
