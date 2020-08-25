package com.netease.music.ui.page.adapter;

import android.content.Context;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.imooc.lib_api.model.user.UserPlaylistBean;
import com.imooc.lib_api.model.user.UserPlaylistEntity;
import com.imooc.lib_image_loader.app.ImageLoaderManager;
import com.netease.music.R;
import com.netease.music.data.config.TYPE;
import com.netease.music.ui.page.discover.square.detail.SongListDetailActivity;
import com.netease.music.util.SearchUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserHomePagePlayListAdapter extends BaseMultiItemQuickAdapter<UserPlaylistEntity, BaseViewHolder> {

    private ImageLoaderManager imageLoaderManager;

    public UserHomePagePlayListAdapter(Context context, List<UserPlaylistEntity> data) {
        super(data);
        addItemType(UserPlaylistEntity.TYPE_HEADER, R.layout.item_user_info_playlist_header);
        addItemType(UserPlaylistEntity.TYPE_CONTENT, R.layout.item_mine_gedan_content);
        addItemType(UserPlaylistEntity.TYPE_FOOTER, R.layout.item_more_info);
        imageLoaderManager = ImageLoaderManager.getInstance();
        setOnItemClickListener((adapter, view, position) -> {
            UserPlaylistEntity entity = (UserPlaylistEntity) adapter.getItem(position);
            switch (entity.getItemType()) {
                case UserPlaylistEntity.TYPE_HEADER:
                    break;
                case UserPlaylistEntity.TYPE_CONTENT:
                    //查看歌单详情
                    SongListDetailActivity.startActivity(context, TYPE.PLAYLIST_ID, entity.getPlaylistBean().getId(), "");
                    break;
                case UserPlaylistEntity.TYPE_FOOTER:
                    //查看更多歌单 TODO
                    Toast.makeText(context, "此功能暂时未实现", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, UserPlaylistEntity entity) {
        switch (baseViewHolder.getItemViewType()) {
            case UserPlaylistEntity.TYPE_HEADER:
                baseViewHolder.setText(R.id.tv_user_info_create_right, entity.getPlayListSize());
                baseViewHolder.setText(R.id.tv_user_info_create_left, entity.getHeaderText());
                break;
            case UserPlaylistEntity.TYPE_CONTENT:
                baseViewHolder.setVisible(R.id.iv_item_gedan_more, false);
                UserPlaylistBean.PlaylistBean item = entity.getPlaylistBean();
                //歌单名
                baseViewHolder.setText(R.id.tv_item_gedan_content_toptext, item.getName());
                //歌单图片
                imageLoaderManager.displayImageForCorner(baseViewHolder.getView(R.id.iv_item_gedan_content_img), item.getCoverImgUrl());
                if (entity.getType() == UserPlaylistEntity.TYPE.TYPE_SUBSCRIBE) {
                    baseViewHolder.setText(R.id.tv_item_gedan_content_bottomtext, item.getTrackCount() + "首,  by " + item.getCreator().getNickname() + "  播放" + SearchUtil.getCorresPondingString(item.getPlayCount()) + "次");
                } else if (entity.getType() == UserPlaylistEntity.TYPE.TYPE_CREATE) {
                    baseViewHolder.setText(R.id.tv_item_gedan_content_bottomtext, item.getTrackCount() + "首,  播放" + SearchUtil.getCorresPondingString(item.getPlayCount()) + "次");
                }
                break;
            case UserPlaylistEntity.TYPE_FOOTER:
                baseViewHolder.setText(R.id.tv_more_info, entity.getFooterText());
                break;
            default:
                break;
        }
    }
}
