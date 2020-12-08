package com.netease.music.ui.page.search;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.state.SearchResultViewModel;

public class SearchResultActivity extends BaseActivity {

    private SearchResultViewModel mSearchResultViewModel;

    @Override
    protected void initViewModel() {
        mSearchResultViewModel = getActivityScopeViewModel(SearchResultViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_search_result, BR.vm, mSearchResultViewModel);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
