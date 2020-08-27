package com.netease.lib_api.model.user;

import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UserPlayListHeader extends BaseExpandNode {

    private String header;
    private int playNum;
    private List<BaseNode> bean;


    public UserPlayListHeader(String header, int playNum, List<BaseNode> bean) {
        //默认展开
        setExpanded(true);
        this.header = header;
        this.playNum = playNum;
        this.bean = bean;
    }

    public String getPlayNum() {
        return "(" + playNum + ")";
    }

    public void setPlayNum(int playNum) {
        this.playNum = playNum;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<BaseNode> getBean() {
        return bean;
    }

    public void setBean(List<BaseNode> bean) {
        this.bean = bean;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return bean;
    }

    public static class UserPlayListContext extends BaseExpandNode {

        private UserPlaylistBean.PlaylistBean playlist;

        public UserPlayListContext(UserPlaylistBean.PlaylistBean playlist) {
            this.playlist = playlist;
        }

        public UserPlaylistBean.PlaylistBean getPlaylist() {
            return playlist;
        }

        @Nullable
        @Override
        public List<BaseNode> getChildNode() {
            return null;
        }
    }
}
