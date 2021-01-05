package com.netease.music.ui.page.video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.data.config.TypeEnum;
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
        mViewModel = getActivityScopeViewModel(VideoDetailViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_video_detail, BR.vm, mViewModel)
                .addBindingParam(BR.proxy, new ClickProxy());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBundleExtra("data") != null) {
            String videoId = getIntent().getBundleExtra("data").getString("videoId");
            //视频详情
            mViewModel.request.getVideoDetailLiveData().observe(this, videoDetailBean -> {
                mViewModel.video.set(videoDetailBean.getData());
                //单独赋值
                mViewModel.isParised.set(videoDetailBean.getData().getPraised());
                mViewModel.isSubscribed.set(videoDetailBean.getData().getSubscribed());
                mViewModel.isFollowed.set(videoDetailBean.getData().getCreator().getFollowed());
            });

            //相关视频
            mViewModel.request.getVideoRelatedLiveData().observe(this, relatedVideoBean -> {

            });
            //视频播放地址 TODO
            mViewModel.request.getVideoUrlLiveData().observe(this, urlBean -> {

            });
            //视频评论 无法禁止滑动 TODO
            mViewModel.request.getVideoCommentLiveData().observe(this, commentBean -> {

                mViewModel.commentAdapter.set(new MultipleSectionGedanCommentAdapter(videoId, TypeEnum.VIDEO_ID, VideoDetailActivity.this, commentBean));

                mViewModel.loadingVisible.set(false);
            });
            //点赞和收藏状态取反
            mViewModel.request.getSubscribeStatusLiveData().observe(this, aBoolean -> {
                mViewModel.isSubscribed.set(!mViewModel.isSubscribed.get());
                showShortToast(mViewModel.isSubscribed.get() ? "收藏成功" : "取消收藏成功");
            });

            mViewModel.request.getLikeStatusLiveData().observe(this, aBoolean -> mViewModel.isParised.set(!mViewModel.isParised.get()));
            //关注去取消关注用户
            mViewModel.request.getFollowStatusLiveData().observe(this, aBoolean -> mViewModel.isFollowed.set(!mViewModel.isFollowed.get()));

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


    public class ClickProxy {
        public void back() {
            finish();
        }

        //点赞
        public void parise() {
            mViewModel.request.requestLikeVideo(mViewModel.video.get().getVid(), !mViewModel.isParised.get());
        }

        //收藏MV
        public void collect() {
            mViewModel.request.requestSubscribeVideo(mViewModel.video.get().getVid(), !mViewModel.isSubscribed.get());
        }

        //关注或取消关注作者
        public void changeSubscribeStatus() {
            mViewModel.request.requestFollowUser(mViewModel.video.get().getCreator().getUserid(), !mViewModel.isFollowed.get());
        }

    }
}
