package com.netease.music.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.imooc.lib_api.model.playlist.DailyRecommendBean;
import com.imooc.lib_network.ApiEngine;
import com.kunminx.architecture.domain.request.BaseRequest;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DailyRecommendRequest extends BaseRequest {

    private MutableLiveData<DailyRecommendBean> mDailyRecommendLiveData;


    public LiveData<DailyRecommendBean> getDailRecommendLiveData() {
        if (mDailyRecommendLiveData == null) {
            mDailyRecommendLiveData = new MutableLiveData<>();
        }
        return mDailyRecommendLiveData;
    }

    public void requestDailyRecommendMusic() {
        ApiEngine.getInstance().getApiService().getDailyRecommend()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DailyRecommendBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DailyRecommendBean dailyRecommendBean) {
                        //推荐原因
                        List<DailyRecommendBean.RecommendReason> recommendReasons = dailyRecommendBean.getData().getRecommendReasons();
                        int recommendMusicSize = dailyRecommendBean.getData().getDailySongs().size();
                        int recommendReasonSize = dailyRecommendBean.getData().getRecommendReasons().size();
                        for (int i = 0; i < recommendReasonSize; i++) {
                            long sondId = recommendReasons.get(i).getSongId();
                            String reason = recommendReasons.get(i).getReason();
                            for (int j = 0; j < recommendMusicSize; j++) {
                                if (sondId == dailyRecommendBean.getData().getDailySongs().get(j).getId()) {
                                    dailyRecommendBean.getData().getDailySongs().get(j).setRecommendReason(reason);
                                }
                            }
                        }
                        mDailyRecommendLiveData.postValue(dailyRecommendBean);
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
