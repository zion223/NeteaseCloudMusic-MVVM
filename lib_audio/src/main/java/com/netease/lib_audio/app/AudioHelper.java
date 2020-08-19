package com.netease.lib_audio.app;

import android.app.Activity;
import android.content.Context;

import com.netease.lib_audio.mediaplayer.core.AudioController;
import com.imooc.lib_api.model.song.AudioBean;

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


	//TODO AudioBean改造
	public static void addAudio(Activity activity, AudioBean bean) {
		AudioController.getInstance().addAudio(bean);
		if (activity != null) {
			//MusicPlayerActivity.start(activity);
		}
	}

	//不启动Activity
	public static void addAudio(AudioBean bean) {
		addAudio(null, bean);
	}

	//加入List的音乐
	public static void addAudio(ArrayList<AudioBean> mLists) {
		AudioController.getInstance().addAudio(mLists);
	}

	//获取当前播放的歌曲
	public static AudioBean getNowPlaying() {
		return AudioController.getInstance().getNowPlaying();
	}

	//获取当前播放队列的歌曲
	public static ArrayList<AudioBean> getCurrentQueue() {
		return AudioController.getInstance().getQueue();
	}

}
