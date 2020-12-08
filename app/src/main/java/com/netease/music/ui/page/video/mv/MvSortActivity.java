package com.netease.music.ui.page.video.mv;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.lib_api.model.mv.MvBean;
import com.netease.lib_common_ui.dialog.MvSortDialog;
import com.netease.lib_image_loader.app.ImageLoaderManager;
import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.state.MvSortViewModel;
import com.netease.music.util.SearchUtil;
import com.netease.music.util.TimeUtil;

import java.util.List;

public class MvSortActivity extends BaseActivity {

    private MvSortViewModel mvSortViewModel;

    @Override
    protected void initViewModel() {
        mvSortViewModel = getActivityScopeViewModel(MvSortViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_mv_sort, BR.vm, mvSortViewModel)
                .addBindingParam(BR.proxy, new ClickProxy())
                .addBindingParam(BR.adapter, new MvSortAdapter(this, null));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mvSortViewModel.request.getAllMvLiveData().observe(this, mvBean -> {
            mvSortViewModel.data.set(mvBean.getData());
            new Handler().postDelayed(() -> mvSortViewModel.loadingVisible.set(false), 500);
        });

        mvSortViewModel.request.requestAllMv(mvSortViewModel.area.get(), mvSortViewModel.type.get(), mvSortViewModel.order.get(), 30);
    }


    public class ClickProxy {
        //显示分类Dialog
        public void showSortDialog() {
            new XPopup.Builder(MvSortActivity.this)
                    .offsetY(100)
                    .popupAnimation(PopupAnimation.TranslateFromTop)
                    .asCustom(new MvSortDialog(MvSortActivity.this, (area, type, order) -> {
                        mvSortViewModel.area.set(area);
                        mvSortViewModel.type.set(type);
                        mvSortViewModel.order.set(order);
                        mvSortViewModel.request.requestAllMv(area, type, order, 30);
                        mvSortViewModel.loadingVisible.set(true);
                    })).show();
        }

        //返回
        public void back() {
            finish();
        }

    }

    static class MvSortAdapter extends BaseQuickAdapter<MvBean.MvDetailBean, BaseViewHolder> implements LoadMoreModule {

        MvSortAdapter(Context context, @Nullable List<MvBean.MvDetailBean> data) {
            super(R.layout.item_mv_normal, data);
            setOnItemClickListener((adapter, view, position) -> {
                //查看MV详情
                MvBean.MvDetailBean entity = (MvBean.MvDetailBean) adapter.getItem(position);
                MvDetailActivity.startActivity(context, entity.getId(), entity.getArtistId());
            });
        }

        @Override
        protected void convert(@NonNull BaseViewHolder adapter, MvBean.MvDetailBean item) {
            ImageLoaderManager.getInstance().displayImageForCorner(adapter.getView(R.id.iv_item_mv_cover), item.getCover());
            //播放量
            adapter.setText(R.id.tv_item_mv_playnum, SearchUtil.getCorresPondingString(item.getPlayCount()));
            //视频时间
            adapter.setText(R.id.tv_item_mv_time, TimeUtil.getTimeNoYMDH(item.getDuration()));
            //MV名字
            adapter.setText(R.id.tv_item_mv_name, item.getName());
            adapter.setText(R.id.tv_item_mv_creator, item.getArtistName());
        }
    }

}
