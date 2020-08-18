package com.netease.music.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.imooc.lib_api.model.album.AlbumOrSongBean;
import com.imooc.lib_api.model.banner.BannerBean;
import com.imooc.lib_api.model.playlist.DailyRecommendBean;
import com.imooc.lib_api.model.playlist.MainRecommendPlayListBean;
import com.imooc.lib_api.model.search.AlbumSearchBean;
import com.imooc.lib_api.model.song.NewSongBean;
import com.imooc.lib_network.ApiEngine;
import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.music.data.config.TYPE;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DiscoverRequest extends BaseRequest {

    //Banner数据
    private MutableLiveData<BannerBean> mBannerLiveData;
    //推荐歌曲数据
    private MutableLiveData<List<MainRecommendPlayListBean.RecommendBean>> mRecommendPlayListLiveData;

    //新歌和新碟数据
    private MutableLiveData<List<AlbumOrSongBean>> mAlbumOrSongLiveData;

    public MutableLiveData<List<AlbumOrSongBean>> getAlbumOrSongLiveData() {
        if (mAlbumOrSongLiveData == null) {
            mAlbumOrSongLiveData = new MutableLiveData<>();
        }
        return mAlbumOrSongLiveData;
    }

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

    public void requestAlbumAndSongData() {
        //新碟上架
        Observable<AlbumSearchBean.ResultBean> albumObservable = ApiEngine.getInstance().getApiService().getTopAlbum(3).subscribeOn(Schedulers.io());
        //新歌速递
        Observable<NewSongBean> newSongObservable = ApiEngine.getInstance().getApiService().getTopSong(0).subscribeOn(Schedulers.io());
        Disposable subscribe = Observable.zip(albumObservable, newSongObservable, (resultBean, newSongBean) -> {
            final List<AlbumOrSongBean> data = new ArrayList<>();
            List<AlbumSearchBean.ResultBean.AlbumsBean> albums = resultBean.getAlbums();
            if (albums.size() >= 3) {
                for (int i = 0; i < 3; i++) {
                    String artistName = albums.get(i).getArtist().getName();
                    String albumName = albums.get(i).getName();
                    String picUrl = albums.get(i).getPicUrl();
                    long id = albums.get(i).getId();
                    data.add(new AlbumOrSongBean(String.valueOf(id), TYPE.ALBUM_ID, picUrl, albumName, artistName));
                }
            }
            final List<DailyRecommendBean.RecommendBean> song = newSongBean.getData();
            if (song.size() >= 3) {
                for (int i = 0; i < 3; i++) {
                    String artistName = song.get(i).getArtists().get(0).getName();
                    String albumName = song.get(i).getName();
                    String picUrl = song.get(i).getAlbum().getPicUrl();
                    long id = song.get(i).getId();
                    data.add(new AlbumOrSongBean(String.valueOf(id), TYPE.SONG_ID, picUrl, albumName, artistName));
                }
            }
            return data;
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(albumOrSongBeans -> {
                    if (albumOrSongBeans.size() >= 5) {
                        mAlbumOrSongLiveData.postValue(albumOrSongBeans);
                    }
                });
    }
}

