package com.netease.lib_api.model.room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.netease.lib_api.model.song.DailyRecommendSongsBean;

import java.util.List;

public class Converters {

    @TypeConverter
    public static DailyRecommendSongsBean.AlBean fromAlBean(String value) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(value, DailyRecommendSongsBean.AlBean.class);
    }

    @TypeConverter
    public static String albeanToString(DailyRecommendSongsBean.AlBean albean) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        return gson.toJson(albean);
    }

    @TypeConverter
    public static List<DailyRecommendSongsBean.ArBean> fromAlBean1(String value) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(value, new TypeToken<List<DailyRecommendSongsBean.ArBean>>() {
        }.getType());
    }

    @TypeConverter
    public static String albean1ToString(List<DailyRecommendSongsBean.ArBean> albean) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        return gson.toJson(albean);
    }

}
