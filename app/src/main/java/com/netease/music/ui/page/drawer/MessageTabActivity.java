package com.netease.music.ui.page.drawer;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.ActivityNavigator;

import com.kunminx.architecture.ui.adapter.MultiFragmentPagerAdapter;
import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.page.drawer.notification.NotificationFragment;
import com.netease.music.ui.page.drawer.notification.NotificationType;
import com.netease.music.ui.state.MessageTabViewModel;

import java.util.ArrayList;

public class MessageTabActivity extends BaseActivity {

    private MessageTabViewModel mState;

    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void initViewModel() {
        mState = getActivityScopeViewModel(MessageTabViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_message_tab, BR.vm, mState)
                .addBindingParam(BR.proxy, new ClickProxy());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init fragment list
        mFragmentList.add(NotificationFragment.newInstance(NotificationType.PRIVATE_LETTER.type));
        mFragmentList.add(NotificationFragment.newInstance(NotificationType.COMMENT.type));
        mFragmentList.add(NotificationFragment.newInstance(NotificationType.FORWARDS.type));
        mFragmentList.add(NotificationFragment.newInstance(NotificationType.NOTICE.type));
        MultiFragmentPagerAdapter multiFragmentPagerAdapter = new MultiFragmentPagerAdapter(getSupportFragmentManager());
        multiFragmentPagerAdapter.init(mFragmentList);
        mState.adapter.set(multiFragmentPagerAdapter);
    }

    @Override
    public void finish() {
        super.finish();
        ActivityNavigator.applyPopAnimationsToPendingTransition(this);
    }

    public class ClickProxy {
        //返回
        public void back() {
            finish();
        }

        //清除未读
        public void clearMessage() {
            showShortToast(R.string.function_not_completed);
        }
    }
}
