package com.netease.lib_audio.app;

import android.content.Context;

import com.netease.lib_api.model.song.AudioBean;
import com.netease.lib_audio.mediaplayer.core.AudioController;

import java.util.ArrayList;

//唯一与外界通信的类
public final class AudioHelper {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        return mContext;
    }

    //加入播放队列
    public static void addAudio(AudioBean bean) {
        AudioController.getInstance().addAudio(bean);
    }

    //加入List<AudioBean>的音乐
    public static void addAudio(ArrayList<AudioBean> mLists) {
        AudioController.getInstance().addAudio(mLists);
    }

}
