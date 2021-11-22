package com.netease.lib_audio.mediaplayer.core;

import android.util.Log;

import com.netease.lib_api.model.room.AppDatabase;
import com.netease.lib_api.model.room.LatestDataDao;
import com.netease.lib_api.model.song.AudioBean;
import com.netease.lib_api.model.song.LatestAudioBean;
import com.netease.lib_audio.app.AudioHelper;
import com.netease.lib_audio.mediaplayer.events.AudioCompleteEvent;
import com.netease.lib_audio.mediaplayer.events.AudioErrorEvent;
import com.netease.lib_audio.mediaplayer.events.AudioPlayModeEvent;
import com.netease.lib_audio.mediaplayer.events.AudioRemoveEvent;
import com.netease.lib_audio.mediaplayer.exception.AudioQueueEmptyException;
import com.netease.lib_common_ui.utils.SharePreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Random;

//不允许在模块外的类访问
public class AudioController {

    private static final String TAG = "AudioController";

    //播放器
    private final AudioPlayer mAudioPlayer;
    //当前播放队列
    private ArrayList<AudioBean> mQueue = new ArrayList<>();
    //播放模式
    private PlayMode mPlayMode;
    //当前播放索引
    private int mQueueIndex;
    // Room数据库
    private AppDatabase appDatabase;
    // 本地SharePreference
    private SharePreferenceUtil preferenceUtil;

    private AudioController() {
        mAudioPlayer = new AudioPlayer();
        appDatabase = AppDatabase.getInstance(AudioHelper.getContext());
        preferenceUtil = SharePreferenceUtil.getInstance(AudioHelper.getContext());
        // 获取数据库保存的音乐播放队列
        mQueue.addAll(appDatabase.getMusicPlayListDao().getMusicPlayList());
        final AudioBean audio = preferenceUtil.getLatestSong();
        if (audio != null && mQueue.size() > 0 && mQueue.contains(audio)) {
            //当前是否有歌曲
            mQueueIndex = mQueue.indexOf(audio);
        } else {
            mQueueIndex = 0;
        }
        mPlayMode = PlayMode.getMode(preferenceUtil.getPlayMode());
        EventBus.getDefault().register(this);
    }

    public static AudioController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final AudioController INSTANCE = new AudioController();
    }

    /**
     * 对外提提供是否开始状态
     */
    public boolean isStartState() {
        return CustomMediaPlayer.Status.STARTED == getStatus();
    }

    /**
     * 对外提提供是否暂停状态
     */
    private boolean isPauseState() {
        return CustomMediaPlayer.Status.PAUSED == getStatus();
    }

    private CustomMediaPlayer.Status getStatus() {
        return mAudioPlayer.getState();
    }

    public ArrayList<AudioBean> getQueue() {
        return mQueue;
    }

    //添加Audio
    public void addAudio(AudioBean bean) {
        addAudio(0, bean);
    }

    //添加Audio集合
    public void addAudio(final ArrayList<AudioBean> list) {
        mQueue.clear();
        mQueue.addAll(list);
        //先清空数据
        new Thread() {
            @Override
            public void run() {
                appDatabase.getMusicPlayListDao().clearMusicPlayList();
                for (AudioBean bean : list) {
                    appDatabase.getMusicPlayListDao().insertMusicPlayList(bean);
                }
            }
        }.start();
    }

    public void addAudio(int index, final AudioBean bean) {

        int query = mQueue.indexOf(bean);
        if (query <= -1) {
            //当前播放列表中没有此歌曲
            mQueue.add(index, bean);
            // 插入单个数据
            setPlayIndex(index);
            new Thread() {
                @Override
                public void run() {
                    appDatabase.getMusicPlayListDao().insertMusicPlayList(bean);
                }
            }.start();
        } else {
            //当前播放列表中有这个歌曲
            AudioBean currentPlaying = getCurrentPlaying();
            if (!currentPlaying.getId().equals(bean.getId())) {
                setPlayIndex(query);
            }
        }
    }

    //删除单个Audio
    public void removeAudio(final AudioBean bean) {
        if (mQueue.size() <= 0) {
            throw new AudioQueueEmptyException("");
        }
        if (mQueue.remove(bean)) {
            EventBus.getDefault().post(new AudioRemoveEvent());
            // 删除单个数据
            new Thread() {
                @Override
                public void run() {
                    appDatabase.getMusicPlayListDao().deleteMusicFromPlayList(bean);
                }
            }.start();
        }
    }

    //删除全部
    public void removeAudio() {
        if (mQueue.size() <= 0) {
            throw new AudioQueueEmptyException("");
        }
        AudioBean currentBean = getCurrentPlaying();
        mQueue.clear();
        //不删除当前播放的歌曲
        mQueue.add(currentBean);
        //更新播放索引
        mQueueIndex = 0;
        // 删除全部数据
        new Thread() {
            @Override
            public void run() {
                appDatabase.getMusicPlayListDao().clearMusicPlayList();
            }
        }.start();
    }

    public void setQueue(ArrayList<AudioBean> bean) {
        setQueue(bean, 0);
    }

    public void setQueue(ArrayList<AudioBean> bean, int index) {
        mQueue.addAll(bean);
        mQueueIndex = index;
        // 插入集合数据
        //SharePreferenceUtil.getInstance(AudioHelper.getContext()).saveMusicList(mQueue);
    }

    public PlayMode getPlayMode() {
        return mPlayMode;
    }

    public void setPlayMode(PlayMode playMode) {
        mPlayMode = playMode;
        //还要对外发送切换事件，更新UI
        EventBus.getDefault().post(new AudioPlayModeEvent(mPlayMode));
        // 持久化播放模式
        preferenceUtil.savePlayMode(PlayMode.getIntValue(playMode));
        Log.e(TAG, "AudioPlayModeEvent " + mPlayMode.name());
    }

    public void setPlayIndex(int index) {
        if (mQueue.size() <= 0) {
            throw new AudioQueueEmptyException("当前播放队列为空,请先设置播放队列.");
        }
        mQueueIndex = index;
        play();
    }

    //播放歌曲
    public void play() {
        final AudioBean currentPlaying = getCurrentPlaying();
        mAudioPlayer.load(currentPlaying);
        //播放时存储最近一次播放的歌曲
        preferenceUtil.saveLatestSong(currentPlaying);
        //添加到最近播放歌曲中(不能再主线程中访问)
        new Thread() {
            @Override
            public void run() {
                LatestDataDao dao = appDatabase.getLatestSongDao();
                //当前存储中没有该歌曲 则添加该歌曲
                if (dao.getAudioBeanById(currentPlaying.getId()) == null) {
                    dao.insertRecentSong(LatestAudioBean.convertAudioToLatestBean(currentPlaying));
                } else {
                    //更新在存储中的顺序
                }
            }
        }.start();
    }

    private AudioBean getCurrentPlaying() {
        return getPlaying(mQueueIndex);
    }

    public void pause() {
        mAudioPlayer.pause();
    }

    public void seekTo(long time) {
        mAudioPlayer.seekTo(time);
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mAudioPlayer != null) {
            mAudioPlayer.release();
        }
        EventBus.getDefault().unregister(this);
    }

    public void resume() {
        mAudioPlayer.resume();
    }

    /**
     * 播放下一首歌曲
     */
    public void next() {
        if (mQueue != null && mQueue.size() > 0) {
            mAudioPlayer.load(getNextPlaying());
        }
    }

    //获取下一首歌曲
    private AudioBean getNextPlaying() {
        switch (mPlayMode) {
            case LOOP:
                //列表循环 取余运算防止越界
                mQueueIndex = (mQueueIndex + 1) % mQueue.size();
                return getPlaying(mQueueIndex);
            case RANDOM:
                //随机播放
                mQueueIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
                return getPlaying(mQueueIndex);
            case REPEAT:
                //单曲循环
                return getPlaying(mQueueIndex);
            default:
                break;
        }
        throw new NullPointerException("cannot find Audio");
    }

    /**
     * 播放上一首歌曲
     */
    public void previous() {
        mAudioPlayer.load(getPrePlaying());
    }

    //获取上一首歌曲
    private AudioBean getPrePlaying() {
        switch (mPlayMode) {
            case LOOP:
                mQueueIndex = (mQueueIndex + mQueue.size() - 1) % mQueue.size();
                return getPlaying(mQueueIndex);
            case RANDOM:
                mQueueIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
                return getPlaying(mQueueIndex);
            case REPEAT:
                return getPlaying(mQueueIndex);
            default:
                break;
        }
        throw new NullPointerException("cannot find Audio");
    }

    public int getQueueIndex() {
        return mQueueIndex;
    }

    /**
     * 切换播放/暂停状态
     */
    public void playOrPause() {
        if (CustomMediaPlayer.Status.IDLE == getStatus()) {
            play();
        }
        if (isStartState()) {
            pause();
        } else if (isPauseState()) {
            resume();
        }
    }

    private AudioBean getPlaying(int index) {
        if (mQueue.size() > 0 && index >= 0 && index < mQueue.size()) {
            return mQueue.get(index);
        } else {
            return null;
            //throw new AudioQueueEmptyException("当前播放队列为空,请先设置播放队列." + "当前Index: "+ index + "播放队列大小: " + mQueue.size());
        }
    }

    public AudioBean getNowPlaying() {
        return getPlaying(mQueueIndex);
    }

    //插放完毕事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioCompleteEvent(AudioCompleteEvent event) {
        Log.e(TAG, "onAudioCompleteEvent");
        next();
    }

    //播放出错事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioErrorEvent(AudioErrorEvent event) {
        next();
    }

}
