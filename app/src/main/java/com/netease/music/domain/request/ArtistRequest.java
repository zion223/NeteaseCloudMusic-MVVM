package com.netease.music.domain.request;

import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.lib_api.model.playlist.TopListDetailBean;
import com.netease.lib_network.ApiEngine;

import java.util.List;

import io.reactivex.disposables.Disposable;

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
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(artistListBean -> mTopArtistData.postValue(artistListBean.getArtists()));
    }

    //根据地区查找
    public void requestHotSingerArea(int type, int area) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getSingerList(type, area)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(artistListBean -> mTopArtistData.postValue(artistListBean.getArtists()));
    }

}
