package com.netease.music.ui.page.discover.square.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.netease.lib_api.model.playlist.PlaylistDetailBean;
import com.netease.lib_api.model.search.AlbumSearchBean;
import com.netease.lib_api.model.song.AudioBean;
import com.netease.lib_api.model.song.DailyRecommendSongsBean;
import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.lib_audio.app.AudioHelper;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.data.config.TypeEnum;
import com.netease.music.ui.page.adapter.PlayMusicListAdapter;
import com.netease.music.ui.page.discover.user.UserDetailActivity;
import com.netease.music.ui.state.SonglistDeatilViewModel;

public class SongListDetailActivity extends BaseActivity {


    private SonglistDeatilViewModel mViewModel;

    private static final String TYPEID = "TYPE_ID";
    private static final String LISTID = "LIST_ID";
    private static final String REASON = "REASON";

    public static void startActivity(Context context, int typeId, long listId, String copyWriter) {
        Bundle bundle = new Bundle();
        bundle.putInt(TYPEID, typeId);
        bundle.putLong(LISTID, listId);
        bundle.putString(REASON, copyWriter);
        Intent intent = new Intent(context, SongListDetailActivity.class);
        intent.putExtra("data", bundle);
        context.startActivity(intent);
    }


    @Override
    protected void initViewModel() {
        mViewModel = getActivityScopeViewModel(SonglistDeatilViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_gedan_detail, BR.vm, mViewModel)
                .addBindingParam(BR.proxy, new ClickProxy());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getIntent().getBundleExtra("data");

        //歌单相关
        mViewModel.songListRequest.getPlayListLiveData().observe(this, playlistDetailBean -> {
            final PlaylistDetailBean.PlaylistBean playlist = playlistDetailBean.getPlaylist();
            //歌单名称
            mViewModel.title.set(playlist.getName());
            //歌单描述
            mViewModel.desc.set(playlist.getDescription());
            //歌单创建者
            mViewModel.creator.set(playlist.getCreator().getNickname());
            //播放数量
            mViewModel.playCount.set(playlist.getPlayCount());
            mViewModel.shareCount.set(playlist.getShareCount());
            mViewModel.commentCount.set(playlist.getCommentCount());
            mViewModel.collectCount.set(playlist.getSubscribedCount());
            mViewModel.songCount.set(String.valueOf(playlist.getTrackCount()));
            //是否收藏该歌单
            mViewModel.isCollected.set(playlist.isSubscribed());

            //图片相关
            //封面
            mViewModel.coverImgUrl.set(playlist.getCoverImgUrl());
            //作者
            mViewModel.creatorImgUrl.set(playlist.getCreator().getAvatarUrl());
            //背景图
            mViewModel.backgroundImgUrl.set(playlist.getCoverImgUrl());

            //用户ID
            mViewModel.creatorId.set(playlist.getCreator().getUserId());
        });
        //专辑相关
        mViewModel.songListRequest.getAlbumDetailLiveData().observe(this, albumDetailBean -> {
            AlbumSearchBean.ResultBean.AlbumsBean album = albumDetailBean.getAlbum();
            //歌单名称
            if (album.getAlias().size() != 0) {
                mViewModel.title.set(album.getName() + album.getAlias().get(0));
            } else {
                mViewModel.title.set(album.getName());
            }
            //歌单描述
            mViewModel.desc.set(album.getDescription());
            //歌单创建者
            if (album.getArtists().size() > 1) {
                //拼接歌手名
                mViewModel.creator.set(album.getArtists().get(0).getName() + "/" + album.getArtists().get(1).getName());
            } else {
                mViewModel.creator.set(album.getArtist().getName());
            }
            //专辑不显示播放数量
            mViewModel.shareCount.set(albumDetailBean.getShareCount());
            mViewModel.commentCount.set(albumDetailBean.getCommentCount());
            mViewModel.collectCount.set(albumDetailBean.getSubCount());
            mViewModel.songCount.set(String.valueOf(albumDetailBean.getSongs().size()));
            //是否收藏该歌单
            mViewModel.isCollected.set(albumDetailBean.isSub());

            //图片相关
            //封面
            mViewModel.coverImgUrl.set(album.getPicUrl());
            //背景图  暂时不显示背景图片
            //mViewModel.backgroundImgUrl.set(album.getPicUrl());

            mViewModel.creatorId.set(album.getArtist().getId());

        });

        //歌曲数据  专辑和歌单的数据
        mViewModel.songListRequest.getSongDetailLiveData().observe(this, songList -> {
            PlayMusicListAdapter playMusicListAdapter = new PlayMusicListAdapter(songList);
            playMusicListAdapter.setOnItemClickListener((adapter, view, position) -> {
                //加入播放队列
                DailyRecommendSongsBean item = (DailyRecommendSongsBean) adapter.getItem(position);
                AudioHelper.addAudio(AudioBean.convertSongToAudioBean(item));
            });
            mViewModel.adapter.set(playMusicListAdapter);
            //延迟1s 显示加载动画
            new Handler().postDelayed(() -> mViewModel.loadingVisible.set(false), 1000);

        });

        //改变对专辑或歌单的收藏或者取消收藏
        mViewModel.songListRequest.getChangeSubStatusLiveData().observe(this, status -> {
            if (status) {
                //改变状态成功 则状态取反
                mViewModel.isCollected.set(!mViewModel.isCollected.get());
            }
        });

        if (data != null) {
            mViewModel.type.set(TypeEnum.getTypeByID(data.getInt(TYPEID)));
            mViewModel.reason.set(data.getString(REASON));
            mViewModel.listId.set(data.getLong(LISTID));
            if (TypeEnum.getTypeByID(data.getInt(TYPEID)) == TypeEnum.PLAYLIST) {
                //请求歌单相关数据
                mViewModel.songListRequest.requestPlayListMusicLiveData(mViewModel.listId.get());
            } else if (TypeEnum.getTypeByID(data.getInt(TYPEID)) == TypeEnum.ALBUM) {
                //请求专辑相关数据
                mViewModel.songListRequest.requestAlbumLiveData(mViewModel.listId.get());
            } else {
                throw new IllegalArgumentException("type id parse error");
            }
        }
    }


    public class ClickProxy {
        public void back() {
            finish();
        }

        //改变对专辑或歌单的收藏或者取消收藏
        public void changeSubscribeStatus() {
            mViewModel.songListRequest.requestChangeSubscribeListStatus(mViewModel.type.get(), mViewModel.isCollected.get(), mViewModel.listId.get());
        }

        public void comment() {
            //专辑评论 或者歌单评论
            CommentActivity.startActivity(SongListDetailActivity.this, mViewModel.listId.get().toString(), mViewModel.type.get().getValue(), mViewModel.coverImgUrl.get(), mViewModel.title.get(), mViewModel.creator.get());
        }

        //查看用户详情或者歌手详情
        public void userInfo() {
            if (mViewModel.type.get() == TypeEnum.PLAYLIST) {
                //查看用户
                UserDetailActivity.startActivity(SongListDetailActivity.this, mViewModel.creatorId.get());
            } else {
                //查看歌手
            }
        }
    }
}
