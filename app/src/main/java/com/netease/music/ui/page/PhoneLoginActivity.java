package com.netease.music.ui.page;


import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.state.PhoneLoginViewModel;

public class PhoneLoginActivity extends BaseActivity {

    private PhoneLoginViewModel mPhoneLoginViewModel;

    @Override
    protected void initViewModel() {
        mPhoneLoginViewModel = getActivityViewModel(PhoneLoginViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_phone_login, BR.vm, mPhoneLoginViewModel)
                .addBindingParam(BR.proxy, new ClickProxy());
    }


    public class ClickProxy {


    }
}
