package com.netease.music.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.imooc.lib_api.model.banner.BannerBean;
import com.imooc.lib_api.model.playlist.MainRecommendPlayListBean;
import com.imooc.lib_network.ApiEngine;
import com.kunminx.architecture.domain.request.BaseRequest;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DiscoverRequest extends BaseRequest {

    //Banner数据
    private MutableLiveData<BannerBean> mBannerLiveData;
    //推荐歌曲数据
    private MutableLiveData<List<MainRecommendPlayListBean.RecommendBean>> mRecommendPlayListLiveData;

    //TODO tip 向 ui 层提供的 request LiveData，使用抽象的 LiveData 而不是 MutableLiveData
    // 如此是为了来自数据层的数据，在 ui 层中只读，以避免团队新手不可预期的误用

    public LiveData<BannerBean> getBannerLiveData() {
        if (mBannerLiveData == null) {
            mBannerLiveData = new MutableLiveData<>();
        }
        return mBannerLiveData;
    }

    public LiveData<List<MainRecommendPlayListBean.RecommendBean>> getRecommendPlaylistLiveData() {
        if (mRecommendPlayListLiveData == null) {
            mRecommendPlayListLiveData = new MutableLiveData<>();
        }
        return mRecommendPlayListLiveData;
    }

    public void requestBannerData() {
        ApiEngine.getInstance().getApiService().getBanner("2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BannerBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BannerBean bannerBean) {
                        mBannerLiveData.postValue(bannerBean);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void requestRecommendPlaylistData() {
        ApiEngine.getInstance().getApiService().getRecommendPlayList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MainRecommendPlayListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MainRecommendPlayListBean bannerBean) {
                        if (bannerBean.getCode() == 200 && bannerBean.getRecommend().size() >= 6) {
                            mRecommendPlayListLiveData.postValue(bannerBean.getRecommend().subList(0, 6));
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

