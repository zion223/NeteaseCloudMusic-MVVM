package com.netease.music.domain.request;

import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.lib_api.model.dj.DjBannerBean;
import com.netease.lib_api.model.dj.DjDetailBean;
import com.netease.lib_api.model.dj.DjProgramBean;
import com.netease.lib_api.model.dj.DjRecommendBean;
import com.netease.lib_api.model.notification.CommonMessageBean;
import com.netease.lib_network.ApiEngine;
import com.netease.lib_network.ExceptionHandle;
import com.netease.lib_network.SimpleObserver;

import org.jetbrains.annotations.NotNull;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class RadioRequest extends BaseRequest {


    //banner数据和推荐电台数据
    private MutableLiveData<DjBannerBean> mBannerLiveData;
    private MutableLiveData<DjRecommendBean> mRecommendRadioLiveData;
    private MutableLiveData<DjDetailBean> mRadioDetailLiveData;
    private MutableLiveData<DjProgramBean> mRadioProgramLiveData;
    //订阅或取消订阅
    private MutableLiveData<CommonMessageBean> mRadioSubLiveData;

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

    public MutableLiveData<CommonMessageBean> getRadioSubLiveData() {
        if (mRadioSubLiveData == null) {
            mRadioSubLiveData = new MutableLiveData<>();
        }
        return mRadioSubLiveData;
    }

    public void requestRadioBanner() {
        ApiEngine.getInstance().getApiService().getRadioBanner()
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(new SimpleObserver<DjBannerBean>() {

                    @Override
                    public void onSuccess(@NonNull @NotNull DjBannerBean djBannerBean) {
                        mBannerLiveData.postValue(djBannerBean);
                    }

                    @Override
                    protected void onFailed(ExceptionHandle.ResponseThrowable errorMsg) {

                    }
                });
    }

    //推荐电台
    public void requestRecommendRadio() {
        ApiEngine.getInstance().getApiService().getRadioRecommend()
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(new SimpleObserver<DjRecommendBean>() {

                    @Override
                    public void onSuccess(@NonNull @NotNull DjRecommendBean djRecommendBean) {
                        if (djRecommendBean.getCode() == 200) {
                            mRecommendRadioLiveData.postValue(djRecommendBean);
                        }
                    }

                    @Override
                    protected void onFailed(ExceptionHandle.ResponseThrowable errorMsg) {

                    }
                });
    }

    public void requestRadioDeatil(String radioId) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getRadioDetail(radioId)
                .compose(ApiEngine.getInstance().applySchedulers())
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
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(djDetailBean -> mRadioProgramLiveData.postValue(djDetailBean));
    }

    //订阅或取消订阅
    public void requestSubRadio(String radioId, boolean isSub) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getSubRadio(radioId, isSub ? 1 : 0)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(djSubBean -> mRadioSubLiveData.postValue(djSubBean));
    }

}
