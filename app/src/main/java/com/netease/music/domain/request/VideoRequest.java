package com.netease.music.domain.request;

import androidx.lifecycle.MutableLiveData;

import com.imooc.lib_api.model.mv.VideoBean;
import com.imooc.lib_api.model.mv.VideoGroupBean;
import com.imooc.lib_network.ApiEngine;
import com.kunminx.architecture.domain.request.BaseRequest;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VideoRequest extends BaseRequest {


    private MutableLiveData<String[]> mTitleLiveData = new MutableLiveData<>();
    private MutableLiveData<long[]> mVideoTabIdLiveData = new MutableLiveData<>();
    private MutableLiveData<VideoBean> mVideoLiveData = new MutableLiveData<>();


    public MutableLiveData<long[]> getVideoTabIdLiveData() {
        if (mVideoTabIdLiveData == null) {
            mVideoTabIdLiveData = new MutableLiveData<>();
        }
        return mVideoTabIdLiveData;
    }

    public MutableLiveData<String[]> getTitleLiveData() {
        if (mTitleLiveData == null) {
            mTitleLiveData = new MutableLiveData<>();
        }
        return mTitleLiveData;
    }


    public MutableLiveData<VideoBean> getVideoLiveData() {
        if (mVideoLiveData == null) {
            mVideoLiveData = new MutableLiveData<>();
        }
        return mVideoLiveData;
    }

    public void requestVideoGroup() {
        ApiEngine.getInstance().getApiService().getVideoGroup()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoGroupBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VideoGroupBean videoGroupBean) {
                        final List<VideoGroupBean.Data> data = videoGroupBean.getData();

                        // 根据ID查询视频 name为标题 tab数量最多为20个
                        int tabSize = data.size();
                        if (tabSize >= 20) {
                            tabSize = 20;
                        }
                        String[] mTitleData = new String[tabSize];
                        long[] mVideoTabId = new long[tabSize];
                        mVideoTabId[0] = 9999;
                        mTitleData[0] = "推荐";
                        for (int i = 1; i < tabSize; i++) {
                            VideoGroupBean.Data tab = data.get(i);
                            String name = tab.getName();
                            mTitleData[i] = name;
                            mVideoTabId[i] = tab.getId();
                        }
                        mTitleLiveData.postValue(mTitleData);
                        mVideoTabIdLiveData.postValue(mVideoTabId);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //请求标签页下面的视频数据
    public void requestVideoTab(long videoId) {
        Observable<VideoBean> videoObservable = null;
        if ("9999".equals(String.valueOf(videoId))) {
            //推荐
            videoObservable = ApiEngine.getInstance().getApiService().getVideoRecommend();
        } else {
            //普通数据
            videoObservable = ApiEngine.getInstance().getApiService().getVideoTab(videoId);
        }
        videoObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VideoBean videoBean) {
                        mVideoLiveData.postValue(videoBean);
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
