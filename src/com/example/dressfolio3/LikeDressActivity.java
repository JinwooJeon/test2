package com.example.dressfolio3;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LikeDressActivity extends Activity {
	float downX = 0, curX = 0;
	String moveTo = "none";
	int index, dressNum;
	ImageView dressImage, originalImage;
	TextView naviTitle, f1Text, f2Text, f3Text;
	Animation originalPhotoLeftAnim, photoLeftAnim, originalPhotoRightAnim, photoRightAnim;
	Integer[] thumbnails = {R.drawable.thumb1, R.drawable.thumb2, R.drawable.thumb3, R.drawable.thumb4,
			R.drawable.thumb5, R.drawable.thumb6, R.drawable.thumb7, R.drawable.thumb8,
			R.drawable.thumb9, R.drawable.thumb10, R.drawable.thumb11, R.drawable.thumb12,
			R.drawable.thumb13, R.drawable.thumb14, R.drawable.thumb15};

	int[] filter1 = {R.drawable.silhouette1off, R.drawable.silhouette1off, R.drawable.silhouette1off, R.drawable.silhouette1off, R.drawable.silhouette1off, R.drawable.silhouette1off};
	int[] filter2 = {R.drawable.colorgrey, R.drawable.colorpurple, R.drawable.colorgrey, R.drawable.colorwhite, R.drawable.colorivory, R.drawable.colorgreen};
	String[] filter3 = {"아비가일 드레스", "크리스틴 스포사", "찰스 박 웨딩", "아비가일 드레스", "찰스 박 웨딩", "아비가일 드레스"};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.likedresslayout);
		
		index = getIntent().getIntExtra("index",0);
		dressNum = getIntent().getIntExtra("dressNum",0);
		
		Button backButton = (Button)findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		naviTitle = (TextView)findViewById(R.id.myDressTitle);
		naviTitle.setText("찜한 드레스 ("+(index*1+1)+"/6)");

		f1Text = (TextView)findViewById(R.id.filter1);
		f1Text.setBackgroundDrawable(getResources().getDrawable(filter1[index]));
		f2Text = (TextView)findViewById(R.id.filter2);
		f2Text.setBackgroundDrawable(getResources().getDrawable(filter2[index]));
		f3Text = (TextView)findViewById(R.id.filter3);
		f3Text.setText(filter3[index]);
		

		originalPhotoLeftAnim = AnimationUtils.loadAnimation(this, R.anim.translate_filter_left);
		photoLeftAnim = AnimationUtils.loadAnimation(this, R.anim.translate_my_left);
		originalPhotoRightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_my_right);
		photoRightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_filter_right);
		SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
		photoLeftAnim.setAnimationListener(animListener);
		photoRightAnim.setAnimationListener(animListener);
		
		
		dressImage = (ImageView)findViewById(R.id.dressPhoto);
		dressImage.setImageDrawable(getResources().getDrawable(thumbnails[dressNum]));
		
		originalImage = (ImageView)findViewById(R.id.originalDressPhoto);
		originalImage.setImageDrawable(getResources().getDrawable(thumbnails[dressNum]));
		originalImage.setVisibility(View.INVISIBLE);
		
		dressImage.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==0){
					downX = event.getX();
					curX = downX;
					moveTo = "none";
				}
				else if(event.getAction()==1){
				}
				else if(event.getAction()==2){
					curX = event.getX();
				}
				
				if(curX-downX>100 && moveTo.equals("none")){
					moveTo = "left";
					Log.i("drag","toLeft");
					if(index == 0){
					}
					else{
						index--;
						dressNum--;
						originalImage.setVisibility(View.VISIBLE);
						dressImage.startAnimation(photoRightAnim);
						originalImage.startAnimation(originalPhotoRightAnim);
						dressImage.setImageDrawable(getResources().getDrawable(thumbnails[dressNum]));	
						
						naviTitle.setText("찜한 드레스 ("+(index*1+1)+"/6)");		
						f1Text.setBackgroundDrawable(getResources().getDrawable(filter1[index]));
						f2Text.setBackgroundDrawable(getResources().getDrawable(filter2[index]));
						f3Text.setText(filter3[index]);
					}
				}
				else if(curX-downX<-100 && moveTo.equals("none")){
					moveTo = "right";
					Log.i("drag","toRight");
					if(index==5){
						
					}
					else{
						index++;
						dressNum++;
						originalImage.setVisibility(View.VISIBLE);
						dressImage.startAnimation(photoLeftAnim);
						originalImage.startAnimation(originalPhotoLeftAnim);
						dressImage.setImageDrawable(getResources().getDrawable(thumbnails[dressNum]));	
						
						naviTitle.setText("찜한 드레스 ("+(index*1+1)+"/6)");	
						f1Text.setBackgroundDrawable(getResources().getDrawable(filter1[index]));
						f2Text.setBackgroundDrawable(getResources().getDrawable(filter2[index]));
						f3Text.setText(filter3[index]);
					}
				}
				return true;
			}
			
			
		});
		
	}
	private class SlidingPageAnimationListener implements AnimationListener {

		public void onAnimationEnd(Animation animation) {
			originalImage.setVisibility(View.INVISIBLE);	
			originalImage.setImageDrawable(dressImage.getDrawable());
		}

		public void onAnimationRepeat(Animation arg0) {
			// TODO Auto-generated method stub
			
		}

		public void onAnimationStart(Animation arg0) {
			// TODO Auto-generated method stub
			
		}
	}
}