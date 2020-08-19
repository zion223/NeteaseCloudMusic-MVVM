package com.netease.lib_audio.mediaplayer.events;

import com.netease.lib_audio.mediaplayer.core.AudioController;

/**
 * 播放模式切换事件
 */
public class AudioPlayModeEvent {

    public AudioController.PlayMode mPlayMode;

    public AudioPlayModeEvent(AudioController.PlayMode playMode) {
        this.mPlayMode = playMode;
    }
}
