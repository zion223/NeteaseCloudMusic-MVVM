package com.netease.lib_api.model.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.netease.lib_api.model.song.AudioBean;
import com.netease.lib_api.model.song.DailyRecommendSongsBean;
import com.netease.lib_api.model.song.LatestAudioBean;
import com.netease.lib_api.model.song.SongDetailBean;

@Database(entities = {AudioBean.class, LatestAudioBean.class, DailyRecommendSongsBean.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {


    public abstract LatestDataDao getLatestSongDao();

    public abstract MusicPlayListDao getMusicPlayListDao();

    public abstract RecommendMusicDao getDailyRecommendDao();

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
