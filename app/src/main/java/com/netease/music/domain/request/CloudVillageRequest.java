package com.netease.music.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.netease.lib_api.model.user.MainEventBean;
import com.netease.lib_network.ApiEngine;
import com.kunminx.architecture.domain.request.BaseRequest;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MainEventBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MainEventBean bean) {
                        mEventLiveData.postValue(bean);
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
