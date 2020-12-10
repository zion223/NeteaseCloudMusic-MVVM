package com.netease.music.ui.page.mine.radio;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.netease.lib_api.model.dj.DjSubListBean;
import com.netease.lib_image_loader.app.ImageLoaderManager;
import com.netease.music.BR;
import com.netease.music.R;
import com.netease.music.ui.page.discover.radio.RadioDetailActivity;
import com.netease.music.ui.state.MyRadioViewModel;

import java.util.List;

public class MyRadioActivity extends BaseActivity {

    private MyRadioViewModel myRadioViewModel;


    @Override
    protected void initViewModel() {
        myRadioViewModel = getActivityScopeViewModel(MyRadioViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.delegate_mine_radio, BR.vm, myRadioViewModel)
                .addBindingParam(BR.subAdapter, new MineRadioAdapter(null))
                .addBindingParam(BR.proxy, new ClickProxy());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myRadioViewModel.request.getDjSubListLiveData().observe(this, djSubListBean -> {
            myRadioViewModel.subRadioList.set(djSubListBean.getDjRadios());
            myRadioViewModel.subRadioSize.set(djSubListBean.getDjRadios().size());
        });

        myRadioViewModel.request.requestSubList();
    }

    public class ClickProxy {
        public void back() {
            finish();
        }
    }

    class MineRadioAdapter extends BaseQuickAdapter<DjSubListBean.DjRadios, BaseViewHolder> {

        private ImageLoaderManager manager;

        MineRadioAdapter(@Nullable List<DjSubListBean.DjRadios> data) {
            super(R.layout.item_mine_radio_content, data);
            manager = ImageLoaderManager.getInstance();
            setOnItemClickListener((adapter, view, position) -> {
                DjSubListBean.DjRadios entity = (DjSubListBean.DjRadios) adapter.getItem(position);
                RadioDetailActivity.startActivity(MyRadioActivity.this, String.valueOf(entity.getId()));
            });
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, DjSubListBean.DjRadios bean) {
            ImageView img = helper.getView(R.id.iv_item_radio_content_img);
            manager.displayImageForCorner(img, bean.getPicUrl(), 5);
            helper.setText(R.id.tv_item_radio_content_name, bean.getName());
            helper.setText(R.id.tv_item_radio_content_creator, "by " + bean.getDj().getNickname());
            helper.setText(R.id.tv_item_radio_content_last_program, bean.getLastProgramName());
        }
    }
}
