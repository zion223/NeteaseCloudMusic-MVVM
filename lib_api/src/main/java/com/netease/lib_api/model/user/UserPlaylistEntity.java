package com.netease.lib_api.model.user;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class UserPlaylistEntity implements MultiItemEntity {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CONTENT = 1;
    public static final int TYPE_FOOTER = 2;


    //头布局数据
    private int playListSize = -1;
    private String headerText;
    //子布局
    private UserPlaylistBean.PlaylistBean playlistBean;
    private TYPE type;
    //尾布局
    private String footerText;

    public TYPE getType() {
        return type;
    }

    public UserPlaylistEntity(String text, int playListSize) {
        this.playListSize = playListSize;
        this.headerText = text;
    }

    public UserPlaylistEntity(TYPE type, UserPlaylistBean.PlaylistBean playlistBean) {
        this.playlistBean = playlistBean;
        this.type = type;
    }

    public UserPlaylistEntity(String footerText) {
        this.footerText = footerText;
    }

    public String getPlayListSize() {
        return "(" + playListSize + ")";
    }

    public void setPlayListSize(int playListSize) {
        this.playListSize = playListSize;
    }

    public UserPlaylistBean.PlaylistBean getPlaylistBean() {
        return playlistBean;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setPlaylistBean(UserPlaylistBean.PlaylistBean playlistBean) {
        this.playlistBean = playlistBean;
    }

    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }

    @Override
    public int getItemType() {
        if (playListSize != -1) {
            return TYPE_HEADER;
        } else if (playlistBean != null) {
            return TYPE_CONTENT;
        } else {
            return TYPE_FOOTER;
        }
    }

    public enum TYPE {
        TYPE_CREATE,
        TYPE_SUBSCRIBE
    }
}
