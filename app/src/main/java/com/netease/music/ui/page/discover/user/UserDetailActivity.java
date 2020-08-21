package com.netease.music.ui.page.discover.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.state.UserDetailViewModel;

public class UserDetailActivity extends BaseActivity {

    private UserDetailViewModel mUserDetailViewModel;

    public static void startActivity(Context context, long userId) {
        Bundle argz = new Bundle();
        argz.putLong("userId", userId);
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra("data", argz);
        context.startActivity(intent);
    }

    @Override
    protected void initViewModel() {
        mUserDetailViewModel = getActivityViewModel(UserDetailViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_user_detail, BR.vm, mUserDetailViewModel);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBundleExtra("data") != null) {
            long userId = getIntent().getBundleExtra("data").getLong("userId");
            mUserDetailViewModel.userId.set(userId);

            mUserDetailViewModel.request.getUserDeatailLiveData().observe(this, userDetailBean -> {
                mUserDetailViewModel.user.set(userDetailBean);
                mUserDetailViewModel.followed.set(userDetailBean.getProfile().isFollowed());
            });

            //获取用户详情
            mUserDetailViewModel.request.requestUserDetail(mUserDetailViewModel.userId.get());
        }
    }
}
