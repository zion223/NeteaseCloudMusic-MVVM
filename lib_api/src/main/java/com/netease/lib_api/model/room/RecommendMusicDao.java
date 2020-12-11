package com.netease.lib_api.model.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.netease.lib_api.model.song.DailyRecommendSongsBean;

import java.util.List;

@Dao
public interface RecommendMusicDao {


    //获取日推列表
    @Query("SELECT * FROM DailyRecommendSongsBean")
    List<DailyRecommendSongsBean> getDailyRecommendMusic();

    //存储日推列表
    @Insert
    void insertRecentSong(DailyRecommendSongsBean... song);

    //清空最近播放的音乐存储
    @Query("DELETE FROM DailyRecommendSongsBean")
    void clearDailyRecommendSong();
}
