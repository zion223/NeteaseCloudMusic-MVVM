package com.netease.music.ui.page.discover.radio;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.lib_api.model.dj.DjRecommendBean;
import com.netease.lib_image_loader.app.ImageLoaderManager;
import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.state.RadioViewModel;

import java.util.ArrayList;
import java.util.List;

public class RadioActivity extends BaseActivity {

    private RadioViewModel mRadioViewModel;

    @Override
    protected void initViewModel() {
        mRadioViewModel = getActivityScopeViewModel(RadioViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_radio, BR.vm, mRadioViewModel)
                .addBindingParam(BR.proxy, new ClickProxy());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DjRecommendAdapter djRecommendAdapter = new DjRecommendAdapter(null);
        djRecommendAdapter.setOnItemClickListener((adapter, view, position) -> {
            //跳转到电台详情
            DjRecommendBean.DjRadiosBean entity = (DjRecommendBean.DjRadiosBean) adapter.getItem(position);
            RadioDetailActivity.startActivity(this, String.valueOf(entity.getId()));
        });
        mRadioViewModel.adapter.set(djRecommendAdapter);

        mRadioViewModel.request.getBannerLiveData().observe(this, djBannerBean -> {
            int size = djBannerBean.getBanners().size();
            final ArrayList<String> picList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                picList.add(djBannerBean.getBanners().get(i).getPic());
            }
            mRadioViewModel.bannersPic.set(picList);
        });
        mRadioViewModel.request.getRadioRecommendLiveData().observe(this, recommendBean -> {
            mRadioViewModel.recommendRadio.set(recommendBean.getDjRadios());
            mRadioViewModel.currentRecommendRadio.set(mRadioViewModel.recommendRadio.get().subList(mRadioViewModel.currentIndex.get(), mRadioViewModel.currentIndex.get() + 3));
            new Handler().postDelayed(() -> mRadioViewModel.loadingVisible.set(false), 500);
        });

        mRadioViewModel.request.requestRadioBanner();
        mRadioViewModel.request.requestRecommendRadio();
    }


    public class ClickProxy {
        public void back() {
            finish();
        }

        //付费精品
        public void vip() {
            startActivity(new Intent(RadioActivity.this, RadioPayActivity.class));
        }

        public void changeRecommendData() {
            if (mRadioViewModel.currentIndex.get() + 3 < 7) {
                mRadioViewModel.currentIndex.set(mRadioViewModel.currentIndex.get() + 3);
            } else {
                mRadioViewModel.currentIndex.set(0);
            }
            mRadioViewModel.currentRecommendRadio.set(mRadioViewModel.recommendRadio.get().subList(mRadioViewModel.currentIndex.get(), mRadioViewModel.currentIndex.get() + 3));
        }
    }

    static class DjRecommendAdapter extends BaseQuickAdapter<DjRecommendBean.DjRadiosBean, BaseViewHolder> {

        private ImageLoaderManager manager;

        DjRecommendAdapter(List<DjRecommendBean.DjRadiosBean> data) {
            super(R.layout.item_radio_recommend, data);
            manager = ImageLoaderManager.getInstance();
        }

        @Override
        protected void convert(@NonNull BaseViewHolder adapter, DjRecommendBean.DjRadiosBean bean) {
            adapter.setText(R.id.iv_radio_recommend_name, bean.getName());
            adapter.setText(R.id.iv_radio_recommend_des, bean.getRcmdtext());
            manager.displayImageForCorner(adapter.getView(R.id.iv_radio_recommend_img), bean.getPicUrl(), 5);
        }
    }
}
