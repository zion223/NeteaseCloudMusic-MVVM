package com.netease.music.domain.request;

import androidx.lifecycle.MutableLiveData;

import com.imooc.lib_api.model.dj.DjBannerBean;
import com.imooc.lib_api.model.dj.DjDetailBean;
import com.imooc.lib_api.model.dj.DjProgramBean;
import com.imooc.lib_api.model.dj.DjRecommendBean;
import com.imooc.lib_api.model.dj.DjSubBean;
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
    private MutableLiveData<DjDetailBean> mRadioDetailLiveData;
    private MutableLiveData<DjProgramBean> mRadioProgramLiveData;
    //订阅或取消订阅
    private MutableLiveData<DjSubBean> mRadioSubLiveData;

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

    public MutableLiveData<DjDetailBean> getRadioDetailLiveData() {
        if (mRadioDetailLiveData == null) {
            mRadioDetailLiveData = new MutableLiveData<>();
        }
        return mRadioDetailLiveData;
    }

    public MutableLiveData<DjProgramBean> getRadioProgramLiveData() {
        if (mRadioProgramLiveData == null) {
            mRadioProgramLiveData = new MutableLiveData<>();
        }
        return mRadioProgramLiveData;
    }

    public MutableLiveData<DjSubBean> getRadioSubLiveData() {
        if (mRadioSubLiveData == null) {
            mRadioSubLiveData = new MutableLiveData<>();
        }
        return mRadioSubLiveData;
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

    public void requestRadioDeatil(String radioId) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getRadioDetail(radioId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(djDetailBean -> mRadioDetailLiveData.postValue(djDetailBean));
    }

    /**
     * 电台节目
     * PS.说明 : 登陆后调用此接口 , 传入rid, 可查看对应电台的电台节目以及对应的 id,
     * 需要 注意的是这个接口返回的 mp3Url 已经无效 , 都为 null, 但是通过调用 /song/url 这 个接口 ,
     * 传入节目 id 仍然能获取到节目音频 , 如 /song/url?id=478446370 获取代码时间的一个节目的音频
     * limit : 返回数量 , 默认为 30
     * offset : 偏移数量，用于分页 , 如 :( 页数 -1)*30, 其中 30 为 limit 的值 , 默认为 0
     * asc : 排序方式,默认为 false (新 => 老 ) 设置 true 可改为 老 => 新
     */
    public void requestRadioProgram(String radioId, boolean asc) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getRadioProgram(radioId, asc)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(djDetailBean -> mRadioProgramLiveData.postValue(djDetailBean));
    }

    //订阅或取消订阅
    public void requestSubRadio(String radioId, boolean isSub) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getSubRadio(radioId, isSub ? 1 : 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(djSubBean -> mRadioSubLiveData.postValue(djSubBean));
    }

}
