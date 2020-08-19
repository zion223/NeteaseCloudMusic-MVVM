package com.netease.music.ui.base.binding_adapter;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kunminx.architecture.ui.adapter.SimpleDataBindingAdapter;
import com.kunminx.architecture.utils.Utils;

import java.util.List;

public class RecyclerViewBindingAdapter {


    @BindingAdapter(value = {"linearAdapter"})
    public static void initRecyclerViewWithLinearLayoutManager(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    @BindingAdapter(value = {"gridAdapter", "spanCount"}, requireAll = false)
    public static void initRecyclerViewWithGridLayoutManager(RecyclerView recyclerView, RecyclerView.Adapter adapter, int count) {
        if (count == 0) count = 3;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), count));
    }

    @BindingAdapter(value = {"submitList"}, requireAll = false)
    public static void initRecyclerViewData(RecyclerView recyclerView, List data) {
        if (recyclerView.getAdapter() instanceof ListAdapter) {
            ((ListAdapter) recyclerView.getAdapter()).submitList(data);
        } else if (recyclerView.getAdapter() instanceof BaseQuickAdapter) {
            ((BaseQuickAdapter) recyclerView.getAdapter()).replaceData(data);
        }
    }

    @BindingAdapter(value = {"addList"}, requireAll = false)
    public static void initRecyclerAddData(RecyclerView recyclerView, List data) {
        if (data != null) {
            if (recyclerView.getAdapter() instanceof ListAdapter) {
                ((ListAdapter) recyclerView.getAdapter()).submitList(data);
            } else if (recyclerView.getAdapter() instanceof BaseQuickAdapter) {
                ((BaseQuickAdapter) recyclerView.getAdapter()).addData(data);
            }
        }
    }

//    @BindingAdapter(value = {"adapter", "submitList", "autoScrollToTopWhenInsert", "autoScrollToBottomWhenInsert"}, requireAll = false)
//    public static void bindList(RecyclerView recyclerView, SimpleDataBindingAdapter adapter, List list,
//                                boolean autoScrollToTopWhenInsert, boolean autoScrollToBottomWhenInsert) {
//
//        if (recyclerView != null && list != null) {
//
//            if (recyclerView.getAdapter() == null) {
//
//                recyclerView.setLayoutManager(new LinearLayoutManager(Utils.getApp()));
//
//                recyclerView.setAdapter(adapter);
//
//                adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//                    @Override
//                    public void onItemRangeInserted(int positionStart, int itemCount) {
//                        if (autoScrollToTopWhenInsert) {
//                            recyclerView.scrollToPosition(0);
//                        } else if (autoScrollToBottomWhenInsert) {
//                            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount());
//                        }
//                    }
//                });
//            }
//
//            adapter.submitList(list);
//        }
//    }
}
