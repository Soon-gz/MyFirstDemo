package com.abings.baby.widget.dialog.animation.FlipExit;

import android.view.View;

import com.abings.baby.widget.dialog.animation.BaseAnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class FlipHorizontalExit extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(ObjectAnimator.ofFloat(view, "rotationY", 0, 90),//
				ObjectAnimator.ofFloat(view, "alpha", 1, 0));
	}
}
