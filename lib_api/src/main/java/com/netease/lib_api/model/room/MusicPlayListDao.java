package com.netease.lib_api.model.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.netease.lib_api.model.song.AudioBean;

import java.util.List;

@Dao
public interface MusicPlayListDao {
    //音乐播放列表

    //获取最近播放的歌曲
    @Query("SELECT * FROM AudioBean")
    List<AudioBean> getMusicPlayList();

    //插入音乐播放列表的歌曲
    @Insert
    void insertMusicPlayList(AudioBean... song);

    @Query("DELETE FROM AudioBean")
    void clearMusicPlayList();

    @Delete()
    void deleteMusicFromPlayList(AudioBean music);

}
