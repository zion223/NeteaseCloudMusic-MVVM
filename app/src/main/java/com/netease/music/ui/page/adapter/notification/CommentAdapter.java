package com.netease.music.ui.page.adapter.notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.lib_api.model.notification.PrivateCommentBean;
import com.netease.lib_image_loader.app.ImageLoaderManager;
import com.netease.music.R;
import com.netease.music.util.TimeUtil;

import java.util.List;

public class CommentAdapter extends BaseQuickAdapter<PrivateCommentBean.Comment, BaseViewHolder> {

    public CommentAdapter(@Nullable List<PrivateCommentBean.Comment> data) {
        super(R.layout.item_notification_comment, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder adapter, PrivateCommentBean.Comment comment) {
        //回复你的用户头像
        ImageLoaderManager.getInstance().displayImageForCircle(adapter.getView(R.id.iv_item_notification_comment_avatar_img), comment.getUser().getAvatarUrl());
        //用户昵称
        adapter.setText(R.id.tv_item_notification_comment_avatar_name, comment.getUser().getNickname());
        //回复时间
        adapter.setText(R.id.tv_item_notification_comment_time, TimeUtil.getPrivateMsgTime(comment.getTime()));
        //回复内容
        adapter.setText(R.id.tv_item_notification_content, "回复我: " + comment.getContent());
        //被回复的内容
        adapter.setText(R.id.tv_item_notification_comment_replied, comment.getBeRepliedContent());
    }
}
