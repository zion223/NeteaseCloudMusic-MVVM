package com.netease.music.ui.page.video;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.lib_api.model.mv.VideoBean;
import com.netease.lib_image_loader.app.ImageLoaderManager;
import com.netease.lib_network.ApiEngine;
import com.netease.lib_video.videoplayer.CustomJzVideoView;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.state.VideoViewModel;
import com.netease.music.util.TimeUtil;

import java.util.List;

import cn.jzvd.Jzvd;


public class VideoFragment extends BaseFragment {


    private VideoViewModel mVideoViewModel;
    private static final String VIDEO_ID = "videoId";

    public static VideoFragment newInstance(long videoId) {
        Bundle argz = new Bundle();
        argz.putLong(VIDEO_ID, videoId);
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(argz);
        return fragment;
    }


    @Override
    protected void initViewModel() {
        mVideoViewModel = getFragmentScopeViewModel(VideoViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_recyclerview_normal, BR.vm, mVideoViewModel)
                .addBindingParam(BR.changeListener, stateChangeListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            long videoId = getArguments().getLong(VIDEO_ID);

            VideoAdapter adapter = new VideoAdapter(null);
            adapter.setOnItemClickListener((adapter1, view, position) -> {
                //进入视频详情页面
                VideoBean.VideoEntity entity = (VideoBean.VideoEntity) adapter1.getItem(position);
                VideoDetailActivity.startActivity(getContext(), entity.getData().getVid());
            });
            mVideoViewModel.adpter.set(adapter);

            mVideoViewModel.request.getVideoLiveData().observe(this, videoBean -> {
                //视频数据
                mVideoViewModel.list.set(videoBean.getDatas());
            });

            mVideoViewModel.request.requestVideoTab(videoId);
        }

    }


    RecyclerView.OnChildAttachStateChangeListener stateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(@NonNull View view) {

        }

        @Override
        public void onChildViewDetachedFromWindow(@NonNull View view) {
            Jzvd jzvd = view.findViewById(R.id.videoplayer);

            if (jzvd != null && Jzvd.CURRENT_JZVD != null &&
                    jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                    Jzvd.releaseAllVideos();
                }
            }
        }
    };


    static class VideoAdapter extends BaseQuickAdapter<VideoBean.VideoEntity, BaseViewHolder> {
        private ImageLoaderManager manager = ImageLoaderManager.getInstance();

        VideoAdapter(@Nullable List<VideoBean.VideoEntity> data) {
            super(R.layout.item_home_video, data);
        }

        @SuppressLint("CheckResult")
        @Override
        protected void convert(@NonNull final BaseViewHolder adapter, final VideoBean.VideoEntity item) {
            LinearLayout llVideoState = adapter.getView(R.id.ll_item_video_status);
            //manager.displayImageForCorner((ImageView) adapter.getView(R.id.iv_item_video_cover), item.getData().getCoverurl());
            //播放次数
            adapter.setText(R.id.tv_item_video_play_count, String.valueOf(item.getData().getPlaytime()));
            //播放时长
            adapter.setText(R.id.tv_item_video_play_duration, TimeUtil.getTimeNoYMDH(item.getData().getDurationms()));
            //视频标题
            adapter.setText(R.id.tv_item_video_title, item.getData().getTitle());
            //视频作者 头像
            manager.displayImageForCircle(adapter.getView(R.id.iv_item_video_creator_img), item.getData().getCreator().getAvatarurl());
            //视频作者 用户名
            adapter.setText(R.id.tv_item_video_creator_name, item.getData().getCreator().getNickname());
            //点赞次数
            adapter.setText(R.id.tv_item_video_praised_count, String.valueOf(item.getData().getPraisedcount()));
            //评论次数
            adapter.setText(R.id.tv_item_video_comment_count, String.valueOf(item.getData().getCommentcount()));

            //视频标签
            //adapter.setText(R.id.tv_item_video_tag, String.valueOf(item.getData().getE));

            CustomJzVideoView jzvdStd = adapter.getView(R.id.videoplayer);
            //切换视频相关内容  播放量
            jzvdStd.setListener(new CustomJzVideoView.OnVideoStateChangeListener() {
                @Override
                public void onStatePlaying() {
                    llVideoState.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onStatePause() {
                    llVideoState.setVisibility(View.VISIBLE);
                }
            });
            //视频播放封面
            manager.displayImageForView(jzvdStd.posterImageView, item.getData().getCoverurl());

            ApiEngine.getInstance().getApiService().getVideoUrl(item.getData().getVid())
                    .compose(ApiEngine.getInstance().applySchedulers())
                    .subscribe(videoUrlBean -> jzvdStd.setUp(videoUrlBean.getUrls().get(0).getUrl(), "", Jzvd.SCREEN_NORMAL));

        }
    }

}
