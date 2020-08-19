package com.netease.music.domain.request;

import androidx.lifecycle.MutableLiveData;

import com.imooc.lib_api.model.dj.DjBannerBean;
import com.imooc.lib_api.model.dj.DjRecommendBean;
import com.imooc.lib_network.ApiEngine;
import com.kunminx.architecture.domain.request.BaseRequest;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RadioRequest extends BaseRequest {


    //banner数据和推荐电台数据
    private MutableLiveData<DjBannerBean> mBannerLiveData;
    private MutableLiveData<DjRecommendBean> mRecommendRadioLiveData;

    public MutableLiveData<DjBannerBean> getBannerLiveData() {
        if (mBannerLiveData == null) {
            mBannerLiveData = new MutableLiveData<>();
        }
        return mBannerLiveData;
    }

    public MutableLiveData<DjRecommendBean> getRadioRecommendLiveData() {
        if (mRecommendRadioLiveData == null) {
            mRecommendRadioLiveData = new MutableLiveData<>();
        }
        return mRecommendRadioLiveData;
    }

    public void requestRadioBanner() {
        ApiEngine.getInstance().getApiService().getRadioBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DjBannerBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DjBannerBean djBannerBean) {
                        mBannerLiveData.postValue(djBannerBean);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //推荐电台
    public void requestRecommendRadio() {
        ApiEngine.getInstance().getApiService().getRadioRecommend()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DjRecommendBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DjRecommendBean djRecommendBean) {
                        if (djRecommendBean.getCode() == 200) {
                            mRecommendRadioLiveData.postValue(djRecommendBean);
                        }
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
