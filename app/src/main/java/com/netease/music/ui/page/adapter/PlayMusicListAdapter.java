package com.netease.music.ui.page.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.lib_api.model.song.DailyRecommendSongsBean;
import com.netease.lib_api.model.song.SongDetailBean;
import com.netease.lib_common_ui.dialog.MusicPopUpDialog;
import com.netease.music.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class PlayMusicListAdapter extends BaseQuickAdapter<DailyRecommendSongsBean, BaseViewHolder> {

    private Context mContext;
    //是否有头布局 影响显示序号
    private Boolean hasHeader = false;
    //在用户的听歌排行中不显示此View 而是显示听歌的次数
    private boolean showMoreView = true;
    //在新歌速递中或者每日推荐中显示歌曲的专辑的图片而不是显示序号
    private boolean showAlbumImg = false;

    public PlayMusicListAdapter(List<DailyRecommendSongsBean> data, boolean hasHeader) {
        super(R.layout.item_gedan_detail_song, data);
        this.hasHeader = hasHeader;
    }

    public PlayMusicListAdapter(List<DailyRecommendSongsBean> data) {
        super(R.layout.item_gedan_detail_song, data);
    }

    public PlayMusicListAdapter(List<DailyRecommendSongsBean> data, boolean hasHeader, boolean showMoreView) {
        super(R.layout.item_gedan_detail_song, data);
        this.hasHeader = hasHeader;
        this.showMoreView = showMoreView;
    }


    @Override
    protected void convert(@NotNull BaseViewHolder helper, DailyRecommendSongsBean item) {
        if (hasHeader) {
            helper.setText(R.id.item_play_no, String.valueOf(helper.getBindingAdapterPosition()));
        } else {
            helper.setText(R.id.item_play_no, String.valueOf(helper.getBindingAdapterPosition() + 1));
        }
        //歌曲名
        helper.setText(R.id.viewpager_list_toptext, item.getName());
        //歌手名 - 专辑名
        helper.setText(R.id.viewpager_list_bottom_text, item.getAr().get(0).getName() + " - " + item.getAl().getName());
        //当前歌曲是否有MV
        if (item.getMv() != 0 && showMoreView) {
            helper.setVisible(R.id.iv_list_video, true);
        }
        //当前歌曲的视频
//        helper.setOnClickListener(R.id.iv_list_video, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //mDelegate.getSupportDelegate().start(MvDeatilDelegate.newInstance(String.valueOf(item.getMv())));
//            }
//        });
//        helper.setOnClickListener(R.id.viewpager_list_button, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showMoreDialog(item);
//            }
//        });
        //是否显示 更多View 和mv图标绑定在一起  不显示更多按钮时 也不显示mv图标
        helper.setVisible(R.id.viewpager_list_button, showMoreView);
    }


    private void showMoreDialog(DailyRecommendSongsBean item) {
        new MusicPopUpDialog.Builder()
                .setContext(mContext)
                //是否显示删除选项
                .setDeleteViewInvisiable(true)
                .setmAlbumName(item.getAl().getName())
                .setmArtistName(item.getAr().get(0).getName())
                .setmAlbumPic(item.getAl().getPicUrl())
                .setmMusicName(item.getName())
                .setListener(new MusicPopUpDialog.OnClickItemListener() {
                    //下一首播放
                    @Override
                    public void onClickNext() {

                    }

                    //收藏到歌单
                    @Override
                    public void onClickAddFav() {
//                        new XPopup.Builder(mContext)
//                                .asCustom(new MusicCollectDialog(mContext, String.valueOf(item.getId())))
//                                .show();
                    }

                    //分享
                    @Override
                    public void onClickShare() {

                    }

                    //删除
                    @Override
                    public void onClickDelete() {
                        //删除创建的歌单的歌曲 删除云盘的歌曲

                    }

                    //歌手详情
                    @Override
                    public void onClickArtistDetail() {
                        //mDelegate.getSupportDelegate().start(ArtistDetailDelegate.newInstance(item.getAr().get(0).getId()));
                    }

                    //专辑详情
                    @Override
                    public void onClickAlbumDetail() {
                        //mDelegate.getSupportDelegate().start(SongListDetailDelegate.newInstance(ALBUM, item.getAl().getId()));
                    }

                    //歌曲评论
                    @Override
                    public void onClickComment() {
                        //mDelegate.getSupportDelegate().start(CommentDelegate.newInstance(String.valueOf(item.getId()), SONG, item.getAl().getPicUrl(), item.getAr().get(0).getName(), item.getAl().getName()));
                    }
                })
                .build().show();
    }
}

