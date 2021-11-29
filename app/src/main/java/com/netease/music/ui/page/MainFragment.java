package com.netease.music.ui.page;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.callback.SharedViewModel;
import com.netease.music.ui.page.adapter.HomePagerAdapter;
import com.netease.music.ui.state.MainViewModel;

public class MainFragment extends BaseFragment {

    private MainViewModel mMainViewModel;
    private SharedViewModel mSharedViewModel;

    @Override
    protected void initViewModel() {
        mMainViewModel = getFragmentScopeViewModel(MainViewModel.class);
        mSharedViewModel = getApplicationScopeViewModel(SharedViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        return new DataBindingConfig(R.layout.fragment_main, BR.vm, mMainViewModel)
                .addBindingParam(BR.click, new ClickProxy())
                .addBindingParam(BR.adapter, new HomePagerAdapter(getChildFragmentManager(), mMainViewModel.channelArray.get()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSharedViewModel.isToCloseSlidePanelIfExpanded().observe(this, aBoolean -> {
            mSharedViewModel.requestToCloseActivityIfAllowed(true);
        });
    }

    public class ClickProxy {

        public void openMenu() {

            // TODO tip 3：此处演示通过 UnPeekLiveData 来发送 生命周期安全的、事件源可追溯的 通知。

            // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/0168753249
            // --------
            // 与此同时，此处传达的另一个思想是 最少知道原则，
            // Activity 内部的事情在 Activity 内部消化，不要试图在 fragment 中调用和操纵 Activity 内部的东西。
            // 因为 Activity 端的处理后续可能会改变，并且可受用于更多的 fragment，而不单单是本 fragment。

            mSharedViewModel.requestToOpenOrCloseDrawer(true);
        }

        public void login() {
            nav().navigate(R.id.action_mainFragment_to_loginActivity);
        }

        public void search() {
            nav().navigate(R.id.action_mainFragment_to_searchActivity);
        }

    }

}
