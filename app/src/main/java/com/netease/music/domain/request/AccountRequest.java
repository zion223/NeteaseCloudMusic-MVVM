package com.netease.music.domain.request;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.netease.lib_api.model.notification.CommonMessageBean;
import com.netease.lib_api.model.user.LoginBean;
import com.netease.lib_network.ApiEngine;
import com.kunminx.architecture.domain.request.BaseRequest;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AccountRequest extends BaseRequest {

    //登录数据(更改密码)
    private MutableLiveData<LoginBean> loginData = new MutableLiveData<>();

    //验证码数据
    private MutableLiveData<CommonMessageBean> captureData = new MutableLiveData<>();


    public LiveData<LoginBean> getLoginLiveData() {
        return loginData;
    }

    public LiveData<CommonMessageBean> getCaptureLiveData() {
        return captureData;
    }

    //请求登录
    public void requestLogin(String phone, String password) {
        ApiEngine.getInstance().getApiService().login(phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginBean loginBean) {
                        loginData.postValue(loginBean);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    //注册 或修改密码
    public void register(String phone, String password, String code) {

        ApiEngine.getInstance().getApiService().register(phone, password, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginBean loginBean) {
                        loginData.postValue(loginBean);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void sendCapture(String phone) {

        ApiEngine.getInstance().getApiService().capture(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonMessageBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CommonMessageBean message) {
                        captureData.postValue(message);
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
