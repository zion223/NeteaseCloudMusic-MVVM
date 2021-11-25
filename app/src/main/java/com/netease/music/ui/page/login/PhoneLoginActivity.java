package com.netease.music.ui.page.login;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.utils.Utils;
import com.netease.lib_common_ui.utils.SharePreferenceUtil;
import com.netease.lib_common_ui.utils.ValidateUtils;
import com.netease.lib_common_ui.widget.CaptchaView;
import com.netease.music.BR;
import com.netease.music.MainActivity;
import com.netease.music.R;
import com.netease.music.ui.state.PhoneLoginViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * 手机登录页面
 *
 * @author Administrator
 */
public class PhoneLoginActivity extends BaseActivity {


    Observable<Long> timer = Observable.interval(0, 1, TimeUnit.SECONDS)
            .take(60 + 1)
            .map(takeValue -> 60 - takeValue)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    private PhoneLoginViewModel mPhoneLoginViewModel;
    //验证码输入完成后的回调
    public CaptchaView.OnInputListener listener = new CaptchaView.OnInputListener() {
        @Override
        public void onSucess(String code) {
            //注册(更改密码)
            mPhoneLoginViewModel.loadingVisible.set(true);
            mPhoneLoginViewModel.accountRequest.register(mPhoneLoginViewModel.phone.get(), mPhoneLoginViewModel.password.get(), code);
        }
    };
    private final TextWatcher phoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mPhoneLoginViewModel.prePhoneColor.set(TextUtils.isEmpty(s.toString()) ? R.color.gray : R.color.black);
        }
    };

    @Override
    protected void initViewModel() {
        mPhoneLoginViewModel = getActivityScopeViewModel(PhoneLoginViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_phone_login, BR.vm, mPhoneLoginViewModel)
                .addBindingParam(BR.proxy, new ClickProxy())
                .addBindingParam(BR.captchaViewInputListener, listener)
                .addBindingParam(BR.phoneTextWatcher, phoneTextWatcher);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO tip：让 accountRequest 可观察页面生命周期，
        // 从而在页面即将退出、且登录请求由于网络延迟尚未完成时，
        // 及时通知数据层取消本次请求，以避免资源浪费和一系列不可预期的问题。
        getLifecycle().addObserver(mPhoneLoginViewModel.accountRequest);


        //观察登录或注册状态  成功后跳转到主界面
        mPhoneLoginViewModel.accountRequest.getLoginLiveData().observe(this, loginBean -> {
            mPhoneLoginViewModel.loadingVisible.set(false);
            if (loginBean.getResponseStatus().isSuccess()) {
                //登陆成功
                SharePreferenceUtil.getInstance(Utils.getApp()).saveUserInfo(loginBean.getResult(), mPhoneLoginViewModel.phone.get());
                startActivity(new Intent(PhoneLoginActivity.this, MainActivity.class));
                finish();
            } else {
                //登陆失败
                showLongToast(loginBean.getResponseStatus().getResponseCode() + loginBean.getResult().getMsg());
                mPhoneLoginViewModel.password.set("");
            }
        });
        //观察验证码发送状态  成功后显示验证码输入界面 并且倒计时启动
        mPhoneLoginViewModel.accountRequest.getCaptureLiveData().observe(this, message -> {
            if (message.getResponseStatus().isSuccess()) {
                showShortToast(R.string.capture_code_success);
                //验证码发送成功
                mPhoneLoginViewModel.showCaptureView.set(true);
                mPhoneLoginViewModel.showInputPasswordView.set(false);
                //显示发送到的手机号 150****7777
                //显示倒计时1min
                //interval 实现倒计时  订阅倒计时接口
                timer.subscribe(new ResourceObserver<Long>() {
                    @Override
                    public void onNext(Long value) {
                        mPhoneLoginViewModel.countDownText.set(value.toString() + "秒");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mPhoneLoginViewModel.countDownText.set("重新获取");
                        mPhoneLoginViewModel.enableCaptureButton.set(true);
                    }
                });
                //无法点击倒计时按钮
                mPhoneLoginViewModel.enableCaptureButton.set(false);

            } else {
                //验证码发送失败 给予失败信息提示
                showLongToast(message.getResponseStatus().getResponseCode());
            }
        });
    }

    public class ClickProxy {
        //返回按钮
        public void back() {
            //判断当前状态   输入手机号状态  输入密码状态  忘记密码状态 输入验证码状态
            //PhoneLoginActivity.this.finish();
            if (mPhoneLoginViewModel.showInputPhoneView.get()) {
                //在输入手机号界面 直接结束Activity
                finish();
            } else if (mPhoneLoginViewModel.showForgetPasswordView.get() && mPhoneLoginViewModel.showInputPasswordView.get()) {
                //当前在忘记密码界面 返回时显示输入密码界面
                mPhoneLoginViewModel.showForgetPasswordView.set(false);
                mPhoneLoginViewModel.title.set("手机号登录");
                mPhoneLoginViewModel.passwordHint.set("请输入密码");
            } else if (mPhoneLoginViewModel.showInputPasswordView.get() && !mPhoneLoginViewModel.showForgetPasswordView.get()) {
                //当前在输入密码界面 返回时显示输入手机号界面
                mPhoneLoginViewModel.showInputPasswordView.set(false);
                mPhoneLoginViewModel.showInputPhoneView.set(true);
            } else {
                // 在输入验证码界面 返回时显示 设置登录密码界面
                mPhoneLoginViewModel.showInputPasswordView.set(true);
                mPhoneLoginViewModel.showForgetPasswordView.set(true);
                mPhoneLoginViewModel.showCaptureView.set(false);
            }
        }

        //下一步
        public void next() {
            //校验手机号格式
            if (ValidateUtils.isMobile(mPhoneLoginViewModel.phone.get())) {
                mPhoneLoginViewModel.showInputPhoneView.set(false);
                mPhoneLoginViewModel.showInputPasswordView.set(true);
                mPhoneLoginViewModel.passwordHint.set("请输入密码");
                //密码框获取到焦点
            } else {
                //手机号格式不正确
                showShortToast(R.string.input_correct_phone_number);
            }
        }

        //登录
        public void login() {
            //判断是要发送验证码  还是直接登录
            if (mPhoneLoginViewModel.showForgetPasswordView.get()) {
                //校验新输入的密码格式
                if (mPhoneLoginViewModel.password.get().length() > 2 && mPhoneLoginViewModel.password.get().length() < 20
                        && ValidateUtils.isPassword(mPhoneLoginViewModel.password.get())) {
                    //发送验证码 显示倒计时 显示验证码输入框
                    mPhoneLoginViewModel.accountRequest.sendCapture(mPhoneLoginViewModel.phone.get());
                }

            } else {
                //登录请求
                mPhoneLoginViewModel.loadingVisible.set(true);
                mPhoneLoginViewModel.accountRequest.requestLogin(mPhoneLoginViewModel.phone.get(), mPhoneLoginViewModel.password.get());
            }
        }

        //忘记密码
        public void forgetPassword() {
            mPhoneLoginViewModel.showForgetPasswordView.set(true);
            mPhoneLoginViewModel.title.set("忘记密码");
            mPhoneLoginViewModel.passwordHint.set("请设置登录密码,8-20位字符,至少字母/数字/符号两种组合");
            mPhoneLoginViewModel.password.set("");
        }

        //重新获取验证码  只有在倒计时结束 显示重新获取 后才可以点击此按钮
        public void reObtainCpatureCode() {
            mPhoneLoginViewModel.accountRequest.sendCapture(mPhoneLoginViewModel.phone.get());
        }
    }


}
