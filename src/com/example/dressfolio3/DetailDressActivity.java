package com.example.dressfolio3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class DetailDressActivity extends Activity implements OnTouchListener{

	public static int countIndexes = 3;

	LinearLayout buttonLayout;
	ImageView[] indexButtons;
	View[] views;
	String moveTo = "none";
	int currentIndex = 0;

	float downX;
	float upX; 
	float curX; 

	ViewFlipper flipper;
	private FrameLayout detailLayout1;
	private boolean isFirstLayout = true;

	// detailLayout 에 있는 드레스 이름, 실루엣, 색깔, 샵 이름
	TextView dressName;
	TextView shopName;
	ImageView showSilhouette;
	ImageView showColor;

	ImageView detailDress;
	ImageView originalImage;
	Button back;

	Integer[] bigDress = {R.drawable.dress12, R.drawable.dress22, R.drawable.dress32, R.drawable.dress42, R.drawable.dress52, R.drawable.dress62, 
			R.drawable.dress72, R.drawable.dress82, R.drawable.dress92};
	String[] bigDressName = {"로리에 드레스", "장미 드레스", "용궁 드레스", "벨라인 드레스", "인어 드레스", "백합드레스", "은빛 탑 드레스", "로렌스 드레스", "듀트 탑 드레스"};
	String[] bigDressShop = {"아비가일 드레스", "찰스박 웨딩", "아뜰리에 레이", "크리스틴 스포사", "몽유애 웨딩", "라브리디아", "라헬 이명은", "플로렌스", "벨에포크"};
	Integer[] bigDressSilhouetteOff = {R.drawable.silhouette1off, R.drawable.silhouette1off, R.drawable.silhouette1off, R.drawable.silhouette1off, R.drawable.silhouette1off, R.drawable.silhouette1off, 
			R.drawable.silhouette1off, R.drawable.silhouette1off, R.drawable.silhouette1off};
	Integer silhouetteDetermine = 0;

	Integer[] bigDressColor = {R.drawable.colorwhite, R.drawable.colorivory, R.drawable.colorwhite, R.drawable.colorivory, R.drawable.colorwhite, R.drawable.colorivory, 
			R.drawable.colorwhite, R.drawable.colorivory, R.drawable.colorwhite};

	Integer imagePosition;
	Animation originalPhotoLeftAnim, photoLeftAnim, originalPhotoRightAnim, photoRightAnim;

	LinearLayout dressStatus;
	ImageView like;
	Integer ImageViewDetermine =0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detaildress);

		detailLayout1 = (FrameLayout) findViewById(R.id.detailLayout01);

		Intent intent = getIntent();        
		imagePosition =intent.getIntExtra("imagePosition", 0);

		detailDress = (ImageView)findViewById(R.id.detailDress);
		detailDress.setImageResource(bigDress[imagePosition]);          
		detailDress.setOnTouchListener(this);

		originalImage = (ImageView)findViewById(R.id.originalDress);
		originalImage.setImageDrawable(getResources().getDrawable(bigDress[imagePosition]));
		originalImage.setVisibility(View.INVISIBLE);	

		originalPhotoLeftAnim = AnimationUtils.loadAnimation(this, R.anim.translate_filter_left);
		photoLeftAnim = AnimationUtils.loadAnimation(this, R.anim.translate_my_left);
		originalPhotoRightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_my_right);
		photoRightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_filter_right);
		SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
		photoLeftAnim.setAnimationListener(animListener);
		photoRightAnim.setAnimationListener(animListener);

		buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
		buttonLayout.setVisibility(View.GONE);
		flipper = (ViewFlipper) findViewById(R.id.flipper);
		flipper.setVisibility(View.GONE);
		flipper.setOnTouchListener(this);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		flipper.addView(View.inflate(this, R.layout.detail_viewflipper_dressinfo, null), 0);

		indexButtons = new ImageView[countIndexes];
		views = new ImageView[countIndexes];

		for(int i = 0; i < countIndexes; i++) {
			indexButtons[i] = new ImageView(getBaseContext());

			if (i == currentIndex) {
				indexButtons[i].setImageResource(R.drawable.grey);
			} else {
				indexButtons[i].setImageResource(R.drawable.black);
			}
			indexButtons[i].setPadding(10, 0, 10, 30);

			buttonLayout.addView(indexButtons[i], params);

			ImageView curView = new ImageView(getBaseContext());
			if(i==0){
				curView.setImageResource(R.drawable.dress63);
			}else if(i==1){
				curView.setImageResource(R.drawable.dress64);
			}
			views[i] = curView;

			flipper.addView(views[i]);
		}
		dressName = (TextView)findViewById(R.id.dressName);
		dressName.setText(bigDressName[imagePosition]);

		showSilhouette = (ImageView)findViewById(R.id.showSilhouette);
		showSilhouette.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(silhouetteDetermine==0){
					showSilhouette.setImageResource(R.drawable.silhouette1on);
					silhouetteDetermine=1;
				}else{
					showSilhouette.setImageResource(R.drawable.silhouette1off);
					silhouetteDetermine=0;
				}
			}
		});

		dressStatus = (LinearLayout)findViewById(R.id.dressStatus);

		shopName = (TextView)findViewById(R.id.showShopName);
		shopName.setText(bigDressShop[imagePosition]);

		like = (ImageView)findViewById(R.id.likeButton);
		like.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(ImageViewDetermine==0){
					like.setImageResource(R.drawable.likeon);
					ImageViewDetermine=1;
				}else{
					like.setImageResource(R.drawable.likeoff);
					ImageViewDetermine=0;
				}
			}
		});

		back = (Button)findViewById(R.id.backButton);
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});     
	} 

	private void updateIndexes() {
		for(int i = 0; i < countIndexes; i++) {
			if (i == currentIndex) {
				indexButtons[i].setImageResource(R.drawable.grey);
			} else {
				indexButtons[i].setImageResource(R.drawable.black);
			}
		}
	}

	public boolean onTouch(View v, MotionEvent event) {


		if(v != detailDress && v != flipper) return false;

		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			moveTo = "none";
			downX = event.getX();

		}else if(event.getAction() == MotionEvent.ACTION_UP){
			upX = event.getX();

			if(Math.abs(upX - downX)<3){
				if (isFirstLayout) {      
					applyRotation(0, 90);
					isFirstLayout = !isFirstLayout;
					buttonLayout.setVisibility(View.VISIBLE);
					dressStatus.setVisibility(View.GONE);

				} else {   
					applyRotation(0, -90);
					isFirstLayout = !isFirstLayout;					
					buttonLayout.setVisibility(View.GONE);
					flipper.setVisibility(View.GONE);
					dressStatus.setVisibility(View.VISIBLE);
				}

			}
			else if (!isFirstLayout){

				if( upX < downX ) {  // in case of right direction

					flipper.setInAnimation(AnimationUtils.loadAnimation(this,  R.anim.push_left_in));
					flipper.setOutAnimation(AnimationUtils.loadAnimation(this,   R.anim.push_left_out));

					if (currentIndex < (countIndexes-1)) {
						flipper.showNext();

						currentIndex++;
						updateIndexes();
					}
				} else if (upX > downX){ // in case of left direction

					flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
					flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));

					if (currentIndex > 0) {
						flipper.showPrevious();

						currentIndex--;
						updateIndexes();
					}
				}
			}
			else if(isFirstLayout){

				curX = event.getX();

				if(curX-downX>100 && moveTo.equals("none")){
					moveTo = "left";
					Log.i("drag","toLeft");
					if(imagePosition == 0){
					}
					else{
						imagePosition--;
						originalImage.setVisibility(View.VISIBLE);
						detailDress.startAnimation(photoRightAnim);
						originalImage.startAnimation(originalPhotoRightAnim);
						detailDress.setImageDrawable(getResources().getDrawable(bigDress[imagePosition]));	
						dressName.setText(bigDressName[imagePosition]);
						shopName.setText(bigDressShop[imagePosition]);
						like.setImageResource(R.drawable.likeoff);

					}
				}
				else if(curX-downX<-100 && moveTo.equals("none")){
					moveTo = "right";
					Log.i("drag","toRight");
					if(imagePosition==8){

					}
					else{
						imagePosition++;
						originalImage.setVisibility(View.VISIBLE);
						detailDress.startAnimation(photoLeftAnim);
						originalImage.startAnimation(originalPhotoLeftAnim);
						detailDress.setImageDrawable(getResources().getDrawable(bigDress[imagePosition]));	
						dressName.setText(bigDressName[imagePosition]);
						shopName.setText(bigDressShop[imagePosition]);
						like.setImageResource(R.drawable.likeoff);

					}
				}
			}
		}


		return true;
	}

	private class SlidingPageAnimationListener implements AnimationListener {

		public void onAnimationEnd(Animation animation) {
			originalImage.setVisibility(View.INVISIBLE);	
			originalImage.setImageDrawable(detailDress.getDrawable());

		}

		public void onAnimationRepeat(Animation arg0) {
			// TODO Auto-generated method stub

		}

		public void onAnimationStart(Animation arg0) {
			// TODO Auto-generated method stub

		}
	}
	private void applyRotation(float start, float end) {

		// Find the center of image
		final float centerX = detailLayout1.getWidth() / 2.0f;
		final float centerY = detailLayout1.getHeight() / 2.0f;
		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final DetailDressFlip3dAnimation rotation = new DetailDressFlip3dAnimation(start, end, centerX, centerY);

		rotation.setDuration(200);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DetailDressNextView(isFirstLayout, detailLayout1, flipper));

		if (isFirstLayout){			
			detailLayout1.startAnimation(rotation);

		} else {
			flipper.startAnimation(rotation);

		}
	}


}