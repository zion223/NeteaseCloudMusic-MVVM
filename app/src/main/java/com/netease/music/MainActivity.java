package com.netease.music;

import android.os.Bundle;
import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.lib_audio.mediaplayer.core.AudioController;
import com.netease.music.ui.callback.SharedViewModel;
import com.netease.music.ui.helper.DrawerCoordinateHelper;
import com.netease.music.ui.state.MainActivityViewModel;


/**
 * @author Administrator
 */
public class MainActivity extends BaseActivity {

    private MainActivityViewModel mMainActivityViewModel;
    private SharedViewModel mSharedViewModel;
    private boolean mIsListened = false;
    private long firstTime = 0;

    @Override
    protected void initViewModel() {
        mMainActivityViewModel = getActivityScopeViewModel(MainActivityViewModel.class);
        mSharedViewModel = getApplicationScopeViewModel(SharedViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        //TODO tip 1: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图刷新的安全性将和基于函数式编程的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样的背景下，用于为 base 页面中的 DataBinding 提供绑定项。

        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        return new DataBindingConfig(R.layout.activity_main, BR.vm, mMainActivityViewModel)
                .addBindingParam(BR.event, new EventHandler());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedViewModel.isToCloseActivityIfAllowed().observe(this, aBoolean -> {
            NavController nav = Navigation.findNavController(this, R.id.main_fragment_host);
            if (nav.getCurrentDestination() != null && nav.getCurrentDestination().getId() != R.id.mainFragment) {
                nav.navigateUp();

            } else if (mMainActivityViewModel.isDrawerOpened.get()) {

                //TODO 同 tip 2

                mSharedViewModel.requestToOpenOrCloseDrawer(false);

            } else {
                if (System.currentTimeMillis() - firstTime > 2000) {
                    showShortToast(R.string.exit_app_toast);
                    firstTime = System.currentTimeMillis();
                } else {
                    exitApp();
                }
            }
        });

        // 观察是否需要立即 finish当前Activity
        mSharedViewModel.isToCloseActivityImmediately().observe(this, aBoolean -> {
            if (aBoolean) {
                //exitApp();
                finish();
            }
        });


        mSharedViewModel.isToOpenOrCloseDrawer().observe(this, aBoolean -> {

            //TODO yes：同 tip 1: 此处将 drawer 的 open 和 close 都放在 drawerBindingAdapter 中操作，规避了视图调用的一致性问题，

            //因为 横屏布局 根本就没有 drawerLayout。此处如果用传统的视图调用方式，会很容易因疏忽而造成空引用。

            //TODO 此外，此处为 drawerLayout 绑定的状态 "openDrawer"，使用 LiveData 而不是 ObservableField，主要是考虑到 ObservableField 具有 "防抖" 的特性，不适合该场景。

            //如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350

            mMainActivityViewModel.openDrawer.setValue(aBoolean);

            //TODO do not:（容易因疏忽 而埋下视图调用的一致性隐患）

            /*if (mBinding.dl != null) {
                if (aBoolean && !mBinding.dl.isDrawerOpen(GravityCompat.START)) {
                    mBinding.dl.openDrawer(GravityCompat.START);
                } else {
                    mBinding.dl.closeDrawer(GravityCompat.START);
                }
            }*/
        });

        DrawerCoordinateHelper.getInstance().isEnableSwipeDrawer().observe(this, aBoolean -> {

            //TODO yes: 同 tip 1

            mMainActivityViewModel.allowDrawerOpen.setValue(aBoolean);

            // TODO do not:（容易因疏忽 而埋下视图调用的一致性隐患）

            /*if (mBinding.dl != null) {
                mBinding.dl.setDrawerLockMode(aBoolean
                        ? DrawerLayout.LOCK_MODE_UNLOCKED
                        : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }*/
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!mIsListened) {

            // TODO tip 2：此处演示通过 UnPeekLiveData 来发送 生命周期安全的、事件源可追溯的 通知。

            // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/0168753249
            // --------
            // 与此同时，此处传达的另一个思想是 最少知道原则，
            // fragment 内部的事情在 fragment 内部消化，不要试图在 Activity 中调用和操纵 Fragment 内部的东西。
            // 因为 fragment 端的处理后续可能会改变，并且可受用于更多的 Activity，而不单单是本 Activity。

            mSharedViewModel.requestToAddSlideListener(true);

            mIsListened = true;
        }
    }

    @Override
    public void onBackPressed() {

        // TODO 同 tip 2

        mSharedViewModel.requestToCloseSlidePanelIfExpanded(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AudioController.getInstance().release();
    }

    public class EventHandler extends DrawerLayout.SimpleDrawerListener {
        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            mMainActivityViewModel.isDrawerOpened.set(true);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            mMainActivityViewModel.isDrawerOpened.set(false);
            mMainActivityViewModel.openDrawer.setValue(false);
        }
    }


}
