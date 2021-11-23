package com.netease.music.ui.page.drawer.notification;

public enum NotificationType {
    COMMENT(0x001),
    FORWARDS(0x002),
    NOTICE(0x003),
    PRIVATE_LETTER(0x004);
    public int type;

    NotificationType(int i) {
        this.type = i;
    }

    public static NotificationType getType(int type) {
        switch (type) {
            case 0x001:
                return COMMENT;
            case 0x002:
                return FORWARDS;
            case 0x003:
                return NOTICE;
            case 0x004:
                return PRIVATE_LETTER;
            default:
                break;
        }
        return null;
    }
}
