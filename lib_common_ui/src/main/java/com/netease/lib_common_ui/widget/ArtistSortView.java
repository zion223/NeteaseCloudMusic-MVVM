package com.netease.lib_common_ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.netease.lib_common_ui.R;


public class ArtistSortView extends LinearLayout implements View.OnClickListener {


	private OnChooseArtistSortListener listener;

	//area
	private int areaPosition = -1;
	//type
	private int typePosition = -1;

	private final TextView[] mTopTextViewArray = new TextView[5];
	private final TextView[] mBottomTextViewArray = new TextView[4];

	TextView mTvSortChina;
	TextView mTvSortWest;
	TextView mTvSortJapan;
	TextView mTvSortKorea;
	TextView mTvSortOther;
	TextView mTvSortMale;
	TextView mTvSortFemale;
	TextView mTvSortGroup;

	public ArtistSortView(Context context) {
		super(context);
		View sortView = LayoutInflater.from(context).inflate(R.layout.sort_view, this);
		initView(sortView);
	}

	public ArtistSortView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		View sortView = LayoutInflater.from(context).inflate(R.layout.sort_view, this);
		initView(sortView);
	}

	private void initView(View sortView) {
		mTvSortChina = sortView.findViewById(R.id.tv_artist_sort_china);
		mTvSortWest = sortView.findViewById(R.id.tv_artist_sort_west);
		mTvSortJapan = sortView.findViewById(R.id.tv_artist_sort_japan);
		mTvSortKorea = sortView.findViewById(R.id.tv_artist_sort_korea);
		mTvSortOther = sortView.findViewById(R.id.tv_artist_sort_other);

		mTvSortMale = sortView.findViewById(R.id.tv_artist_sort_male);
		mTvSortFemale = sortView.findViewById(R.id.tv_artist_sort_female);
		mTvSortGroup = sortView.findViewById(R.id.tv_artist_sort_group);
		//setOnClickListener
		mTvSortChina.setOnClickListener(this);
		mTvSortWest.setOnClickListener(this);
		mTvSortJapan.setOnClickListener(this);
		mTvSortKorea.setOnClickListener(this);
		mTvSortOther.setOnClickListener(this);
		mTvSortMale.setOnClickListener(this);
		mTvSortFemale.setOnClickListener(this);
		mTvSortGroup.setOnClickListener(this);
		//init data
		//area
		mTvSortChina.setTag(1);
		mTvSortWest.setTag(2);
		mTvSortJapan.setTag(3);
		mTvSortKorea.setTag(4);
		mTvSortOther.setTag(0);
		//type
		mTvSortMale.setTag(1);
		mTvSortFemale.setTag(2);
		mTvSortGroup.setTag(3);
		mTopTextViewArray[1] = mTvSortChina;
		mTopTextViewArray[2] = mTvSortWest;
		mTopTextViewArray[3] = mTvSortJapan;
		mTopTextViewArray[4] = mTvSortKorea;
		mTopTextViewArray[0] = mTvSortOther;
		mBottomTextViewArray[1] = mTvSortMale;
		mBottomTextViewArray[2] = mTvSortFemale;
		mBottomTextViewArray[3] = mTvSortGroup;
	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.tv_artist_sort_china || id == R.id.tv_artist_sort_west
				|| id == R.id.tv_artist_sort_japan || id == R.id.tv_artist_sort_korea || id == R.id.tv_artist_sort_other) {
			//顶部数据
			int top = (int) v.getTag();
			areaPosition = top;
			mTopTextViewArray[top].setTextColor(Color.RED);
			for (int i = 0; i < mTopTextViewArray.length; i++) {
				if (mTopTextViewArray[i] != null && i != areaPosition) {
					mTopTextViewArray[i].setTextColor(Color.GRAY);
				}
			}
			if (typePosition == -1) {
				//初始点击时
				typePosition = 1;
				mBottomTextViewArray[typePosition].setTextColor(Color.RED);
			}
			//回调接口
			int area = areaPosition;
			if (area == 1) {
				area = 7;
			} else if (area == 2) {
				area = 96;
			} else if (area == 3) {
				area = 8;
			} else if (area == 4) {
				area = 16;
			}
			listener.onChoose(typePosition, area);

		} else {
			//底部数据
			int bottom = (int) v.getTag();
			typePosition = bottom;
			mBottomTextViewArray[bottom].setTextColor(Color.RED);
			for (int i = 0; i < mBottomTextViewArray.length; i++) {
				if (mBottomTextViewArray[i] != null && i != typePosition) {
					mBottomTextViewArray[i].setTextColor(Color.GRAY);
				}
			}
			if (areaPosition == -1) {
				//初始点击时
				areaPosition = 1;
				mTopTextViewArray[areaPosition].setTextColor(Color.RED);
			}
			//回调数据
			if (listener != null) {
				int area = areaPosition;
				if (area == 1) {
					area = 7;
				} else if (area == 2) {
					area = 96;
				} else if (area == 3) {
					area = 8;
				} else if (area == 4) {
					area = 16;
				}
				listener.onChoose(typePosition, area);
			}
		}
	}

	public void setOnChooseArtistSortListener(OnChooseArtistSortListener listener) {
		this.listener = listener;
	}

	public interface OnChooseArtistSortListener {
		void onChoose(int type, int area);
	}
}
