package com.netease.lib_audio.mediaplayer.core;

public enum PlayMode {
    LOOP,
    RANDOM,
    REPEAT;

    public static int getIntValue(PlayMode mode) {
        switch (mode) {
            case LOOP:
                return 1;
            case RANDOM:
                return 2;
            case REPEAT:
                return 3;
            default:
                return 1;
        }
    }

    public static PlayMode getMode(int mode) {
        switch (mode) {
            case 1:
                return LOOP;
            case 2:
                return RANDOM;
            case 3:
                return REPEAT;
            default:
                return LOOP;
        }
    }
}
