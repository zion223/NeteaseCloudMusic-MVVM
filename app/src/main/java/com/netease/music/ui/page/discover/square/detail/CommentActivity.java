package com.netease.music.ui.page.discover.square.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.data.config.TypeEnum;
import com.netease.music.ui.page.adapter.MultipleSectionGedanCommentAdapter;
import com.netease.music.ui.state.CommentViewModel;

//评论  音乐评论 歌单评论 专辑评论
public class CommentActivity extends BaseActivity {

    private CommentViewModel mCommentViewModel;

    private static final String TYPE_ID = "TYPE_ID";
    private static final String LIST_ID = "LIST_ID";
    private static final String IMG_ID = "IMG_ID";
    private static final String TOP_TEXT_ID = "TOP_TEXT_ID";
    private static final String BOTTOM_TEXT_ID = "BOTTOM_TEXT_ID";


    public static void startActivity(Context context, String listId, int type, String img, String creator, String title) {
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_ID, type);
        bundle.putString(LIST_ID, listId);
        bundle.putString(IMG_ID, img);
        bundle.putString(TOP_TEXT_ID, creator);
        bundle.putString(BOTTOM_TEXT_ID, title);
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra("data", bundle);
        context.startActivity(intent);
    }


    @Override
    protected void initViewModel() {
        mCommentViewModel = getActivityScopeViewModel(CommentViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_gedan_detail_comment, BR.vm, mCommentViewModel)
                .addBindingParam(BR.proxy, new ClickProxy());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getIntent().getBundleExtra("data");
        if (data != null) {
            mCommentViewModel.type.set(TypeEnum.getTypeByID(data.getInt(TYPE_ID)));
            mCommentViewModel.listId.set(data.getString(LIST_ID));
            mCommentViewModel.imgUrl.set(data.getString(IMG_ID));
            mCommentViewModel.titile.set(data.getString(TOP_TEXT_ID));
            String creator = data.getString(BOTTOM_TEXT_ID);
            if (mCommentViewModel.type.get() == TypeEnum.PLAYLIST) {
                //歌单显示by xxx
                String creatorIndex = "by " + creator;
                SpannableString msp = new SpannableString(creatorIndex);
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.GRAY);
                msp.setSpan(foregroundColorSpan, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mCommentViewModel.createor.set(msp);
            } else {
                mCommentViewModel.createor.set(creator);
            }
        }
        mCommentViewModel.request.getCommentLiveData().observe(this, commentBean -> {
            MultipleSectionGedanCommentAdapter mAdapter = new MultipleSectionGedanCommentAdapter(mCommentViewModel.listId.get(), mCommentViewModel.type.get().getValue(), CommentActivity.this, commentBean);
            mCommentViewModel.adapter.set(mAdapter);

            new Handler().postDelayed(() -> mCommentViewModel.loadingVisible.set(false), 500);
        });

        mCommentViewModel.request.getCommentSizeLiveData().observe(this, size -> {
            mCommentViewModel.commentSize.set(size);

        });
        mCommentViewModel.request.requestCommentData(mCommentViewModel.type.get(), mCommentViewModel.listId.get());
    }


    public class ClickProxy {
        public void back() {
            finish();
        }
    }
}
