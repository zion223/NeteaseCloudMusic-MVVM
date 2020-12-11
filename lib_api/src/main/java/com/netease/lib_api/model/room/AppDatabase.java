package com.netease.lib_api.model.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.netease.lib_api.model.song.AudioBean;
import com.netease.lib_api.model.song.LatestAudioBean;

@Database(entities = {AudioBean.class, LatestAudioBean.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {


    public abstract LatestDataDao getLatestSongDao();

    public abstract MusicPlayListDao getMusicPlayListDao();

    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "cloud_music_data")
                    //允许在主线程访问数据库
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

}
