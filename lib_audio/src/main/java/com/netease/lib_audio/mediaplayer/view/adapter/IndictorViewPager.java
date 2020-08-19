package com.netease.lib_audio.mediaplayer.view.adapter;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class IndictorViewPager extends ViewPager {


	private PointF pointF = new PointF();
	private OnSingleTouchListener listener;

	public IndictorViewPager(@NonNull Context context) {
		super(context);
	}

	public IndictorViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				pointF.x = ev.getX();
				pointF.y = ev.getY();
				if (this.getChildCount() > 1) {
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (PointF.length(ev.getX() - pointF.x, ev.getY()
						- pointF.y) < (float) 5.0) {
					if (listener != null) {
						listener.onSingleTouch(ev);
					}
					return true;
				}
				break;
		}
		return super.onTouchEvent(ev);
	}

	public void setOnSingleTouchListener(OnSingleTouchListener listener) {
		this.listener = listener;
	}

	public interface OnSingleTouchListener {
		void onSingleTouch(MotionEvent event);
	}

}
