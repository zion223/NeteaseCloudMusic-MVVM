package com.netease.music.ui.page.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.lib_api.model.dj.DjProgramBean;
import com.netease.music.R;
import com.netease.music.util.SearchUtil;
import com.netease.music.util.TimeUtil;

import java.util.List;

public class RadioProgramAdapter extends BaseQuickAdapter<DjProgramBean.ProgramsBean, BaseViewHolder> {

    private int totalSize = 0;

    public RadioProgramAdapter(int size, @Nullable List<DjProgramBean.ProgramsBean> data) {
        super(R.layout.item_radio_program, data);
        this.totalSize = size;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder adapter, DjProgramBean.ProgramsBean bean) {
        //节目名称
        adapter.setText(R.id.item_radio_program_toptext, bean.getName());
        //编号
        if (totalSize != 0) {
            adapter.setText(R.id.item_radio_program_no, String.valueOf(totalSize - adapter.getLayoutPosition() + 1));
        } else {
            adapter.setText(R.id.item_radio_program_no, String.valueOf(adapter.getLayoutPosition()));
        }
        //创建时间
        adapter.setText(R.id.item_radio_program_createtime, TimeUtil.getTimeStandardOnlyYMD(bean.getCreateTime()));
        //播放数量
        int listenerCount = bean.getListenerCount();
        //播放数量
        adapter.setText(R.id.item_radio_program_playnum, SearchUtil.getCorresPondingString(listenerCount));
        //节目时间
        adapter.setText(R.id.item_radio_program_duration, TimeUtil.getTimeNoYMDH(bean.getDuration()));
    }
}
