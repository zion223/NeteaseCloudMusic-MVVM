package com.netease.lib_api.model.playlist;

import com.chad.library.adapter.base.entity.JSectionEntity;
import com.netease.lib_api.model.song.MusicCommentBean;

public class PlayListCommentEntity extends JSectionEntity {

    private boolean isHeader = false;
    //评论数量
    private String count = "";
    private String header;
    private MusicCommentBean.CommentsBean comment;

    //头布局数据  头布局标题 评论数量
    public PlayListCommentEntity(String header, String count) {
        this.isHeader = true;
        this.header = header;
        this.count = count;
    }

    public PlayListCommentEntity(String header) {
        this.isHeader = true;
        this.header = header;
    }

    //非头布局数据
    public PlayListCommentEntity(MusicCommentBean.CommentsBean object) {
        this.comment = object;
    }

    public MusicCommentBean.CommentsBean getComment() {
        return comment;
    }

    public String getHeader() {
        return header;
    }

    /**
     * 重写此方法，返回 boolen 值，告知是否是header
     */
    @Override
    public boolean isHeader() {
        return isHeader;
    }

    public String getCount() {
        return count;
    }
}
