package com.netease.music.ui.page.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.netease.lib_api.model.playlist.MainRecommendPlayListBean;
import com.netease.lib_image_loader.app.ImageLoaderManager;
import com.kunminx.architecture.ui.adapter.SimpleDataBindingAdapter;
import com.netease.music.R;
import com.netease.music.data.config.TYPE;
import com.netease.music.databinding.ItemDiscoverGedanBinding;
import com.netease.music.ui.page.discover.square.detail.SongListDetailActivity;

public class RecommendPlayListAdapter extends SimpleDataBindingAdapter<MainRecommendPlayListBean.RecommendBean, ItemDiscoverGedanBinding> {

    private ImageLoaderManager manager;

    public RecommendPlayListAdapter(Context context) {
        super(context, R.layout.item_discover_gedan, DiffUtils.getInstance().getRecommendPlayListItemCallback());
        manager = ImageLoaderManager.getInstance();
        setOnItemClickListener((item, position) -> SongListDetailActivity.startActivity(context, TYPE.PLAYLIST_ID, item.getId(), item.getCopywriter()));
    }

    @Override
    protected void onBindItem(ItemDiscoverGedanBinding binding, MainRecommendPlayListBean.RecommendBean item, RecyclerView.ViewHolder holder) {
        binding.setItem(item);
        //圆角图片
        manager.displayImageForCorner(binding.ivItemDiscover, item.getPicUrl(), 5);
    }
}