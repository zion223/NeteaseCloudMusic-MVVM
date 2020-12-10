package com.netease.music.ui.page.discover.radio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.lib_api.model.dj.DjDetailBean;
import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.utils.BarUtils;
import com.netease.lib_image_loader.app.ImageLoaderManager;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.databinding.ItemRadioProgramHeaderBinding;
import com.netease.music.ui.page.adapter.RadioProgramAdapter;
import com.netease.music.ui.page.discover.user.UserDetailActivity;
import com.netease.music.ui.state.RadioDetailViewModel;

import java.util.List;

import static com.netease.lib_audio.app.AudioHelper.getContext;

//电台详情
public class RadioDetailActivity extends BaseActivity {


    private RadioDetailViewModel mRadioDetailViewModel;

    public static void startActivity(Context context, String radioId) {
        Bundle argz = new Bundle();
        argz.putString("radioId", radioId);
        Intent intent = new Intent(context, RadioDetailActivity.class);
        intent.putExtra("data", argz);
        context.startActivity(intent);
    }

    @Override
    protected void initViewModel() {
        mRadioDetailViewModel = getActivityScopeViewModel(RadioDetailViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_radio_deatail, BR.vm, mRadioDetailViewModel)
                .addBindingParam(BR.proxy, new ClickProxy());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
        if (getIntent().getBundleExtra("data") != null) {
            String radioId = getIntent().getBundleExtra("data").getString("radioId");
            mRadioDetailViewModel.radioId.set(radioId);
            //电台详情  电台热评详情
            mRadioDetailViewModel.request.getRadioDetailLiveData().observe(this, djDetailBean -> {
                mRadioDetailViewModel.radio.set(djDetailBean.getData());
                //是否已订阅
                mRadioDetailViewModel.isSub.set(djDetailBean.getData().isSubed());
                final CharSequence[] mTitleDataList = new CharSequence[2];
                mTitleDataList[0] = "详情";
                SpannableString msp = new SpannableString("节目" + djDetailBean.getData().getProgramCount());
                msp.setSpan(new AbsoluteSizeSpan(30), 2, msp.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mTitleDataList[1] = msp;
                mRadioDetailViewModel.indicatorTitle.set(mTitleDataList);

                //热门评论
                final RadioHotCommentAdapter radioHotCommentAdapter = new RadioHotCommentAdapter(RadioDetailActivity.this, djDetailBean.getData().getCommentDatas());
                radioHotCommentAdapter.addHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.item_gedan_comment_header, null, false));
                mRadioDetailViewModel.hotCommentAdapter.set(radioHotCommentAdapter);
            });

            //电台节目

            mRadioDetailViewModel.request.getRadioProgramLiveData().observe(this, djProgramBean -> {
                mRadioDetailViewModel.programList.set(djProgramBean.getPrograms());
                //节目头布局
                ItemRadioProgramHeaderBinding inflate = ItemRadioProgramHeaderBinding.inflate(LayoutInflater.from(this));
                inflate.setLifecycleOwner(this);
                inflate.setVariable(BR.count, djProgramBean.getCount());
                inflate.setVariable(BR.asc, mRadioDetailViewModel.asc.get());
                inflate.setVariable(BR.proxy, new ClickProxy());
                View programHeaderView = inflate.getRoot();
                //新 -> 老  排序序号
                RadioProgramAdapter radioProgramAdapter;
                radioProgramAdapter = new RadioProgramAdapter(mRadioDetailViewModel.asc.get() ? 0 : djProgramBean.getCount(), djProgramBean.getPrograms());
                radioProgramAdapter.setHeaderView(programHeaderView);
                mRadioDetailViewModel.programAdapter.set(radioProgramAdapter);

            });

            mRadioDetailViewModel.request.getRadioSubLiveData().observe(this, djsubBean -> {
                if (djsubBean.getCode() == 200) {
                    //订阅或取消订阅成功
                    mRadioDetailViewModel.isSub.set(!mRadioDetailViewModel.isSub.get());
                }
            });

            //电台详情
            mRadioDetailViewModel.request.requestRadioDeatil(radioId);
            //节目排序方式
            mRadioDetailViewModel.request.requestRadioProgram(radioId, mRadioDetailViewModel.asc.get());
        }

    }

    //取消订阅对话框  TODO Context.getResources() on a null object reference
    //UnsubscribeRadioDialog dialog = new UnsubscribeRadioDialog(RadioDetailActivity.this, () -> mRadioDetailViewModel.request.requestSubRadio(mRadioDetailViewModel.radioId.get(), !mRadioDetailViewModel.isSub.get()));

    public class ClickProxy {
        //改变排序
        public void changeProgramAsc() {
            //排序状态取反
            mRadioDetailViewModel.asc.set(!mRadioDetailViewModel.asc.get());

            mRadioDetailViewModel.request.requestRadioProgram(mRadioDetailViewModel.radioId.get(), mRadioDetailViewModel.asc.get());
        }

        public void back() {
            finish();
        }

        //订阅或取消订阅
        public void changeSubStatus() {
//            if(mRadioDetailViewModel.isSub.get()){
//                //取消订阅时
//                new XPopup.Builder(getBaseContext())
//                        .asCustom(dialog)
//                        .show();
//            }else{
            //状态取反
            mRadioDetailViewModel.request.requestSubRadio(mRadioDetailViewModel.radioId.get(), !mRadioDetailViewModel.isSub.get());
            //}
        }


        public void userInfo() {
            UserDetailActivity.startActivity(RadioDetailActivity.this, mRadioDetailViewModel.radio.get().getDj().getUserId());
        }
    }


    static class RadioHotCommentAdapter extends BaseQuickAdapter<DjDetailBean.DjRadioBean.CommentDatasBean, BaseViewHolder> {

        RadioHotCommentAdapter(Context context, @Nullable List<DjDetailBean.DjRadioBean.CommentDatasBean> data) {
            super(R.layout.item_radio_hot_comment, data);
            addChildClickViewIds(R.id.tv_item_hot_comment_avatar_name, R.id.iv_item_hot_comment_avatar_img);
            setOnItemChildClickListener((adapter, view, position) -> {
                DjDetailBean.DjRadioBean.CommentDatasBean entity = (DjDetailBean.DjRadioBean.CommentDatasBean) adapter.getItem(position);
                if (view.getId() == R.id.tv_item_hot_comment_avatar_name || view.getId() == R.id.iv_item_hot_comment_avatar_img) {
                    UserDetailActivity.startActivity(context, Long.parseLong(entity.getUserProfile().getUserId()));
                }
            });
        }


        @Override
        protected void convert(BaseViewHolder helper, DjDetailBean.DjRadioBean.CommentDatasBean item) {
            //评论者昵称
            helper.setText(R.id.tv_item_hot_comment_avatar_name, item.getUserProfile().getNickname());
            //评论者头像
            ImageLoaderManager.getInstance().displayImageForCircle(helper.getView(R.id.iv_item_hot_comment_avatar_img), item.getUserProfile().getAvatarUrl());
            //评论内容
            helper.setText(R.id.tv_item_hot_comment_content, item.getContent());
            //评论来源
            helper.setText(R.id.tv_item_hot_comment_from, item.getProgramName());
        }
    }
}
