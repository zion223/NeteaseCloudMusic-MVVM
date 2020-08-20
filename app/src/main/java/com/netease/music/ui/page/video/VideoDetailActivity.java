package com.netease.music.ui.page.video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.data.config.TYPE;
import com.netease.music.ui.page.adapter.MultipleSectionGedanCommentAdapter;
import com.netease.music.ui.state.VideoDetailViewModel;

public class VideoDetailActivity extends BaseActivity {

    private VideoDetailViewModel mViewModel;

    public static void startActivity(Context context, String videoId) {
        Bundle argz = new Bundle();
        argz.putString("videoId", videoId);
        Intent intent = new Intent(context, VideoDetailActivity.class);
        intent.putExtra("data", argz);
        context.startActivity(intent);
    }

    @Override
    protected void initViewModel() {
        mViewModel = getActivityViewModel(VideoDetailViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_video_detail, BR.vm, mViewModel);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBundleExtra("data") != null) {
            String videoId = getIntent().getBundleExtra("data").getString("videoId");
            //视频详情
            mViewModel.request.getVideoDetailLiveData().observe(this, videoDetailBean -> mViewModel.video.set(videoDetailBean.getData()));

            //相关视频
            mViewModel.request.getVideoRelatedLiveData().observe(this, relatedVideoBean -> {

            });
            //视频播放地址
            mViewModel.request.getVideoUrlLiveData().observe(this, urlBean -> {

            });
            mViewModel.request.getVideoCommentLiveData().observe(this, commentBean -> {

                MultipleSectionGedanCommentAdapter commentAdapter = new MultipleSectionGedanCommentAdapter(videoId, TYPE.VIDEO_ID, this.getBaseContext(), commentBean);
                mViewModel.commentAdapter.set(commentAdapter);

                mViewModel.loadingVisible.set(false);
            });

            //视频详情
            mViewModel.request.requestVideoDeatail(videoId);
            //视频播放地址
            mViewModel.request.requestVideoUrl(videoId);
            //相关视频
            mViewModel.request.requestRelatedVideo(videoId);
            //视频评论
            mViewModel.request.requestVideoComment(videoId);

        }
    }
}
