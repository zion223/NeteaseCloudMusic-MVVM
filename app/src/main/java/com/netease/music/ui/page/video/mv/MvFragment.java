package com.netease.music.ui.page.video.mv;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.state.MvViewModel;
import com.netease.music.util.TimeUtil;

public class MvFragment extends BaseFragment {

    private MvViewModel mViewModel;

    @Override
    protected void initViewModel() {
        mViewModel = getFragmentScopeViewModel(MvViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_mv_normal, BR.vm, mViewModel)
                .addBindingParam(BR.proxy, new ClickProxy());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel.request.getMvTopBeanLiveData().observe(this, mvTopBean -> {
            mViewModel.updateTime.set("更新时间 : " + TimeUtil.getTimeStandardOnlyMDChinese(mvTopBean.getUpdateTime()));
            mViewModel.mvCoverImg.set(mvTopBean.getData().get(0).getCover());
        });

        mViewModel.request.requestMvTop();

    }

    public class ClickProxy {
        //更多Mv
        public void mvMore() {

        }

        //mv排行榜
        public void mvRank() {

        }

        //MV分类
        public void mvSort() {
            startActivity(new Intent(getContext(), MvSortActivity.class));
        }
    }
}
