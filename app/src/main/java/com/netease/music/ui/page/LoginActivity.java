package com.netease.music.ui.page;

import android.content.Intent;

import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.state.LoginViewModel;

public class LoginActivity extends BaseActivity {

    private LoginViewModel mLoginViewModel;

    @Override
    protected void initViewModel() {
        mLoginViewModel = getActivityViewModel(LoginViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        return new DataBindingConfig(R.layout.fragment_login, BR.vm, mLoginViewModel)
                .addBindingParam(BR.click, new ClickProxy());
    }

    public class ClickProxy {

        public void phone() {
            startActivity(new Intent(LoginActivity.this, PhoneLoginActivity.class));
        }

        //TODO 体验
        public void experience() {

        }

    }

    //返回按钮直接结束Activity
    @Override
    public void onBackPressed() {
        finish();
    }
}
