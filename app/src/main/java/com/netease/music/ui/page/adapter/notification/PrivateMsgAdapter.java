package com.netease.music.ui.page.adapter.notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.lib_api.model.notification.MsgDetailBean;
import com.netease.lib_api.model.notification.PrivateMsgBean;
import com.netease.lib_common_ui.utils.GsonUtil;
import com.netease.lib_image_loader.app.ImageLoaderManager;
import com.netease.music.R;
import com.netease.music.util.SearchUtil;
import com.netease.music.util.TimeUtil;

import java.util.List;

public class PrivateMsgAdapter extends BaseQuickAdapter<PrivateMsgBean.Msg, BaseViewHolder> {

    public PrivateMsgAdapter(@Nullable List<PrivateMsgBean.Msg> data) {
        super(R.layout.item_notification_msg, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder adapter, PrivateMsgBean.Msg item) {
        //发私信的人 昵称
        adapter.setText(R.id.tv_item_notification_fromuser, item.getFromUser().getNickname());
        //发送时间
        adapter.setText(R.id.tv_item_notification_time, TimeUtil.getPrivateMsgTime(item.getLastMsgTime()));
        ImageLoaderManager.getInstance().displayImageForCircle(adapter.getView(R.id.iv_item_notification_fromuser_avatar), item.getFromUser().getAvatarUrl());
        //最后一条消息
        String lastMsg = item.getLastMsg();
        MsgDetailBean msgDetailBean = GsonUtil.fromJSON(lastMsg, MsgDetailBean.class);
        adapter.setText(R.id.iv_item_notification_message, msgDetailBean.getMsg());
        //用户身份
        SearchUtil.setUserTypeImg(item.getFromUser().getUserType(), R.id.iv_item_notification_user_tag, adapter);

        if (item.getNewMsgCount() != 0) {
            adapter.setVisible(R.id.tv_item_notification_count, true);
            adapter.setText(R.id.tv_item_notification_count, String.valueOf(item.getNewMsgCount()));
        }
    }
}
