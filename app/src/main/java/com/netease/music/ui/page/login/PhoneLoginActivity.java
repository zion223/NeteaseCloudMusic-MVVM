package com.netease.music.ui.page.login;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.netease.lib_common_ui.utils.SharePreferenceUtil;
import com.netease.lib_common_ui.utils.ValidateUtils;
import com.netease.lib_common_ui.widget.CaptchaView;
import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.utils.Utils;
import com.netease.music.BR;
import com.netease.music.MainActivity;
import com.netease.music.R;
import com.netease.music.ui.state.PhoneLoginViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;


public class PhoneLoginActivity extends BaseActivity {


    private PhoneLoginViewModel mPhoneLoginViewModel;

    Observable<Long> timer = Observable.interval(0, 1, TimeUnit.SECONDS)
            .take(60 + 1)
            .map(takeValue -> 60 - takeValue)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());


    @Override
    protected void initViewModel() {
        mPhoneLoginViewModel = getActivityViewModel(PhoneLoginViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_phone_login, BR.vm, mPhoneLoginViewModel)
                .addBindingParam(BR.proxy, new ClickProxy())
                .addBindingParam(BR.listener, listener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //观察登录或注册状态  成功后跳转到主界面
        mPhoneLoginViewModel.accountRequest.getLoginLiveData().observe(this, loginBean -> {
            if (loginBean.getCode() == 200) {
                //登陆成功
                SharePreferenceUtil.getInstance(Utils.getApp()).saveUserInfo(loginBean, mPhoneLoginViewModel.phone.get());
                startActivity(new Intent(PhoneLoginActivity.this, MainActivity.class));
                finish();
            } else {
                //登陆失败
                showLongToast("手机号或密码输入错误");
            }
        });
        //观察验证码发送状态  成功后显示验证码输入界面 并且倒计时启动
        mPhoneLoginViewModel.accountRequest.getCaptureLiveData().observe(this, message -> {
            if (message.getCode() == 200) {
                //验证码发送成功
                mPhoneLoginViewModel.showCaptureView.set(true);
                mPhoneLoginViewModel.showInputPassword.set(false);
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
                showLongToast(message.getMessage());
            }
        });
    }


    public class ClickProxy {
        //返回按钮
        public void back() {
            //判断当前状态   输入手机号状态  输入密码状态  忘记密码状态 输入验证码状态
            //PhoneLoginActivity.this.finish();
            if (mPhoneLoginViewModel.showInputPhone.get()) {
                //在输入手机号界面 直接结束Activity
                finish();
            } else if (mPhoneLoginViewModel.showForgetPassword.get() && mPhoneLoginViewModel.showInputPassword.get()) {
                //当前在忘记密码界面 返回时显示输入密码界面
                mPhoneLoginViewModel.showForgetPassword.set(false);
                mPhoneLoginViewModel.title.set("手机号登录");
                mPhoneLoginViewModel.passwordHint.set("请输入密码");
            } else if (mPhoneLoginViewModel.showInputPassword.get() && !mPhoneLoginViewModel.showForgetPassword.get()) {
                //当前在输入密码界面 返回时显示输入手机号界面
                mPhoneLoginViewModel.showInputPassword.set(false);
                mPhoneLoginViewModel.showInputPhone.set(true);
            }
        }

        //下一步
        public void next() {
            //校验手机号格式
            if (ValidateUtils.isMobile(mPhoneLoginViewModel.phone.get())) {
                mPhoneLoginViewModel.showInputPhone.set(false);
                mPhoneLoginViewModel.showInputPassword.set(true);
                mPhoneLoginViewModel.passwordHint.set("请输入密码");
                //密码框获取到焦点
            } else {
                //手机号格式不正确
                showShortToast("请输入正确格式的手机号");
            }
        }

        //登录
        public void login() {
            //判断是要发送验证码  还是直接登录
            if (mPhoneLoginViewModel.showForgetPassword.get()) {
                //校验新输入的密码格式
                if (mPhoneLoginViewModel.password.get().length() > 2 && mPhoneLoginViewModel.password.get().length() < 20
                        && ValidateUtils.isPassword(mPhoneLoginViewModel.password.get())) {
                    //发送验证码 显示倒计时 显示验证码输入框
                    mPhoneLoginViewModel.accountRequest.sendCapture(mPhoneLoginViewModel.phone.get());
                }

            } else {
                //登录请求
                mPhoneLoginViewModel.accountRequest.requestLogin(mPhoneLoginViewModel.phone.get(), mPhoneLoginViewModel.password.get());
            }
        }

        //忘记密码
        public void forgetPassword() {
            mPhoneLoginViewModel.showForgetPassword.set(true);
            mPhoneLoginViewModel.title.set("忘记密码");
            mPhoneLoginViewModel.passwordHint.set("请设置登录密码,8-20位字符,至少字母/数字/符号两种组合");
        }

        //重新获取验证码  只有在倒计时结束 显示重新获取 后才可以点击此按钮
        public void reObtainCpatureCode() {
            mPhoneLoginViewModel.accountRequest.sendCapture(mPhoneLoginViewModel.phone.get());
        }
    }

    //验证码输入完成后的回调
    public CaptchaView.OnInputListener listener = new CaptchaView.OnInputListener() {
        @Override
        public void onSucess(String code) {
            //注册(更改密码)
            mPhoneLoginViewModel.accountRequest.register(mPhoneLoginViewModel.phone.get(), mPhoneLoginViewModel.password.get(), code);
        }
    };


}
