package com.example.dressfolio3;

import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

public final class DetailDressNextView implements Animation.AnimationListener {
	private boolean mCurrentView;
	FrameLayout detailLayout1;
	ViewFlipper detailLayout2;

	public DetailDressNextView(boolean currentView, FrameLayout detail1, ViewFlipper detail2) {
		mCurrentView = currentView;
		this.detailLayout1 = detail1;
		this.detailLayout2 = detail2;
	}

	public void onAnimationStart(Animation animation) {

	}

	public void onAnimationEnd(Animation animation) {
		detailLayout1.post(new DetailDressSwapViews(mCurrentView, detailLayout1, detailLayout2));
	}

	public void onAnimationRepeat(Animation animation) {
		
	}

}