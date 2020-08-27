package com.netease.music.ui.page.search;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.lib_api.model.search.HotSearchDetailBean;
import com.netease.lib_image_loader.app.ImageLoaderManager;
import com.netease.music.R;

import java.util.List;


public class HotSearchAdapter extends BaseQuickAdapter<HotSearchDetailBean.DataBean, BaseViewHolder> {

    private ImageLoaderManager manager;

    HotSearchAdapter(@Nullable List<HotSearchDetailBean.DataBean> data) {
        super(R.layout.item_search_suggest, data);
        manager = ImageLoaderManager.getInstance();
    }

    @Override
    protected void convert(BaseViewHolder helper, HotSearchDetailBean.DataBean item) {
        final TextView tvRank = helper.getView(R.id.tv_suggest_rank);
        final ImageView mIvIcon = helper.getView(R.id.iv_suggest_icon);
        final TextView suggestName = helper.getView(R.id.tv_suggest_name);
        if (!TextUtils.isEmpty(item.getIconUrl())) {
            manager.displayImageForView(mIvIcon, item.getIconUrl());
        }
        if (helper.getBindingAdapterPosition() <= 3) {
            suggestName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvRank.setTextColor(Color.parseColor("#FF4500"));
        } else {
            suggestName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tvRank.setTextColor(Color.GRAY);
        }
        //关键字
        helper.setText(R.id.tv_suggest_name, item.getSearchWord());
        //内容
        helper.setText(R.id.tv_suggest_artist, item.getContent());
        //搜索热度
        helper.setText(R.id.tv_suggest_hot, String.valueOf(item.getScore()));
        //排名
        helper.setText(R.id.tv_suggest_rank, String.valueOf(helper.getLayoutPosition()));

    }
}
