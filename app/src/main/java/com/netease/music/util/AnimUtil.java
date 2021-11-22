package com.netease.music.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * @author Zhangruiping
 */
public class AnimUtil {

	/**
	 * 评论点赞动画
	 *
	 * @param view
	 * @return AnimatorSet
	 */
	public static AnimatorSet getLikeAnim(ImageView view) {
		AnimatorSet animatorSetsuofang = new AnimatorSet();//组合动画
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f, 1f);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.5f, 1f);
		animatorSetsuofang.setDuration(500);
		animatorSetsuofang.setInterpolator(new DecelerateInterpolator());
		animatorSetsuofang.play(scaleX).with(scaleY);//两个动画同时开始
		return animatorSetsuofang;
	}

	/**
	 * 没同意用户协议时左右晃动的动画
	 */
	public static Animation shakeAnimation() {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
//设置一个循环加速器，使用传入的次数就会出现摆动的效果。
		translateAnimation.setInterpolator(new CycleInterpolator(3));
		translateAnimation.setDuration(300);
		return translateAnimation;
	}
}
