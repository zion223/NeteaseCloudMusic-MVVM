package com.netease.music.domain.request;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.domain.request.BaseRequest;
import com.netease.lib_api.model.album.AlbumDetailBean;
import com.netease.lib_api.model.album.AlbumDynamicBean;
import com.netease.lib_api.model.notification.CommonMessageBean;
import com.netease.lib_api.model.playlist.PlaylistDetailBean;
import com.netease.lib_api.model.song.DailyRecommendSongsBean;
import com.netease.lib_api.model.song.SongDetailBean;
import com.netease.lib_network.ApiEngine;
import com.netease.lib_network.ExceptionHandle;
import com.netease.lib_network.SimpleObserver;
import com.netease.music.data.config.TypeEnum;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SongListDetailRequest extends BaseRequest {


    //查询歌单数据
    private MutableLiveData<PlaylistDetailBean> mPlayListLiveData;
    //查询专辑数据
    private MutableLiveData<AlbumDetailBean> mAlbumDetailLiveData;

    //歌曲
    private MutableLiveData<List<DailyRecommendSongsBean>> mSongDetailLiveData;
    //改变对专辑或歌单的收藏状态
    private MutableLiveData<Boolean> mChangeSubStatusLiveData;

    public LiveData<PlaylistDetailBean> getPlayListLiveData() {
        if (mPlayListLiveData == null) {
            mPlayListLiveData = new MutableLiveData<>();
        }
        return mPlayListLiveData;
    }

    public LiveData<Boolean> getChangeSubStatusLiveData() {
        if (mChangeSubStatusLiveData == null) {
            mChangeSubStatusLiveData = new MutableLiveData<>();
        }
        return mChangeSubStatusLiveData;
    }

    public LiveData<List<DailyRecommendSongsBean>> getSongDetailLiveData() {
        if (mSongDetailLiveData == null) {
            mSongDetailLiveData = new MutableLiveData<>();
        }
        return mSongDetailLiveData;
    }

    public LiveData<AlbumDetailBean> getAlbumDetailLiveData() {
        if (mAlbumDetailLiveData == null) {
            mAlbumDetailLiveData = new MutableLiveData<>();
        }
        return mAlbumDetailLiveData;
    }

    //收藏  歌单/专辑
    @SuppressLint("CheckResult")
    public void requestPlayListMusicLiveData(long id) {
        //先请求歌单数据 在请求歌单的歌曲
        ApiEngine.getInstance().getApiService().getPlaylistDetail(id)
                .compose(ApiEngine.getInstance().applySchedulers())
                .doAfterSuccess(playlistDetailBean -> mPlayListLiveData.postValue(playlistDetailBean))
                .observeOn(Schedulers.io())
                .flatMap((Function<PlaylistDetailBean, Single<SongDetailBean>>) playlistDetailBean -> {
                    List<PlaylistDetailBean.PlaylistBean.TrackIdsBean> trackIds = playlistDetailBean.getPlaylist().getTrackIds();
                    int ids = trackIds.size();
                    if (ids > 80) {
                        ids = 80;
                    }
                    final StringBuilder params = new StringBuilder();
                    for (int i = 0; i < ids; i++) {
                        //最后一个参数不加逗号
                        if (i == ids - 1) {
                            params.append(trackIds.get(i).getId());
                        } else {
                            params.append(trackIds.get(i).getId()).append(",");
                        }
                    }
                    //请求歌曲数据
                    return ApiEngine.getInstance().getApiService().getSongDetail(params.toString());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songDetailBean -> mSongDetailLiveData.postValue(songDetailBean.getSongs()), throwable -> {

                });

    }

    //请求专辑的数据  专辑详情和专辑动态
    public void requestAlbumLiveData(long id) {
        Single<AlbumDetailBean> albumDetailObservable = ApiEngine.getInstance().getApiService().getAlbumDetail(id).subscribeOn(Schedulers.io());
        Single<AlbumDynamicBean> albumDynamicObservable = ApiEngine.getInstance().getApiService().getAlbumDynamic(id).subscribeOn(Schedulers.io());
        Disposable subscribe = Single.zip(albumDetailObservable, albumDynamicObservable, (albumDetailBean, albumDynamicBean) -> {
            //转换数据
            albumDetailBean.setCommentCount(albumDynamicBean.getCommentCount());
            albumDetailBean.setShareCount(albumDynamicBean.getShareCount());
            albumDetailBean.setSubCount(albumDynamicBean.getSubCount());
            albumDetailBean.setSub(albumDynamicBean.isSub());
            //专辑的歌曲
            mSongDetailLiveData.postValue(albumDetailBean.getSongs());

            return albumDetailBean;
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    mAlbumDetailLiveData.postValue(bean);
                });
    }

    //改变对专辑或歌单的收藏或者取消收藏
    public void requestChangeSubscribeListStatus(TypeEnum type, boolean isCollected, long id) {
        Single<CommonMessageBean> changeObservable = null;
        if (type.getValue() == TypeEnum.PLAYLIST_ID) {
            //收藏或取消收藏歌单
            changeObservable = ApiEngine.getInstance().getApiService().subscribePlayList(id, !isCollected ? 1 : 2);
        } else {
            //收藏或取消收藏专辑
            changeObservable = ApiEngine.getInstance().getApiService().subscribeAlbum(id, !isCollected ? 1 : 2);
        }
        changeObservable.compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(new SimpleObserver<CommonMessageBean>() {
                    @Override
                    public void onSuccess(@NonNull @NotNull CommonMessageBean commonMessageBean) {
                        //操作成功
                        mChangeSubStatusLiveData.setValue(commonMessageBean.getCode() == 200);
                    }

                    @Override
                    protected void onFailed(ExceptionHandle.ResponseThrowable errorMsg) {
                        mChangeSubStatusLiveData.setValue(false);
                    }
                });
    }
}
