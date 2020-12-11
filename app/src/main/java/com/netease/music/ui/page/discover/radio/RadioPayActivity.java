package com.netease.music.ui.page.discover.radio;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.lib_api.model.dj.DjPaygiftBean;
import com.netease.lib_image_loader.app.ImageLoaderManager;
import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.state.RadioPayViewModel;

import java.util.List;

public class RadioPayActivity extends BaseActivity {

    private RadioPayViewModel mViewModel;

    @Override
    protected void initViewModel() {
        mViewModel = getActivityScopeViewModel(RadioPayViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_radio_pay, BR.vm, mViewModel)
                .addBindingParam(BR.proxy, new ClickProxy());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RadioPayAdapter radioPayAdapter = new RadioPayAdapter(null);
        radioPayAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            mViewModel.offset.set(mViewModel.offset.get() + 10);
            mViewModel.request.requestRadioPayData(mViewModel.limit.get(), mViewModel.offset.get());
        });
        mViewModel.adapter.set(radioPayAdapter);

        //观察付费精品数据
        mViewModel.request.getRadioPayLiveData().observe(this, djPaygiftBean -> {

            //第一次请求 和 加载更多的请求
            mViewModel.radioList.set(djPaygiftBean.getData().getList());
            if (radioPayAdapter.getLoadMoreModule().isLoading()) {
                radioPayAdapter.getLoadMoreModule().loadMoreComplete();
            } else if (mViewModel.offset.get() == 0) {
                //第一次加载
                new Handler().postDelayed(() -> mViewModel.loadingVisible.set(false), 500);
            }
        });
        //请求数据
        mViewModel.request.requestRadioPayData(mViewModel.limit.get(), mViewModel.offset.get());
    }


    public class ClickProxy {
        public void back() {
            finish();
        }
    }


    static class RadioPayAdapter extends BaseQuickAdapter<DjPaygiftBean.DataBean.ListBean, BaseViewHolder> implements LoadMoreModule {

        private ImageLoaderManager manager;

        RadioPayAdapter(@Nullable List<DjPaygiftBean.DataBean.ListBean> data) {
            super(R.layout.item_radio_pay_normal, data);
            manager = ImageLoaderManager.getInstance();
        }

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, DjPaygiftBean.DataBean.ListBean bean) {
            baseViewHolder.setText(R.id.tv_radio_pay_title, bean.getName());
            baseViewHolder.setText(R.id.tv_radio_pay_desc, bean.getRcmdText());
            int radioFeeType = bean.getRadioFeeType();
            if (radioFeeType == 2) {
                baseViewHolder.setText(R.id.tv_radio_pay_price, "￥" + bean.getOriginalPrice() / 100);
            } else if (radioFeeType == 1) {
                // 分期收费
                baseViewHolder.setText(R.id.tv_radio_pay_price, "￥" + bean.getOriginalPrice() / 100 + "/期");
            }
            manager.displayImageForCorner(baseViewHolder.getView(R.id.iv_radio_pay_img), bean.getPicUrl(), 5);
        }
    }
}
