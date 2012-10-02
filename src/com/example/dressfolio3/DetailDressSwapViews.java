package com.example.dressfolio3;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

public final class DetailDressSwapViews implements Runnable {
	private boolean mIsFirstView;
	FrameLayout detailLayout1;
	ViewFlipper detailLayout2;

	public DetailDressSwapViews(boolean isFirstView, FrameLayout detail1, ViewFlipper detail2) {
		mIsFirstView = isFirstView;
		this.detailLayout1 = detail1;
		this.detailLayout2 = detail2;
	}

	public void run() {
		final float centerX = detailLayout1.getWidth() / 2.0f;
		final float centerY = detailLayout1.getHeight() / 2.0f;
		DetailDressFlip3dAnimation rotation;

		if (mIsFirstView) {
			detailLayout1.setVisibility(View.GONE);
			detailLayout2.setVisibility(View.VISIBLE);
			detailLayout2.requestFocus();

			rotation = new DetailDressFlip3dAnimation(-90, 0, centerX, centerY);

		} else {
			detailLayout2.setVisibility(View.GONE);
			detailLayout1.setVisibility(View.VISIBLE);
			detailLayout1.requestFocus();

			rotation = new DetailDressFlip3dAnimation(90, 0, centerX, centerY);
		}

		rotation.setDuration(200);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new DecelerateInterpolator());

		if (mIsFirstView) {
			detailLayout2.startAnimation(rotation);
		} else {
			detailLayout1.startAnimation(rotation);
		}
	}
}