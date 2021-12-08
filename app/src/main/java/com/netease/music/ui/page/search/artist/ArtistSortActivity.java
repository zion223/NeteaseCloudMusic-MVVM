package com.netease.music.ui.page.search.artist;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.lib_api.model.playlist.TopListDetailBean;
import com.netease.lib_common_ui.widget.ArtistSortView;
import com.netease.lib_image_loader.app.ImageLoaderManager;
import com.netease.lib_network.ApiEngine;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.state.ArtistSortViewModel;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class ArtistSortActivity extends BaseActivity {

    private ArtistSortViewModel mViewModel;

    @Override
    protected void initViewModel() {
        mViewModel = getActivityScopeViewModel(ArtistSortViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_artist_sort, BR.vm, mViewModel)
                .addBindingParam(BR.listener, listener)
                .addBindingParam(BR.adapter, new ArtistSortAdapter(null))
                .addBindingParam(BR.proxy, new ClickProxy());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(mViewModel.request);
        mViewModel.request.getTopArtistData().observe(this, artists -> mViewModel.artistData.set(artists));

        mViewModel.request.requestHotSinger();
    }

    private ArtistSortView.OnChooseArtistSortListener listener = (type, area) -> {
        //加载数据
        mViewModel.request.requestHotSingerArea(type, area);
    };


    public class ClickProxy {
        public void back() {
            finish();
        }
    }

    static class ArtistSortAdapter extends BaseQuickAdapter<TopListDetailBean.Artist, BaseViewHolder> {
        private ImageLoaderManager manager;

        ArtistSortAdapter(@Nullable List<TopListDetailBean.Artist> data) {
            super(R.layout.item_singer_normal, data);
            manager = ImageLoaderManager.getInstance();
            addChildClickViewIds(R.id.ll_singer_followed, R.id.ll_singer_follow);
            setOnItemChildClickListener((adapter, view, position) -> {
                TopListDetailBean.Artist entity = (TopListDetailBean.Artist) adapter.getItem(position);
                final View followedView = adapter.getViewByPosition(position, R.id.ll_singer_followed);
                final View followView = adapter.getViewByPosition(position, R.id.ll_singer_follow);
                //点击事件发生 则该View处于可见状态
                int id = view.getId();
                boolean isSubed = (id == R.id.ll_singer_followed);
                Disposable subscribe = ApiEngine.getInstance().getApiService().getSubArtist(entity.getId(), !isSubed ? 1 : 0)
                        .compose(ApiEngine.getInstance().applySchedulers())
                        .subscribe(djSubBean -> {
                            if (djSubBean.getCode() == 200 && followedView != null && followView != null) {
                                //关注或取消关注成功
                                followedView.setVisibility(!isSubed ? View.VISIBLE : View.GONE);
                                followView.setVisibility(!isSubed ? View.GONE : View.VISIBLE);
                            }
                        });
            });
        }

        @Override
        protected void convert(@NonNull final BaseViewHolder adapter, final TopListDetailBean.Artist item) {
            //是否已关注
            adapter.setVisible(item.isFollowed() ? R.id.ll_singer_followed : R.id.ll_singer_follow, true);

            adapter.setText(R.id.tv_singer_name, item.getName());

            manager.displayImageForCircle(adapter.getView(R.id.iv_singer_avatar), item.getPicUrl());
        }
    }


}
