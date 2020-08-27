package com.netease.music.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.netease.lib_api.model.search.HotSearchDetailBean;
import com.netease.lib_network.ApiEngine;
import com.kunminx.architecture.domain.request.BaseRequest;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchRequest extends BaseRequest {

    private MutableLiveData<HotSearchDetailBean> mHotSearchLiveData;

    //TODO tip 向 ui 层提供的 request LiveData，使用抽象的 LiveData 而不是 MutableLiveData
    // 如此是为了来自数据层的数据，在 ui 层中只读，以避免团队新手不可预期的误用

    public LiveData<HotSearchDetailBean> getHotSearchLiveData() {
        if (mHotSearchLiveData == null) {
            mHotSearchLiveData = new MutableLiveData<>();
        }
        return mHotSearchLiveData;
    }

    public void requestHotSearch() {
        ApiEngine.getInstance().getApiService().getSearchHotDetail()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotSearchDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HotSearchDetailBean hotSearchDetailBean) {
                        mHotSearchLiveData.postValue(hotSearchDetailBean);
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
