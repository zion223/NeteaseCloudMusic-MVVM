package com.netease.music.ui.page.drawer.notification;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.lib_common_ui.utils.SharePreferenceUtil;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.page.adapter.notification.CommentAdapter;
import com.netease.music.ui.page.adapter.notification.ForwardsMeAdapter;
import com.netease.music.ui.page.adapter.notification.PrivateMsgAdapter;
import com.netease.music.ui.page.adapter.notification.PrivateNoticeAdapter;
import com.netease.music.ui.state.NotificationViewModel;


// 通知相关 评论 私信 @我 通知
public class NotificationFragment extends BaseFragment {

    private NotificationViewModel mState;

    private static final String NOTIFICATION_TYPE = "notificationType";

    public static NotificationFragment newInstance(int type) {
        Bundle argz = new Bundle();
        argz.putInt(NOTIFICATION_TYPE, type);
        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(argz);
        return fragment;
    }

    @Override
    protected void initViewModel() {
        mState = getFragmentScopeViewModel(NotificationViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_recyclerview_notification, BR.vm, mState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final NotificationType type;
        if (getArguments() != null && NotificationType.getType(getArguments().getInt(NOTIFICATION_TYPE)) != null) {
            type = NotificationType.getType(getArguments().getInt(NOTIFICATION_TYPE));
            switch (type) {
                case COMMENT:
                    mState.request.getPrivateCommentLiveData().observe(getViewLifecycleOwner(), comments -> {
                        mState.loadingVisible.set(false);
                        mState.adapter.set(new CommentAdapter(comments));
                    });
                    //请求
                    mState.request.requestPirvateCommnet(SharePreferenceUtil.getInstance(getContext()).getUserId());
                    break;
                case NOTICE:
                    mState.request.getNoticeLiveData().observe(getViewLifecycleOwner(), data -> {
                        mState.loadingVisible.set(false);
                        mState.adapter.set(new PrivateNoticeAdapter(data));
                    });
                    //请求
                    mState.request.requestNotice();
                    break;
                case FORWARDS:
                    mState.request.getForwardsMeLiveData().observe(getViewLifecycleOwner(), data -> {
                        mState.loadingVisible.set(false);
                        mState.adapter.set(new ForwardsMeAdapter(data));
                    });
                    //请求
                    mState.request.requestForwardsMe();
                    break;
                case PRIVATE_LETTER:
                    mState.request.getPrivateLetterLiveData().observe(getViewLifecycleOwner(), data -> {
                        mState.loadingVisible.set(false);
                        mState.adapter.set(new PrivateMsgAdapter(data));
                    });
                    //请求
                    mState.request.requestPirvateLetter();
                    break;
                default:
                    break;
            }

        }
    }


}
