package com.netease.music.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.lib_api.model.playlist.DailyRecommendBean;
import com.netease.lib_network.ApiEngine;
import com.netease.lib_network.ExceptionHandle;
import com.netease.lib_network.SimpleObserver;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.annotations.NonNull;

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
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(new SimpleObserver<DailyRecommendBean>() {
                    @Override
                    public void onSuccess(@NonNull @NotNull DailyRecommendBean dailyRecommendBean) {
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
                    protected void onFailed(ExceptionHandle.ResponseThrowable errorMsg) {

                    }
                });
    }
}
