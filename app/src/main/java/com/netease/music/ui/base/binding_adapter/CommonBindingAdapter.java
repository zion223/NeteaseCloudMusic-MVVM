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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.imooc.lib_common_ui.widget.CaptchaView;
import com.kunminx.architecture.utils.ClickUtils;
import com.kunminx.architecture.utils.Utils;

import java.util.List;

/**
 * Create by KunMinX at 19/9/18
 */
@SuppressWarnings("unused")
public class CommonBindingAdapter {

    @BindingAdapter(value = {"imageUrl", "placeHolder"}, requireAll = false)
    public static void loadUrl(ImageView view, String url, Drawable placeHolder) {
        Glide.with(view.getContext()).load(url).placeholder(placeHolder).into(view);
    }

    @BindingAdapter(value = {"visible"}, requireAll = false)
    public static void visible(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter(value = {"showDrawable", "drawableShowed"}, requireAll = false)
    public static void showDrawable(ImageView view, boolean showDrawable, int drawableShowed) {
        view.setImageResource(showDrawable ? drawableShowed : android.R.color.transparent);
    }

    @BindingAdapter(value = {"textColor"}, requireAll = false)
    public static void setTextColor(TextView textView, int textColorRes) {
        textView.setTextColor(textView.getResources().getColor(textColorRes));
    }

    @BindingAdapter(value = {"imageRes"}, requireAll = false)
    public static void setImageRes(ImageView imageView, int imageRes) {
        imageView.setImageResource(imageRes);
    }

    @BindingAdapter(value = {"selected"}, requireAll = false)
    public static void selected(View view, boolean select) {
        view.setSelected(select);
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

    @BindingAdapter(value = {"linearAdapter"})
    public static void initRecyclerViewWithLinearLayoutManager(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    @BindingAdapter(value = {"onEditorActionListener"})
    public static void setOnEditorActionListener(EditText editText, TextView.OnEditorActionListener listener) {
        editText.setOnEditorActionListener(listener);
    }

    @BindingAdapter(value = {"submitList"})
    public static void setRecyclerViewData(EditText editText, TextView.OnEditorActionListener listener) {
        editText.setOnEditorActionListener(listener);
    }

    @BindingAdapter(value = {"adapter", "submitList", "autoScrollToTopWhenInsert", "autoScrollToBottomWhenInsert"}, requireAll = false)
    public static void bindList(RecyclerView recyclerView, BaseQuickAdapter adapter, List list,
                                boolean autoScrollToTopWhenInsert, boolean autoScrollToBottomWhenInsert) {

        if (recyclerView != null && list != null) {

            if (recyclerView.getAdapter() == null) {

                recyclerView.setLayoutManager(new LinearLayoutManager(Utils.getApp()));

                recyclerView.setAdapter(adapter);

                adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        if (autoScrollToTopWhenInsert) {
                            recyclerView.scrollToPosition(0);
                        } else if (autoScrollToBottomWhenInsert) {
                            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount());
                        }
                    }
                });
            }

            adapter.addData(list);
        }
    }
}
