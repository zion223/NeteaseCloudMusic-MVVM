/*
 * Copyright 2018-present KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netease.music.ui.base.binding_adapter;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.google.android.material.appbar.AppBarLayout;
import com.kunminx.architecture.utils.ClickUtils;
import com.netease.lib_common_ui.HornizeItemView;
import com.netease.lib_common_ui.bannder.HolderCreator;
import com.netease.lib_common_ui.widget.ArtistSortView;
import com.netease.lib_common_ui.widget.CaptchaView;
import com.netease.music.R;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

/**
 * Create by KunMinX at 19/9/18
 */
@SuppressWarnings("unused")
public class CommonBindingAdapter {

    @BindingAdapter(value = {"visible"}, requireAll = false)
    public static void visible(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter(value = {"textColor"}, requireAll = false)
    public static void setTextColor(TextView textView, int textColorRes) {
        textView.setTextColor(textView.getResources().getColor(textColorRes));
    }

    @BindingAdapter(value = {"selected"}, requireAll = false)
    public static void selected(View view, boolean select) {
        view.setSelected(select);
    }

    @BindingAdapter(value = {"alpha"}, requireAll = false)
    public static void setAlpha(View view, float alpha) {
        view.setAlpha(alpha);
    }

    @BindingAdapter(value = {"leftCompoundDrawable"}, requireAll = false)
    public static void setCompoundDrawables(TextView view, Drawable drawable) {
        view.setCompoundDrawables(drawable, null, null, null);
    }

    @BindingAdapter(value = {"backgroundDrawable"}, requireAll = false)
    public static void setBackGround(View view, Drawable alpha) {
        view.setBackground(alpha);
    }

    @BindingAdapter(value = {"imgalpha"}, requireAll = false)
    public static void setImageViewAlpha(ImageView view, int alpha) {
        view.setImageAlpha(alpha);
    }

    @BindingAdapter(value = {"onClickWithDebouncing"}, requireAll = false)
    public static void onClickWithDebouncing(View view, View.OnClickListener clickListener) {
        ClickUtils.applySingleDebouncing(view, clickListener);
    }

    @BindingAdapter(value = {"adjustWidth"})
    public static void adjustWidth(View view, int adjustWidth) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = adjustWidth;
        view.setLayoutParams(params);
    }

    @BindingAdapter(value = {"adjustHeight"})
    public static void adjustHeight(View view, int adjustHeight) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = adjustHeight;
        view.setLayoutParams(params);
    }

    @BindingAdapter(value = {"initCaptureViewListener"})
    public static void setListener(CaptchaView captchaView, CaptchaView.OnInputListener listener) {
        captchaView.setOnInputListener(listener);
    }

    @BindingAdapter(value = {"showLoadingAnim"})
    public static void showLoadingAnim(ImageView imageView, Boolean show) {
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        if (show) {
            animationDrawable.start();
        } else {
            animationDrawable.stop();
        }
    }

    @BindingAdapter(value = {"onEditorActionListener"})
    public static void setOnEditorActionListener(EditText editText, TextView.OnEditorActionListener listener) {
        editText.setOnEditorActionListener(listener);
    }

    @BindingAdapter(value = {"textChangedListener"})
    public static void addTextChangedListener(EditText editText, TextWatcher listener) {
        editText.addTextChangedListener(listener);
    }


    @BindingAdapter(value = {"viewPagerAdapter"})
    public static void setViewPagerAdapter(ViewPager viewPager, PagerAdapter adapter) {
        viewPager.setAdapter(adapter);
    }

    @BindingAdapter(value = {"currentItem"})
    public static void setCurrentItem(ViewPager viewPager, int item) {
        viewPager.setCurrentItem(item);
    }

    @BindingAdapter(value = {"viewPagerOffsetLimit"})
    public static void setViewPagerAdapter(ViewPager viewPager, int limit) {
        viewPager.setOffscreenPageLimit(limit);
    }

    @BindingAdapter(value = {"onSmartRefreshListener"}, requireAll = false)
    public static void setOffsetChangeListener(SmartRefreshLayout refreshLayout, OnRefreshListener listener) {
        refreshLayout.setOnRefreshListener(listener);
    }

    @BindingAdapter(value = {"onSmartLoadMoreListener"}, requireAll = false)
    public static void setOnLoadMoreListener(SmartRefreshLayout refreshLayout, OnLoadMoreListener listener) {
        refreshLayout.setOnLoadMoreListener(listener);
    }

    @BindingAdapter(value = {"onOffsetChangedListener"}, requireAll = false)
    public static void addOnOffsetChangedListener(AppBarLayout appBarLayout, AppBarLayout.OnOffsetChangedListener listener) {
        appBarLayout.addOnOffsetChangedListener(listener);
    }

    @BindingAdapter(value = {"bannerPic", "bannerListener"}, requireAll = false)
    public static void setDefault(ConvenientBanner<String> convenientBanner,
                                  List<String> banners,
                                  OnItemClickListener listener) {
        convenientBanner.setPages(new HolderCreator(), banners)
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setPageTransformer(new DefaultTransformer())
                .setOnItemClickListener(listener)
                .setCanLoop(true);
    }

    @BindingAdapter(value = "onChooseAreaListener")
    public static void setOnChooseListener(ArtistSortView sortView, ArtistSortView.OnChooseArtistSortListener listener) {
        sortView.setOnChooseArtistSortListener(listener);
    }

    //HornizeItemView 相关的BindingAdapter
    @BindingAdapter(value = "hornizeItemViewTileText")
    public static void setHornizeItemViewTileText(HornizeItemView view, CharSequence text) {
        view.getTileView().setText(text);
    }

    @BindingAdapter(value = "hornizeItemViewRightText")
    public static void setHornizeItemViewRightText(HornizeItemView view, CharSequence text) {
        view.getRightTextView().setText(text);
    }

    @BindingAdapter(value = "onClickHornizeItemView")
    public static void setOnClickHornizeItemView(HornizeItemView view, View.OnClickListener listener) {
        view.setOnClickListener(listener);
    }


}
