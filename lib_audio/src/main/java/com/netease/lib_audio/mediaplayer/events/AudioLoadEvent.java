package com.netease.lib_audio.mediaplayer.events;

import com.netease.lib_api.model.song.AudioBean;

public class AudioLoadEvent {
    public AudioBean mAudioBean;

    public AudioLoadEvent(AudioBean audioBean) {
        this.mAudioBean = audioBean;
    }
}
