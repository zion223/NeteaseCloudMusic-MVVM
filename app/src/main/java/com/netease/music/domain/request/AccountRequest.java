package com.netease.music.domain.request;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.data.response.ResponseStatus;
import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.lib_api.model.notification.CommonMessageBean;
import com.netease.lib_api.model.user.LoginBean;
import com.netease.lib_network.ApiEngine;
import com.netease.lib_network.ExceptionHandle;
import com.netease.lib_network.SimpleObserver;


public class AccountRequest extends BaseRequest {

    //登录数据(更改密码)
    private MutableLiveData<DataResult<LoginBean>> loginData = new MutableLiveData<>();

    //验证码数据
    private MutableLiveData<DataResult<CommonMessageBean>> captureData = new MutableLiveData<>();


    public LiveData<DataResult<LoginBean>> getLoginLiveData() {
        return loginData;
    }

    public LiveData<DataResult<CommonMessageBean>> getCaptureLiveData() {
        return captureData;
    }

    //请求登录
    public void requestLogin(String phone, String password) {
        ApiEngine.getInstance().getApiService().login(phone, password)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(new SimpleObserver<LoginBean>() {

                    @Override
                    protected void onSuccess(LoginBean result) {
                        ResponseStatus responseStatus = new ResponseStatus(String.valueOf(result.getCode()), result.getCode() == 200);
                        loginData.postValue(new DataResult<>(result, responseStatus));
                    }

                    @Override
                    protected void onFailed(ExceptionHandle.ResponseThrowable result) {
                        ResponseStatus responseStatus = new ResponseStatus(String.valueOf(result.code), false);
                        loginData.postValue(new DataResult<>(null, responseStatus));
                    }
                });

    }

    //注册 或修改密码
    public void register(String phone, String password, String code) {

        ApiEngine.getInstance().getApiService().register(phone, password, code)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(new SimpleObserver<LoginBean>() {
                    @Override
                    protected void onSuccess(LoginBean loginBean) {
                        ResponseStatus responseStatus = new ResponseStatus(String.valueOf(loginBean.getCode()), loginBean.getCode() == 200);
                        loginData.postValue(new DataResult<>(loginBean, responseStatus));
                    }

                    @Override
                    protected void onFailed(ExceptionHandle.ResponseThrowable result) {
                        ResponseStatus responseStatus = new ResponseStatus(String.valueOf(result.code), false);
                        loginData.postValue(new DataResult<>(null, responseStatus));
                    }
                });

    }

    // 发送验证码
    public void sendCapture(String phone) {

        ApiEngine.getInstance().getApiService().capture(phone)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(new SimpleObserver<CommonMessageBean>() {
                    @Override
                    protected void onSuccess(CommonMessageBean result) {
                        ResponseStatus responseStatus = new ResponseStatus(String.valueOf(result.getCode()), result.getCode() == 200);
                        captureData.postValue(new DataResult<>(result, responseStatus));
                    }

                    @Override
                    protected void onFailed(ExceptionHandle.ResponseThrowable errorMsg) {
                        ResponseStatus responseStatus = new ResponseStatus(String.valueOf(errorMsg.code), false);
                        loginData.postValue(new DataResult<>(null, responseStatus));
                    }
                });
    }
}
