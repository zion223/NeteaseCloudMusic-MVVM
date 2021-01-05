package com.netease.music.ui.page.discover.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;

import androidx.annotation.Nullable;

import com.netease.lib_common_ui.utils.SpanUtil;
import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.data.config.TypeEnum;
import com.netease.music.ui.page.adapter.UserHomePagePlayListAdapter;
import com.netease.music.ui.page.cloud.EventAdapter;
import com.netease.music.ui.page.discover.square.detail.SongListDetailActivity;
import com.netease.music.ui.state.UserDetailViewModel;

public class UserDetailActivity extends BaseActivity {

    private UserDetailViewModel mUserDetailViewModel;

    public static void startActivity(Context context, long userId) {
        Bundle argz = new Bundle();
        argz.putLong("userId", userId);
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra("data", argz);
        context.startActivity(intent);
    }

    @Override
    protected void initViewModel() {
        mUserDetailViewModel = getActivityScopeViewModel(UserDetailViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_user_detail, BR.vm, mUserDetailViewModel)
                .addBindingParam(BR.proxy, new ClickProxy());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBundleExtra("data") != null) {
            long userId = getIntent().getBundleExtra("data").getLong("userId");
            mUserDetailViewModel.userId.set(userId);

            mUserDetailViewModel.request.getUserFollowLiveData().observe(this, followBean -> {
                mUserDetailViewModel.followed.set(!mUserDetailViewModel.followed.get());
                //关注成功则提示一下
                if (mUserDetailViewModel.followed.get()) {
                    showShortToast(followBean.getFollowContent());
                }
            });
            //用户详情  TODO 解析省份和市 https://blog.csdn.net/u014686875/article/details/78489668
            mUserDetailViewModel.request.getUserDeatailLiveData().observe(this, userDetailBean -> {
                mUserDetailViewModel.user.set(userDetailBean);
                mUserDetailViewModel.followed.set(userDetailBean.getProfile().isFollowed());
                //动态数量
                SpannableString eventText = userDetailBean.getProfile().getEventCount() > 10000 ? new SpannableString("动态 99+") : new SpannableString("动态 " + userDetailBean.getProfile().getEventCount());
                CharSequence[] mTitleDataList = new CharSequence[2];
                mTitleDataList[0] = "主页";
                mTitleDataList[1] = SpanUtil.newInstance().appendText(eventText)
                        .setFlag(Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        .setFontSize(35, 3, eventText.toString().length())
                        .setFontColor(Color.GRAY, 3, eventText.toString().length())
                        .getBuilder();
                mUserDetailViewModel.indicatorTitle.set(mTitleDataList);
            });
            //用户动态
            mUserDetailViewModel.request.getUserEventLiveData().observe(this, userEventBean -> mUserDetailViewModel.eventAdapter.set(new EventAdapter(UserDetailActivity.this, userEventBean.getEvents())));
            //歌单
            mUserDetailViewModel.request.getUserPlayListLiveData().observe(this, playList -> {
                mUserDetailViewModel.playListAdapter.set(new UserHomePagePlayListAdapter(UserDetailActivity.this, playList));
            });
            //喜欢的歌单
            mUserDetailViewModel.request.getUserLikePlayListLiveData().observe(this, playList -> mUserDetailViewModel.likePlayList.set(playList));

            //获取用户详情
            mUserDetailViewModel.request.requestUserDetail(mUserDetailViewModel.userId.get());
            //用户动态
            mUserDetailViewModel.request.requestUserEvent(mUserDetailViewModel.userId.get());
            //用户歌单
            mUserDetailViewModel.request.requestUserPlaylist(mUserDetailViewModel.userId.get());
        }
    }


    public class ClickProxy {
        public void back() {
            finish();
        }

        //关注或者取消关注
        public void changefollowStatus() {
            mUserDetailViewModel.request.requestUserFollow(mUserDetailViewModel.userId.get(), !mUserDetailViewModel.followed.get());
        }


        //查看用户喜欢的歌单
        public void userLikePlaylist() {
            SongListDetailActivity.startActivity(UserDetailActivity.this, TypeEnum.PLAYLIST_ID, mUserDetailViewModel.likePlayList.get().getId(), "");
        }
    }
}
