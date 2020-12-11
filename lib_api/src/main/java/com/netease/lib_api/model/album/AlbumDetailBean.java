package com.netease.lib_api.model.album;


import com.netease.lib_api.model.search.AlbumSearchBean;
import com.netease.lib_api.model.song.DailyRecommendSongsBean;
import com.netease.lib_api.model.song.SongDetailBean;

import java.util.ArrayList;

public class AlbumDetailBean {

    private int code;
    private boolean onSale;
    private boolean isSub;
    private long subTime;
    private Object albumGameInfo;
    private int commentCount;
    private int likedCount;
    private int shareCount;
    private int subCount;
    private AlbumSearchBean.ResultBean.AlbumsBean album;
    private ArrayList<DailyRecommendSongsBean> songs;

    public boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }

    public boolean isSub() {
        return isSub;
    }

    public void setSub(boolean sub) {
        isSub = sub;
    }

    public long getSubTime() {
        return subTime;
    }

    public void setSubTime(long subTime) {
        this.subTime = subTime;
    }

    public Object getAlbumGameInfo() {
        return albumGameInfo;
    }

    public void setAlbumGameInfo(Object albumGameInfo) {
        this.albumGameInfo = albumGameInfo;
    }

    public String getCommentCount() {
        return String.valueOf(commentCount);
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(int likedCount) {
        this.likedCount = likedCount;
    }

    public String getShareCount() {
        return String.valueOf(shareCount);
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public String getSubCount() {
        return String.valueOf(subCount);
    }

    public void setSubCount(int subCount) {
        this.subCount = subCount;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public AlbumSearchBean.ResultBean.AlbumsBean getAlbum() {
        return album;
    }

    public void setAlbum(AlbumSearchBean.ResultBean.AlbumsBean album) {
        this.album = album;
    }

    public ArrayList<DailyRecommendSongsBean> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<DailyRecommendSongsBean> songs) {
        this.songs = songs;
    }
}
