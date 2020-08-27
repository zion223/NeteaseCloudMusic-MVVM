package com.netease.lib_api.model.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.netease.lib_api.model.song.AudioBean;

import java.util.List;

@Dao
public interface LatestDataDao {

    /**
     * 存储并且获取最近播放的歌曲、视频、专辑、歌单、电台
     */

    //获取最近播放的歌曲
    @Query("SELECT * FROM AudioBean")
    List<AudioBean> getRecentSong();

    //查询最近播放的歌曲的数量
    @Query("SELECT COUNT(*) FROM AudioBean")
    LiveData<Integer> getRecentSongSize();

    //插入最近播放的一首歌曲
    @Insert
    void insertRecentSong(AudioBean... song);

    @Query("SELECT * FROM AudioBean WHERE song_id = :id")
    AudioBean getAudioBeanById(String id);

    //清空最近播放的音乐存储
    @Query("DELETE FROM AudioBean")
    void deleteAllRecentSong();

}
