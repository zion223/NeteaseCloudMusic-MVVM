package com.netease.lib_common_ui.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.netease.lib_common_ui.R;
import com.netease.lib_common_ui.utils.SharePreferenceUtil;

import java.util.HashMap;
import java.util.Map;

public class TimerOffDialog extends CenterPopupView implements View.OnClickListener {

    private OnSelectTimerListener listener;
    private HashMap<Integer, Integer> checkHashmap = new HashMap<>();
    private int originTime;
    private int time;
    private SharePreferenceUtil preferenceUtil;

    public TimerOffDialog(@NonNull Context context, OnSelectTimerListener listener) {
        super(context);
        this.listener = listener;
        preferenceUtil = SharePreferenceUtil.getInstance(context);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        time = preferenceUtil.getStopAudioTime();
        originTime = time;
        for (Map.Entry<Integer, Integer> entry : checkHashmap.entrySet()) {
            if (time == entry.getKey()) {
                findViewById(entry.getValue()).setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    private void initView() {
        checkHashmap.put(0, R.id.iv_notimer_check);
        checkHashmap.put(10, R.id.iv_10min_check);
        checkHashmap.put(20, R.id.iv_20min_check);
        checkHashmap.put(30, R.id.iv_30min_check);
        checkHashmap.put(45, R.id.iv_45min_check);
        checkHashmap.put(60, R.id.iv_60min_check);
        findViewById(R.id.ll_notimer).setOnClickListener(this);
        findViewById(R.id.ll_10timer).setOnClickListener(this);
        findViewById(R.id.ll_20timer).setOnClickListener(this);
        findViewById(R.id.ll_30timer).setOnClickListener(this);
        findViewById(R.id.ll_45timer).setOnClickListener(this);
        findViewById(R.id.ll_60timer).setOnClickListener(this);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_timer_off;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_notimer) {
            time = 0;
        } else if (id == R.id.ll_10timer) {
            time = 10;
        } else if (id == R.id.ll_20timer) {
            time = 20;
        } else if (id == R.id.ll_30timer) {
            time = 30;
        } else if (id == R.id.ll_45timer) {
            time = 45;
        } else if (id == R.id.ll_60timer) {
            time = 60;
        }
        for (Map.Entry<Integer, Integer> entry : checkHashmap.entrySet()) {
            findViewById(entry.getValue()).setVisibility(time == entry.getKey() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (originTime != time) {
            preferenceUtil.setStopAudioTime(time);
            listener.onSelect(time);
        }
    }

    public interface OnSelectTimerListener {
        void onSelect(int time);
    }
}
