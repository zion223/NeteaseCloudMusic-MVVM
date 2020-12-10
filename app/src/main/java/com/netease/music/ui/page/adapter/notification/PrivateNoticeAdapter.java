package com.netease.music.ui.page.adapter.notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.lib_api.model.notification.PrivateNoticeBean;
import com.netease.lib_api.model.notification.UserNoticeJsonBean;
import com.netease.lib_common_ui.utils.GsonUtil;
import com.netease.lib_image_loader.app.ImageLoaderManager;
import com.netease.music.R;
import com.netease.music.util.SearchUtil;
import com.netease.music.util.TimeUtil;

import java.util.List;

public class PrivateNoticeAdapter extends BaseQuickAdapter<PrivateNoticeBean.Notice, BaseViewHolder> {

    public PrivateNoticeAdapter(@Nullable List<PrivateNoticeBean.Notice> data) {
        super(R.layout.item_notification_notice, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder adapter, PrivateNoticeBean.Notice item) {
        //解析JSON数据  TODO 适配更多通知类型

        UserNoticeJsonBean userNoticeJsonBean = GsonUtil.fromJSON(item.getNotice(), UserNoticeJsonBean.class);
        if (userNoticeJsonBean != null && userNoticeJsonBean.getComment() != null) {
            //评论的内容
            adapter.setText(R.id.iv_item_notice_message, userNoticeJsonBean.getComment().getContent());
            //点赞评论
            adapter.setText(R.id.tv_item_notice_event, "赞了你的评论");
        } else if (userNoticeJsonBean.getPlaylist() != null) {
            //歌单的通知
            adapter.setText(R.id.iv_item_notice_message, userNoticeJsonBean.getPlaylist().getName());
            adapter.setText(R.id.tv_item_notice_event, "收藏了你的歌单");
        } else if (userNoticeJsonBean.getGeneralNotice() != null) {
            // 常规的通知
            adapter.setText(R.id.iv_item_notice_message, userNoticeJsonBean.getGeneralNotice().getContent());
            adapter.setText(R.id.tv_item_notice_event, userNoticeJsonBean.getGeneralNotice().getActionDesc());
        } else {
            adapter.setText(R.id.iv_item_notice_message, "不支持的内容");
            adapter.setText(R.id.tv_item_notice_event, "不支持的内容");
        }
        adapter.setText(R.id.tv_item_notice_fromuser, userNoticeJsonBean.getUser().getNickname());
        //时间
        adapter.setText(R.id.tv_item_notice_time, TimeUtil.getPrivateMsgTime(item.getTime()));

        ImageLoaderManager.getInstance().displayImageForCircle(adapter.getView(R.id.iv_item_notice_fromuser_avatar), userNoticeJsonBean.getUser().getAvatarUrl());
        //显示用户类型
        SearchUtil.setUserTypeImg(userNoticeJsonBean.getUser().getUserType(), R.id.iv_item_notice_user_tag, adapter);

    }
}