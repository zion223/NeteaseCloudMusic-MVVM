package com.netease.music.ui.page.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.navigation.ActivityNavigator;

import com.netease.lib_api.model.search.HotSearchDetailBean;
import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.utils.Utils;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.helper.DrawerCoordinateHelper;
import com.netease.music.ui.page.search.artist.ArtistSortActivity;
import com.netease.music.ui.state.SearchViewModel;

public class SearchActivity extends BaseActivity {

    private SearchViewModel mSearchViewModel;

    @Override
    protected void initViewModel() {
        mSearchViewModel = getActivityScopeViewModel(SearchViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.fragment_search, BR.vm, mSearchViewModel)
                .addBindingParam(BR.click, new ClickProxy())
                .addBindingParam(BR.listener, listener);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLifecycle().addObserver(DrawerCoordinateHelper.getInstance());

        mSearchViewModel.searchRequest.getHotSearchLiveData().observe(this, hotSearchDetailBean -> {

            HotSearchAdapter hotSearchAdapter = new HotSearchAdapter(hotSearchDetailBean.getData());
            hotSearchAdapter.setHeaderView(LayoutInflater.from(Utils.getApp()).inflate(R.layout.header_suggest, null));
            hotSearchAdapter.setOnItemClickListener((adapter, view, position) -> {
                HotSearchDetailBean.DataBean entity = (HotSearchDetailBean.DataBean) adapter.getItem(position);
                String searchword = entity.getSearchWord();
                //启动搜索详情页面
                //getSupportDelegate().start(SearchResultDelegate.newInstance(searchword));
            });
            mSearchViewModel.adapter.set(hotSearchAdapter);
        });

        mSearchViewModel.searchRequest.requestHotSearch();
    }

    @Override
    public void finish() {
        super.finish();
        ActivityNavigator.applyPopAnimationsToPendingTransition(this);
    }

    //输入框监听回车按键
    TextView.OnEditorActionListener listener = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            //打开搜索结果Activity

            //getParentDelegate().getSupportDelegate().start(SearchResultDelegate.newInstance(mEtKeywords.getText().toString()));
            //隐藏软键盘
            toggleSoftInput();
        }
        return false;
    };


    public class ClickProxy {

        //返回
        public void back() {
            finish();
        }

        //打开歌手分类页面
        public void navToArtist() {
            startActivity(new Intent(SearchActivity.this, ArtistSortActivity.class));
        }

    }
}
