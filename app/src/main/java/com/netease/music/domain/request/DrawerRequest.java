package com.netease.music.domain.request;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.data.response.ResponseStatus;
import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.lib_api.model.notification.CommonMessageBean;
import com.netease.lib_network.ApiEngine;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DrawerRequest extends BaseRequest {

    private final MutableLiveData<DataResult<CommonMessageBean>> mLoginOutLiveData = new MutableLiveData<>();


    public LiveData<DataResult<CommonMessageBean>> getLoginOutLiveData() {
        return mLoginOutLiveData;
    }

    public void requestLoginOut() {
        Disposable subscribe = ApiEngine.getInstance().getApiService().logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(commonMessageBean -> {
                    // 业务逻辑操作
                    ResponseStatus responseStatus = new ResponseStatus(String.valueOf(commonMessageBean.getCode()), commonMessageBean.getCode() == 200);
                    mLoginOutLiveData.postValue(new DataResult<>(commonMessageBean, responseStatus));
                });
    }
}
