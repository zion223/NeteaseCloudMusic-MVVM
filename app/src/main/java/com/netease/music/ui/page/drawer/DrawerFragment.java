package com.netease.music.ui.page.drawer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.lxj.xpopup.XPopup;
import com.netease.lib_audio.mediaplayer.core.AudioController;
import com.netease.lib_common_ui.dialog.TimerOffDialog;
import com.netease.lib_common_ui.utils.SharePreferenceUtil;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.callback.SharedViewModel;
import com.netease.music.ui.page.login.LoginActivity;
import com.netease.music.ui.state.DrawerViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class DrawerFragment extends BaseFragment {

    private DrawerViewModel mDrawerViewModel;
    private SharedViewModel mSharedViewModel;

    private Disposable mPauseMusicDisposable;

    private SharePreferenceUtil preferenceUtill;
    @Override
    protected void initViewModel() {
        mDrawerViewModel = getFragmentScopeViewModel(DrawerViewModel.class);
        mSharedViewModel = getApplicationScopeViewModel(SharedViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        return new DataBindingConfig(R.layout.fragment_drawer, BR.vm, mDrawerViewModel)
                .addBindingParam(BR.click, new ClickProxy());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceUtill = SharePreferenceUtil.getInstance(getContext());

        // 观察LiveData
        mDrawerViewModel.request.getLoginOutLiveData().observe(getViewLifecycleOwner(), dataResult -> {
            if (dataResult.getResponseStatus().isSuccess()) {
                // 成功退出登录
                preferenceUtill.removeUserInfo();
                // finish当前Activity
                mSharedViewModel.requestToFinishActivity(true);
                startActivity(new Intent(getContext(), LoginActivity.class));
            } else {
                showShortToast(dataResult.getResponseStatus().getResponseCode() + getString(R.string.op_failed));
            }
        });
    }

    public class ClickProxy {

        //查看消息
        public void searchMessage() {
            // 关闭侧边栏
            mSharedViewModel.requestToOpenOrCloseDrawer(false);
            nav().navigate(R.id.action_drawerFragment_to_messageTabActivity);
        }

        //查看音乐云盘
        public void searchCloud() {
            mSharedViewModel.requestToOpenOrCloseDrawer(false);
            nav().navigate(R.id.action_drawerFragment_to_cloudActivity);
        }

        //登录界面
        public void login() {
            mSharedViewModel.requestToOpenOrCloseDrawer(false);
            nav().navigate(R.id.action_drawerFragment_to_loginActivity);
        }

        // 打开定时停止播放dialog
        public void openTimerDialog() {
            mSharedViewModel.requestToOpenOrCloseDrawer(false);
            new XPopup.Builder(getContext())
                    .asCustom(new TimerOffDialog(getContext(), listener))
                    .show();
        }

        // 退出登录
        public void exitLogin() {
            mDrawerViewModel.request.requestLoginOut();
        }
    }

    private TimerOffDialog.OnSelectTimerListener listener = time -> {
        if (time == 0) {
            mDrawerViewModel.leftPauseAudioTime.setValue("");
            if (mPauseMusicDisposable != null) {
                mPauseMusicDisposable.dispose();
            }
        } else {
            if (mPauseMusicDisposable != null) {
                mPauseMusicDisposable.dispose();
            }
            mPauseMusicDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        if (aLong == time * 60 - 1) {
                            // 到达定时时间
                            mPauseMusicDisposable.dispose();
                            mDrawerViewModel.leftPauseAudioTime.setValue("");
                            // 停止播放音乐
                            AudioController.getInstance().pause();
                            // 恢复定时时间为0
                            SharePreferenceUtil.getInstance(getContext()).setStopAudioTime(0);
                        } else {
                            int remainSecends = time * 60 - aLong.intValue();
                            int minutes = remainSecends / 60;
                            int remainingSeconds = remainSecends % 60;
                            mDrawerViewModel.leftPauseAudioTime.setValue(minutes + ":" + remainingSeconds);
                        }
                    });
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPauseMusicDisposable != null && !mPauseMusicDisposable.isDisposed()) {
            mPauseMusicDisposable.dispose();
            mPauseMusicDisposable = null;
        }
    }
}
