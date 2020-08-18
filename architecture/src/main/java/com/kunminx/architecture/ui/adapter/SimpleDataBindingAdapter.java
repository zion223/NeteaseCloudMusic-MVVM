package com.kunminx.architecture.ui.adapter;

import android.content.Context;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;

public abstract class SimpleDataBindingAdapter<M, B extends ViewDataBinding> extends BaseDataBindingAdapter<M, B> {

    private final int layout;

    public SimpleDataBindingAdapter(Context context, int layout, @NonNull DiffUtil.ItemCallback<M> diffCallback) {
        super(context, diffCallback);
        this.layout = layout;
    }

    @Override
    protected @LayoutRes
    int getLayoutResId(int viewType) {
        return this.layout;
    }

}
