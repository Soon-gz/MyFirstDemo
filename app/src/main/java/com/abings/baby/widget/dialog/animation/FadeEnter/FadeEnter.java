package com.abings.baby.widget.dialog.animation.FadeEnter;

import android.view.View;

import com.abings.baby.widget.dialog.animation.BaseAnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class FadeEnter extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(duration));
	}
}
