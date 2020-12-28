package com.netease.lib_common_ui.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


import com.netease.lib_api.model.song.AudioBean;
import com.netease.lib_api.model.user.LoginBean;

import java.util.Locale;

import static com.netease.lib_common_ui.utils.Constants.SpKey.PLAY_MODE;


/**
 * 本地SharePreference工具
 */
public class SharePreferenceUtil {

	private static final String TAG = "SharePreferenceUtil";

	private static SharedPreferences sp;
	private static SharedPreferences.Editor editor;
	private static SharePreferenceUtil mInstance;
	private Locale systemCurrentLocal = Locale.CHINESE;

	private SharePreferenceUtil() {
	}

	@SuppressLint("CommitPrefEdits")
	private static void init(Context context) {
		if (sp == null) {
			sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		}
		editor = sp.edit();
	}

	public static SharePreferenceUtil getInstance(Context context) {
		if (mInstance == null) {
			synchronized (SharePreferenceUtil.class) {
				if (mInstance == null) {
					init(context);
					mInstance = new SharePreferenceUtil();
				}
			}
		}
		return mInstance;
	}

	int getSelectLanguage() {
		return getInt(Constants.SpKey.TAG_LANGUAGE, 0);
	}

	Locale getSystemCurrentLocal() {
		return systemCurrentLocal;
	}

	/**
	 * 获取保存的本地音乐数量
	 */
	public int getLocalMusicCount(int defaultvalue) {
		return getInt(Constants.SpKey.LOCAL_MUSIC_COUNT, defaultvalue);
	}

	/**
	 * 本地音乐数量
	 */
	public void saveLocalMusicCount(int count) {
		saveInt(Constants.SpKey.LOCAL_MUSIC_COUNT, count);
	}

	/**
	 * 保存用户的信息以及电话号码（因为bean里的电话号码要处理字符串，所以这里直接暴力传比较高效）
	 */
	public void saveUserInfo(LoginBean bean, String phoneNumber) {
		if (bean.getBindings() != null && bean.getBindings().size() > 1) {
			saveAuthToken(bean.getBindings().get(1).getTokenJsonStr());
		}
		saveUserId(String.valueOf(bean.getProfile().getUserId()));
		saveAccountNum(phoneNumber);
		saveString(Constants.SpKey.USER_INFO, GsonUtil.toJson(bean));
	}

	/**
	 * 获取当前登录用户信息
	 */
	public String getUserInfo(String defaultValue) {
		return getString(Constants.SpKey.USER_INFO, defaultValue);
	}

	/**
	 * 退出登录 移除已登录用户信息
	 */
	public void removeUserInfo() {
		remove(Constants.SpKey.USER_INFO);
		saveAuthToken("");
	}

	/**
	 * 获取当前登录用户ID
	 */
	public int getUserId() {
		String userInfo = getString(Constants.SpKey.USER_INFO, "");
		if (TextUtils.isEmpty(userInfo)) {
			return 0;
		}
		LoginBean loginBean = GsonUtil.fromJSON(userInfo, LoginBean.class);
		return loginBean.getProfile().getUserId();
	}


	/**
	 * 保存当前用户userId
	 */
	private void saveUserId(String id) {
		saveString(Constants.SpKey.USER_ID, id);
	}


	/**
	 * 保存账号数量
	 */
	private void saveAccountNum(String phoneNumber) {
		saveString(Constants.SpKey.PHONE_NUMBER, phoneNumber);
	}

	/**
	 * 获取账号数量
	 */
	public String getAccountNum() {
		return getString(Constants.SpKey.PHONE_NUMBER, "");
	}

	/**
	 * 保存token
	 */
	private void saveAuthToken(String token) {
		saveString(Constants.SpKey.AUTH_TOKEN, token);
	}

	/**
	 * 获取AuthToken
	 */
	public String getAuthToken(String defaultValue) {
		return getString(Constants.SpKey.AUTH_TOKEN, defaultValue);
	}


    /**
     * 存储最近一次听过的歌曲
     */
    public void saveLatestSong(AudioBean songInfo) {
		saveString(Constants.SpKey.LATEST_SONG, GsonUtil.toJson(songInfo));
    }

	/**
	 * 获取最近一次听过的歌曲
	 */
	public AudioBean getLatestSong() {
		return GsonUtil.fromJSON(getString(Constants.SpKey.LATEST_SONG, ""), AudioBean.class);
	}

	/**
	 * 保存上次获取日推的时间
	 */
	public void saveDailyUpdateTime(String updateTime) {
		saveString(Constants.SpKey.DAILY_UPDATE_TIME, updateTime);
	}

	/**
	 * 获取上次获取日推的时间
	 */
	public String getDailyUpdateTime() {
		return getString(Constants.SpKey.DAILY_UPDATE_TIME, "");
	}

	/**
	 * 存储当前歌手ID
	 */
	public void saveCurrentArtistId(String id) {
		saveString(Constants.SpKey.CURRENT_ARTIST_ID, id);
	}

	/**
	 * 移除存储的歌手ID
	 */
	public void removeCurrentArtistId() {
		remove(Constants.SpKey.CURRENT_ARTIST_ID);
	}

	/**
	 * 获取当前歌手ID
	 */
	public String getCurrentArtistId() {
		return getString(Constants.SpKey.CURRENT_ARTIST_ID, "");
	}


	/**
	 * 获取定时停止播放时间
	 */
	public int getStopAudioTime() {
		return getInt(Constants.SpKey.STOP_AUDIO_TIME, 0);
	}

	/**
	 * 设置定时停止播放时间
	 */
	public void setStopAudioTime(int time) {
		saveInt(Constants.SpKey.STOP_AUDIO_TIME, time);
	}

	/**
	 * 保存当前的播放模式
	 */
	public void savePlayMode(int mode) {
		saveInt(PLAY_MODE, mode);
	}

	/**
	 * 获取当前播放模式
	 */
	public int getPlayMode() {
		return getInt(PLAY_MODE, 0);
	}

	private void remove(String key) {
		editor.remove(key).apply();
	}

	private String getString(String key, String defaultValue) {
		return sp.getString(key, defaultValue);
	}

	private void saveLong(String key, long values) {
		editor.putLong(key, values).apply();
	}

	private void saveInt(String key, int values) {
		editor.putInt(key, values).apply();
	}

	private long getLong(String key, long defaultValue) {
		return sp.getLong(key, defaultValue);
	}

	private int getInt(String key, int defaultValue) {
		return sp.getInt(key, defaultValue);
	}

	private void saveString(String key, String value) {
		editor.putString(key, value).apply();
	}
}
