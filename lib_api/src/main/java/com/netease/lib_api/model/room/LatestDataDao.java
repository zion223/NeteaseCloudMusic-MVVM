package com.netease.lib_api.model.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.netease.lib_api.model.song.LatestAudioBean;

import java.util.List;

@Dao
public interface LatestDataDao {

    /**
     * 存储并且获取最近播放的歌曲、视频、专辑、歌单、电台
     */

    //获取最近播放的歌曲
    @Query("SELECT * FROM LatestAudioBean")
    List<LatestAudioBean> getRecentAduio();

    //查询最近播放的歌曲的数量
    @Query("SELECT COUNT(*) FROM LatestAudioBean")
    LiveData<Integer> getRecentSongSize();

    //插入最近播放的一首歌曲
    @Insert
    void insertRecentSong(LatestAudioBean... song);

    @Query("SELECT * FROM LatestAudioBean WHERE song_id = :id")
    LatestAudioBean getAudioBeanById(String id);

    //清空最近播放的音乐存储
    @Query("DELETE FROM LatestAudioBean")
    void deleteAllRecentSong();

}
