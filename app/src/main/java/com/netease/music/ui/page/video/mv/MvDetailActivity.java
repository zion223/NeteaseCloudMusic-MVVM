package com.netease.music.ui.page.video.mv;

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
import com.netease.music.ui.state.MvDetailViewModel;

public class MvDetailActivity extends BaseActivity {


    private MvDetailViewModel mMvDetailViewModel;

    public static void startActivity(Context context, String videoId, String artistId) {
        Bundle argz = new Bundle();
        argz.putString("mvId", videoId);
        argz.putString("artistId", artistId);
        Intent intent = new Intent(context, MvDetailActivity.class);
        intent.putExtra("data", argz);
        context.startActivity(intent);
    }


    @Override
    protected void initViewModel() {
        mMvDetailViewModel = getActivityScopeViewModel(MvDetailViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_mv_detail, BR.vm, mMvDetailViewModel)
                .addBindingParam(BR.proxy, new ClickProxy());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBundleExtra("data") != null) {
            String mvId = getIntent().getBundleExtra("data").getString("mvId");
            String artistId = getIntent().getBundleExtra("data").getString("artistId");

            mMvDetailViewModel.request.getMvDetailLiveData().observe(this, mvDetailBean -> {
                mMvDetailViewModel.mvData.set(mvDetailBean);
            });

            mMvDetailViewModel.request.getArtistInfoLiveData().observe(this, artistInfo -> {
                mMvDetailViewModel.artistName.set(artistInfo.getArtist().getName());
                mMvDetailViewModel.artistPic.set(artistInfo.getArtist().getImg1v1Url());
                mMvDetailViewModel.isFollowed.set(artistInfo.getArtist().isFollowed());
            });

            mMvDetailViewModel.request.getMvCommentLiveData().observe(this, commentBean -> {

                mMvDetailViewModel.commentAdapter.set(new MultipleSectionGedanCommentAdapter(mvId, TypeEnum.MV_ID, MvDetailActivity.this, commentBean));

                mMvDetailViewModel.loadingVisible.set(false);
            });
            //点赞数据
            mMvDetailViewModel.request.getChangeLikeStatusLiveData().observe(this, aBoolean -> {
                if (aBoolean) {
                    //状态取反
                    mMvDetailViewModel.isParised.set(!mMvDetailViewModel.isParised.get());
                }
            });

            mMvDetailViewModel.request.requestMvDetail(mvId);
            mMvDetailViewModel.request.requestArtistInfo(artistId);
            mMvDetailViewModel.request.requestMvComment(mvId);
        }
    }

    public class ClickProxy {

        public void back() {
            finish();
        }

        //TODO 关注歌手 收藏MV

        public void subArtist() {

        }

        public void parise() {
            mMvDetailViewModel.request.requestLikeMv(mMvDetailViewModel.mvData.get().getId(), !mMvDetailViewModel.isParised.get());
        }

        public void collectMv() {

        }
    }
}
