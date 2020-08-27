package com.netease.music.ui.page.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.netease.lib_api.model.album.AlbumOrSongBean;
import com.kunminx.architecture.ui.adapter.SimpleDataBindingAdapter;
import com.netease.music.R;
import com.netease.music.data.config.TYPE;
import com.netease.music.databinding.ItemDiscoverAlbumSongBinding;
import com.netease.music.ui.page.discover.square.detail.SongListDetailActivity;

public class AlbumSongAdapter extends SimpleDataBindingAdapter<AlbumOrSongBean, ItemDiscoverAlbumSongBinding> {

    public AlbumSongAdapter(Context context) {
        super(context, R.layout.item_discover_album_song, DiffUtils.getInstance().getAlbumOrSongItemCallback());
        setOnItemClickListener((item, position) -> {
            if (item.getType() == TYPE.SONG_ID) {
                //加入播放队列
            } else {
                //查看专辑详情
                SongListDetailActivity.startActivity(context, item.getType(), item.getId(), "");
            }
        });
    }

    @Override
    protected void onBindItem(ItemDiscoverAlbumSongBinding binding, AlbumOrSongBean item, RecyclerView.ViewHolder holder) {
        binding.setItem(item);
    }
}
