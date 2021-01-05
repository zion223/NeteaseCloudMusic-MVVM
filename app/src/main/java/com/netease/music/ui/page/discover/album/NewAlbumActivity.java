package com.netease.music.ui.page.discover.album;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.lib_api.model.search.AlbumSearchBean;
import com.netease.lib_image_loader.app.ImageLoaderManager;
import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.data.config.TypeEnum;
import com.netease.music.ui.page.discover.square.detail.SongListDetailActivity;
import com.netease.music.ui.state.NewAlbumViewModel;

import java.util.List;

public class NewAlbumActivity extends BaseActivity {

    private NewAlbumViewModel mViewModel;

    @Override
    protected void initViewModel() {
        mViewModel = getActivityScopeViewModel(NewAlbumViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_new_album, BR.vm, mViewModel)
                .addBindingParam(BR.proxy, new ClickProxy());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NewAlbumAdapter newAlbumAdapter = new NewAlbumAdapter(null);
        newAlbumAdapter.setOnItemClickListener((adapter, view, position) -> {
            AlbumSearchBean.ResultBean.AlbumsBean entity = (AlbumSearchBean.ResultBean.AlbumsBean) adapter.getItem(position);
            SongListDetailActivity.startActivity(NewAlbumActivity.this, TypeEnum.ALBUM_ID, entity.getId(), "");
        });
        mViewModel.request.getAlbumLiveData().observe(this, album -> {
            newAlbumAdapter.addData(album.getAlbums());
            mViewModel.adapter.set(newAlbumAdapter);
            new Handler().postDelayed(() -> mViewModel.loadingVisible.set(false), 1000);
        });
        //请求数据
        mViewModel.request.requestAlbumData();
    }

    public class ClickProxy {
        public void back() {
            finish();
        }
    }

    private static class NewAlbumAdapter extends BaseQuickAdapter<AlbumSearchBean.ResultBean.AlbumsBean, BaseViewHolder> {
        private ImageLoaderManager manager;

        NewAlbumAdapter(@Nullable List<AlbumSearchBean.ResultBean.AlbumsBean> data) {
            super(R.layout.item_discover_album, data);
            manager = ImageLoaderManager.getInstance();
        }

        @Override
        protected void convert(@NonNull BaseViewHolder adapter, AlbumSearchBean.ResultBean.AlbumsBean item) {
            adapter.setVisible(R.id.iv_item_album_icon, true);
            manager.displayImageForCorner(adapter.getView(R.id.iv_item_album_song), item.getPicUrl());
            adapter.setText(R.id.tv_item_album_song_name, item.getName());
            adapter.setText(R.id.tv_item_album_song_artist, item.getArtist().getName());
        }
    }
}
