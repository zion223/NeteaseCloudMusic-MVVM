package com.netease.music.ui.state;

import androidx.databinding.ObservableField;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.netease.lib_api.model.notification.PrivateCommentBean;
import com.netease.music.domain.request.CommentRequest;
import com.netease.music.domain.request.NotificationRequest;
import com.netease.music.ui.state.load.BaseLoadingViewModel;

import java.util.List;

public class NotificationViewModel extends BaseLoadingViewModel {

    public final ObservableField<List<Object>> list = new ObservableField<>();

    public final NotificationRequest request = new NotificationRequest();


}
