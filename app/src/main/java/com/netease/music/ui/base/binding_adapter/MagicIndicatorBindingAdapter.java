package com.netease.music.ui.base.binding_adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;

import androidx.databinding.BindingAdapter;
import androidx.viewpager.widget.ViewPager;

import com.kunminx.architecture.utils.Utils;
import com.netease.music.R;
import com.netease.music.data.config.CHANNEL;
import com.netease.music.ui.view.ScaleTransitionPagerTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

public class MagicIndicatorBindingAdapter {

    //主界面 初始化TabLayout
    @BindingAdapter(value = {"initHomeMagicIndicator"}, requireAll = false)
    public static void bindHomeMagicIndocator(MagicIndicator magicIndicator, CHANNEL[] channels) {
        magicIndicator.setBackgroundColor(Color.WHITE);

        View rootView = magicIndicator.getRootView();
        ViewPager viewPager = rootView.findViewById(R.id.view_pager);
        CommonNavigator commonNavigator = new CommonNavigator(Utils.getApp());
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return channels.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                final SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(channels[index].getKey());
                simplePagerTitleView.setTextSize(19);
                simplePagerTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                simplePagerTitleView.setNormalColor(Color.parseColor("#999999"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#333333"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                return 1.0f;
            }
        });
        magicIndicator.setNavigator(commonNavigator);

        ViewPagerHelper.bind(magicIndicator, viewPager);
        viewPager.setCurrentItem(1);
    }
}
