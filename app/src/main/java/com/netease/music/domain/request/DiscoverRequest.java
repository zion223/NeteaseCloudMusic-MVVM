package com.netease.music.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.lib_api.model.album.AlbumOrSongBean;
import com.netease.lib_api.model.banner.BannerBean;
import com.netease.lib_api.model.playlist.DailyRecommendBean;
import com.netease.lib_api.model.playlist.MainRecommendPlayListBean;
import com.netease.lib_api.model.search.AlbumSearchBean;
import com.netease.lib_api.model.search.TopAlbumBean;
import com.netease.lib_api.model.song.DailyRecommendSongsBean;
import com.netease.lib_api.model.song.NewSongBean;
import com.netease.lib_network.ApiEngine;
import com.netease.lib_network.ExceptionHandle;
import com.netease.lib_network.SimpleObserver;
import com.netease.music.data.config.TypeEnum;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DiscoverRequest extends BaseRequest {

    //Banner数据
    private MutableLiveData<BannerBean> mBannerLiveData;
    //推荐歌曲数据
    private MutableLiveData<List<MainRecommendPlayListBean.RecommendBean>> mRecommendPlayListLiveData;
    //歌曲详情
    private MutableLiveData<DailyRecommendSongsBean> mSongDetailLiveData;

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

    public MutableLiveData<DailyRecommendSongsBean> getSongDetailLiveData() {
        if (mSongDetailLiveData == null) {
            mSongDetailLiveData = new MutableLiveData<>();
        }
        return mSongDetailLiveData;
    }

    public void requestBannerData() {
        ApiEngine.getInstance().getApiService().getBanner("2")
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(new SimpleObserver<BannerBean>() {
                    @Override
                    public void onSuccess(@NonNull @NotNull BannerBean bannerBean) {
                        mBannerLiveData.postValue(bannerBean);
                    }

                    @Override
                    protected void onFailed(ExceptionHandle.ResponseThrowable errorMsg) {

                    }
                });
    }


    public void requestRecommendPlaylistData() {
        ApiEngine.getInstance().getApiService().getRecommendPlayList()
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(new SimpleObserver<MainRecommendPlayListBean>() {
                    @Override
                    public void onSuccess(@NonNull @NotNull MainRecommendPlayListBean bannerBean) {
                        if (bannerBean.getCode() == 200 && bannerBean.getRecommend().size() >= 6) {
                            mRecommendPlayListLiveData.postValue(bannerBean.getRecommend().subList(0, 6));
                        }
                    }

                    @Override
                    protected void onFailed(ExceptionHandle.ResponseThrowable errorMsg) {

                    }
                });
    }

    public void requestAlbumAndSongData() {
        //新碟上架
        Single<TopAlbumBean> albumObservable = ApiEngine.getInstance().getApiService().getTopAlbum(3).subscribeOn(Schedulers.io());
        //新歌速递
        Single<NewSongBean> newSongObservable = ApiEngine.getInstance().getApiService().getTopSong(0).subscribeOn(Schedulers.io());
        Disposable subscribe = Single.zip(albumObservable, newSongObservable, (resultBean, newSongBean) -> {
            final List<AlbumOrSongBean> data = new ArrayList<>();
            List<AlbumSearchBean.ResultBean.AlbumsBean> albums = resultBean.getWeekData();
            if (albums.size() >= 3) {
                for (int i = 0; i < 3; i++) {
                    String artistName = albums.get(i).getArtist().getName();
                    String albumName = albums.get(i).getName();
                    String picUrl = albums.get(i).getPicUrl();
                    long id = albums.get(i).getId();
                    data.add(new AlbumOrSongBean(String.valueOf(id), TypeEnum.ALBUM_ID, picUrl, albumName, artistName));
                }
            }
            final List<DailyRecommendBean.RecommendBean> song = newSongBean.getData();
            if (song.size() >= 3) {
                for (int i = 0; i < 3; i++) {
                    String artistName = song.get(i).getArtists().get(0).getName();
                    String albumName = song.get(i).getName();
                    String picUrl = song.get(i).getAlbum().getPicUrl();
                    long id = song.get(i).getId();
                    data.add(new AlbumOrSongBean(String.valueOf(id), TypeEnum.SONG_ID, picUrl, albumName, artistName));
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

    public void requestSongDetailData(long id) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getSongDetail(String.valueOf(id))
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(songDetailBean -> mSongDetailLiveData.postValue(songDetailBean.getSongs().get(0)));
    }
}

