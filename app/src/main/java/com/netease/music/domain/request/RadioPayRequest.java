package com.netease.music.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.imooc.lib_api.model.dj.DjPaygiftBean;
import com.imooc.lib_network.ApiEngine;
import com.kunminx.architecture.domain.request.BaseRequest;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DjPaygiftBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DjPaygiftBean djPaygiftBean) {
                        mRadioPayLiveData.postValue(djPaygiftBean);
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
