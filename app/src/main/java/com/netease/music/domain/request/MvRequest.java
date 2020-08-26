package com.netease.music.domain.request;

import androidx.lifecycle.MutableLiveData;

import com.imooc.lib_api.model.mv.MvBean;
import com.imooc.lib_api.model.mv.MvTopBean;
import com.imooc.lib_network.ApiEngine;
import com.kunminx.architecture.domain.request.BaseRequest;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MvRequest extends BaseRequest {


    private MutableLiveData<MvTopBean> mvTopBeanLiveData;
    private MutableLiveData<MvBean> mAllMvLiveData;

    public MutableLiveData<MvTopBean> getMvTopBeanLiveData() {
        if (mvTopBeanLiveData == null) {
            mvTopBeanLiveData = new MutableLiveData<>();
        }
        return mvTopBeanLiveData;
    }

    public MutableLiveData<MvBean> getAllMvLiveData() {
        if (mAllMvLiveData == null) {
            mAllMvLiveData = new MutableLiveData<>();
        }
        return mAllMvLiveData;
    }

    public void requestMvTop() {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getMvTop()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mvTopBean -> mvTopBeanLiveData.postValue(mvTopBean));
    }

    public void requestAllMv(String area, String type, String order, int limit) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getAllMv(area, type, order, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mvTopBean -> mAllMvLiveData.postValue(mvTopBean));
    }
}
