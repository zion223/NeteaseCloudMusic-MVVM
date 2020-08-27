package com.netease.music.domain.request;

import androidx.lifecycle.MutableLiveData;

import com.netease.lib_api.model.playlist.TopListDetailBean;
import com.netease.lib_network.ApiEngine;
import com.kunminx.architecture.domain.request.BaseRequest;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArtistRequest extends BaseRequest {


    private MutableLiveData<List<TopListDetailBean.Artist>> mTopArtistData = new MutableLiveData<>();

    public MutableLiveData<List<TopListDetailBean.Artist>> getTopArtistData() {
        if (mTopArtistData == null) {
            mTopArtistData = new MutableLiveData<>();
        }
        return mTopArtistData;
    }

    public void requestHotSinger() {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getHotSingerList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(artistListBean -> mTopArtistData.postValue(artistListBean.getArtists()));
    }

    //根据地区查找
    public void requestHotSingerArea(int type, int area) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getSingerList(type, area)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(artistListBean -> mTopArtistData.postValue(artistListBean.getArtists()));
    }

}
