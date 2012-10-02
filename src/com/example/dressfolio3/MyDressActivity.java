package com.example.dressfolio3;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyDressActivity extends Activity {
	float downX = 0, curX = 0;
	String moveTo = "none";
	int index, dressNum;
	ImageView dressImage, originalImage;
	TextView naviTitle;
	Animation originalPhotoLeftAnim, photoLeftAnim, originalPhotoRightAnim, photoRightAnim;
	Integer[] thumbnails = {R.drawable.thumb1, R.drawable.thumb2, R.drawable.thumb3, R.drawable.thumb4,
			R.drawable.thumb5, R.drawable.thumb6, R.drawable.thumb7, R.drawable.thumb8,
			R.drawable.thumb9, R.drawable.thumb10, R.drawable.thumb11, R.drawable.thumb12,
			R.drawable.thumb13, R.drawable.thumb14, R.drawable.thumb15};

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mydresslayout);
		
		index = getIntent().getIntExtra("index",0);
		dressNum = getIntent().getIntExtra("dressNum",0);
		
		Button backButton = (Button)findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		naviTitle = (TextView)findViewById(R.id.myDressTitle);
		naviTitle.setText("입어 본 드레스 ("+(index*1+1)+"/4)");

		originalPhotoLeftAnim = AnimationUtils.loadAnimation(this, R.anim.translate_filter_left);
		photoLeftAnim = AnimationUtils.loadAnimation(this, R.anim.translate_my_left);
		originalPhotoRightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_my_right);
		photoRightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_filter_right);
		SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
		photoLeftAnim.setAnimationListener(animListener);
		photoRightAnim.setAnimationListener(animListener);
		
		if(index<3){
			dressImage = (ImageView)findViewById(R.id.dressPhoto);
			dressImage.setImageDrawable(getResources().getDrawable(thumbnails[dressNum]));
			
			originalImage = (ImageView)findViewById(R.id.originalDressPhoto);
			originalImage.setImageDrawable(getResources().getDrawable(thumbnails[dressNum]));
			originalImage.setVisibility(View.INVISIBLE);
		}
		else{
			dressImage = (ImageView)findViewById(R.id.dressPhoto);
			originalImage = (ImageView)findViewById(R.id.originalDressPhoto);
			originalImage.setVisibility(View.INVISIBLE);
			down2();	
		}
		
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
						
						naviTitle.setText("입어 본 드레스 ("+(index*1+1)+"/4)");
					}
				}
				else if(curX-downX<-100 && moveTo.equals("none")){
					moveTo = "right";
					Log.i("drag","toRight");
					if(index==3){
						
					}
					else if(index==2){
						index++;
						dressNum++;
						down2();
						
						naviTitle.setText("입어 본 드레스 ("+(index*1+1)+"/4)");	
						
					}
					else{
						index++;
						dressNum++;
						originalImage.setVisibility(View.VISIBLE);
						dressImage.startAnimation(photoLeftAnim);
						originalImage.startAnimation(originalPhotoLeftAnim);
						dressImage.setImageDrawable(getResources().getDrawable(thumbnails[dressNum]));	
						
						naviTitle.setText("입어 본 드레스 ("+(index*1+1)+"/4)");
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
			Log.d("index",index+"");
			if(index==3){
				originalImage.setImageBitmap(image);
			}
		}

		public void onAnimationRepeat(Animation arg0) {
			// TODO Auto-generated method stub
			
		}

		public void onAnimationStart(Animation arg0) {
			// TODO Auto-generated method stub
			
		}
	}

	public void down2(){
		String urlAddr = "http://www.dressndress.net/files/a.jpg";
		DownThread thread = new DownThread(urlAddr);
		thread.start();

	}

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg){
			int result = msg.what;
			switch(result){
			case 0 : 
				dressImage.setImageBitmap(image);
				originalImage.setVisibility(View.VISIBLE);
				dressImage.startAnimation(photoLeftAnim);
				originalImage.startAnimation(originalPhotoLeftAnim);
				break;
			}
		}
	};
	Bitmap image;

	class DownThread extends Thread{
			String urlAddr;
			DownThread(String urlAddr){
			this.urlAddr = urlAddr;
		}
		public void run(){

			try{
				URL url = new URL(urlAddr);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setDoInput(true); 
				conn.connect();
				InputStream is = conn.getInputStream();
				BitmapFactory.Options option = new BitmapFactory.Options();
				option.inSampleSize = 4;
				
				image = BitmapFactory.decodeStream(is, null, option);
				is.close();
				is=null;
				handler.sendEmptyMessage(0);

				conn.disconnect();
			}catch (Exception e) {
				Log.e("다운로드 에러", e.getMessage());
			}
		}//run()

	}
}