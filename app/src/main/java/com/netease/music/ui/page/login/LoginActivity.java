package com.netease.music.ui.page.login;

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
        mLoginViewModel = getActivityScopeViewModel(LoginViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        return new DataBindingConfig(R.layout.fragment_login, BR.vm, mLoginViewModel)
                .addBindingParam(BR.click, new ClickProxy());
    }

    public class ClickProxy {

        public void phoneLogin() {
            if (mLoginViewModel.argeeRule.get()) {
                startActivity(new Intent(LoginActivity.this, PhoneLoginActivity.class));
                finish();
            } else {
                //提示用户点击同意条款
                showShortToast("请点击同意《用户协议》和《隐私政策》《儿童隐私政策》");
            }
        }

        //TODO 体验
        public void experience() {
            showShortToast("此功能暂时无实现");
        }

    }

    //返回按钮直接结束Activity
    @Override
    public void onBackPressed() {
        finish();
    }
}
