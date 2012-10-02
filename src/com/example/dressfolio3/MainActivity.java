package com.example.dressfolio3;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	private boolean isMyMenuOpen = false;
	private boolean leftMenuOpen = false;
	Integer determinant =0;

	Button leftMenuButton,myButton, closeMyPageButton, closeLeftMenuButton;

	Animation FilterLeft, FilterLeft2, FilterRight,	FilterRight2;
	Animation MyLeftAnim, MyLeftAnim2, MyRightAnim,	 MyRightAnim2;

	LinearLayout leftMenu, myMenu, mainGrid;   

	static final int[] filters = {R.id.silhouette1, R.id.silhouette2, R.id.silhouette3, R.id.silhouette4,R.id.silhouette5, R.id.silhouette6, 
		R.id.color1, R.id.color2, R.id.color3, R.id.color4, R.id.color5, R.id.color6, R.id.color7, R.id.color8};

	ImageView[] imageView;
	Integer ImageNumber;

	TextView leftFeedback, leftAnnouncement, leftSetting;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);        


		// 이미지 임시 설정 및 main 화면 table에 삽입.
		Integer[] thumbnails = {R.drawable.dress1, R.drawable.dress2, R.drawable.dress3, R.drawable.dress4,	R.drawable.dress5, R.drawable.dress6, 
				R.drawable.dress7, R.drawable.dress8, R.drawable.dress9};
		Integer[] bigDress = {R.drawable.dress12, R.drawable.dress22, R.drawable.dress32, R.drawable.dress42, R.drawable.dress52, R.drawable.dress62, 
				R.drawable.dress72, R.drawable.dress82, R.drawable.dress92};

		ImageNumber = thumbnails.length;

		imageView = new ImageView[thumbnails.length];        

		for(int i=0; i<thumbnails.length; i++){
			imageView[i] = new ImageView(this);
			imageView[i].setImageResource(thumbnails[i]);
			imageView[i].setPadding(0,0,0,0);

			imageView[i].setOnClickListener(new MyListener(bigDress[i], i));            
		}

		TableRow row1 = (TableRow) findViewById(R.id.tableRow1);
		TableRow row2 = (TableRow) findViewById(R.id.tableRow2);
		TableRow row3 = (TableRow) findViewById(R.id.tableRow3);

		for(int i=0; i<thumbnails.length; i++){
			if(i<(thumbnails.length-1)/3+1)
				row1.addView(imageView[i], 240, 367);     
			else if(i<(thumbnails.length-1)/3+(thumbnails.length-2)/3+2)
				row2.addView(imageView[i], 240, 367);   
			else
				row3.addView(imageView[i], 240, 367);   
		}

		mainGrid = (LinearLayout) findViewById(R.id.mainGridLayout);

		// About My Page
		myButton = 		 (Button) findViewById(R.id.myButton);
		closeMyPageButton =    (Button) findViewById(R.id.closeMyMenuButton);		
		myMenu =   (LinearLayout) findViewById(R.id.myMenuLayout);

		// About My Page Animation
		MyLeftAnim = AnimationUtils.loadAnimation(this, R.anim.translate_my_left);
		MyLeftAnim2 = AnimationUtils.loadAnimation(this, R.anim.translate_my_left2);
		MyRightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_my_right);
		MyRightAnim2 = AnimationUtils.loadAnimation(this, R.anim.translate_my_right2);

		// About Left Menu
		leftMenuButton = (Button) findViewById(R.id.leftMenuButton);
		closeLeftMenuButton =  (Button) findViewById(R.id.closeLeftMenuButton);
		leftMenu = (LinearLayout) findViewById(R.id.leftMenuLayout);

		// About Left Menu Animation (filters)
		FilterRight = AnimationUtils.loadAnimation(this, R.anim.translate_filter_right);
		FilterLeft = AnimationUtils.loadAnimation(this, R.anim.translate_filter_left);
		FilterRight2 = AnimationUtils.loadAnimation(this, R.anim.translate_filter_right2);
		FilterLeft2 = AnimationUtils.loadAnimation(this, R.anim.translate_filter_left2);

		// Sliding Listener		
		SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
		FilterRight.setAnimationListener(animListener);
		FilterLeft.setAnimationListener(animListener); 
		MyLeftAnim.setAnimationListener(animListener);
		MyRightAnim.setAnimationListener(animListener);

		myButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (isMyMenuOpen) {
					myMenu.startAnimation(MyRightAnim);
					mainGrid.startAnimation(MyRightAnim2);

				} else {
					determinant=1;
					disableAllImages();
					myMenu.setVisibility(View.VISIBLE);
					myMenu.startAnimation(MyLeftAnim);
					mainGrid.startAnimation(MyLeftAnim2);
				}
			}
		});


		leftMenuButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (leftMenuOpen) {
					leftMenu.startAnimation(FilterLeft);
					mainGrid.startAnimation(FilterLeft2);

				} else {
					determinant=-1;
					disableAllImages();
					leftMenu.setVisibility(View.VISIBLE);
					leftMenu.startAnimation(FilterRight);
					mainGrid.startAnimation(FilterRight2);
				}

			}
		});    



		for(int filtersButton:filters){
			ImageView Btn = (ImageView)findViewById(filtersButton);
			Btn.setOnClickListener(this);
		}

		ExpandableListView myPageList = (ExpandableListView) findViewById(R.id.myPageList);
		myPageList.setAdapter(new MyPageListAdapter());
		myPageList.setDividerHeight(0);
		down2();
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
				handler.sendEmptyMessage(0);

				conn.disconnect();
			}catch (Exception e) {
				Log.e("다운로드 에러", e.getMessage());
			}
		}//run()

	}

	public void disableAllImages(){
		for(int i=0; i<ImageNumber; i++){
			imageView[i].setEnabled(false);            
		}
	}

	public void enableAllImages(){
		for(int i=0; i<ImageNumber; i++){
			imageView[i].setEnabled(true);          
		}
	}

	public void onClick(View v) {

		switch(v.getId()){
		case R.id.silhouette1: 		Toast.makeText(getApplicationContext(), "Here1", 3000).show();		break;
		case R.id.silhouette2: 		Toast.makeText(getApplicationContext(), "Here2", 3000).show();		break;
		case R.id.silhouette3: 		Toast.makeText(getApplicationContext(), "Here3", 3000).show();		break;
		case R.id.silhouette4: 		Toast.makeText(getApplicationContext(), "Here4", 3000).show();		break;
		case R.id.silhouette5: 		Toast.makeText(getApplicationContext(), "Here5", 3000).show();		break;
		case R.id.silhouette6: 		Toast.makeText(getApplicationContext(), "Here6", 3000).show();		break;
		case R.id.color1: 		break;
		case R.id.color2: 		break;
		case R.id.color3: 		break;
		case R.id.color4: 		break;
		case R.id.color5: 		break;
		case R.id.color6: 		break;
		case R.id.color7: 		break;
		case R.id.color8: 		break;

		}
	}

	// send clicked image to next activity
	private class MyListener implements Button.OnClickListener {

		int image;
		int imagePosition;

		public MyListener (int imageName, int position) {
			image = imageName;
			imagePosition = position;
		}

		public void onClick(View v) {

			Intent mainToDetailDress = new Intent(MainActivity.this, DetailDressActivity.class);
			mainToDetailDress.putExtra("imagePosition", imagePosition);
			startActivity(mainToDetailDress);
		}
	} 

	// slidingPageAnimation - leftMenu, myPage 
	private class SlidingPageAnimationListener implements AnimationListener {

		public void onAnimationEnd(Animation animation) {

			if(determinant<0){
				if (leftMenuOpen) {

					leftMenuButton.setEnabled(true);
					myButton.setEnabled(true);
					closeLeftMenuButton.setEnabled(false);				
					mainGrid.setEnabled(true);
					enableAllImages();

					closeLeftMenuButton.setVisibility(View.GONE);
					leftMenu.setVisibility(View.GONE);					

					determinant=0;
					leftMenuOpen = false;

				} else {

					leftMenuButton.setEnabled(false);
					myButton.setEnabled(false);
					closeLeftMenuButton.setEnabled(true);
					mainGrid.setEnabled(false);

					closeLeftMenuButton.setVisibility(View.VISIBLE);
					closeLeftMenuButton.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							leftMenu.startAnimation(FilterLeft);
							mainGrid.startAnimation(FilterLeft2);						
						}
					});

					leftMenuOpen = true;
				}	

			}else{

				if (isMyMenuOpen) {					

					leftMenuButton.setEnabled(true);
					myButton.setEnabled(true);
					closeMyPageButton.setEnabled(false);
					mainGrid.setEnabled(true);
					enableAllImages();

					closeMyPageButton.setVisibility(View.GONE);
					myMenu.setVisibility(View.GONE);

					isMyMenuOpen = false;
					determinant=0;

				} else {           

					leftMenuButton.setEnabled(false);
					myButton.setEnabled(false);
					closeMyPageButton.setEnabled(true);
					mainGrid.setEnabled(false);

					closeMyPageButton.setVisibility(View.VISIBLE);
					closeMyPageButton.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							myMenu.startAnimation(MyRightAnim);
							mainGrid.startAnimation(MyRightAnim2);						
						}
					});

					isMyMenuOpen = true;

				}				
			}
		}

		public void onAnimationRepeat(Animation animation) {

		}

		public void onAnimationStart(Animation animation) {

		} 
	}

	// listview in myPage
	public class MyPageListAdapter extends BaseExpandableListAdapter {

		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}

		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			if(groupPosition==0){
				LinearLayout ly = new LinearLayout(MainActivity.this);
				ly.setOrientation(LinearLayout.VERTICAL);
				ly.setPadding(10, 0, 0, 0);
				ly.setBackgroundColor(Color.rgb(170, 170, 170));
				return ly;
			}
			else if(groupPosition==1){
				LinearLayout ly = new LinearLayout(MainActivity.this);
				ly.setOrientation(LinearLayout.VERTICAL);
				ly.setBackgroundColor(Color.rgb(140, 140, 140));

				LinearLayout ly1 = new LinearLayout(MainActivity.this);
				ly1.setOrientation(LinearLayout.HORIZONTAL);
				ly1.setBackgroundColor(Color.rgb(140, 140, 140));		
				ly1.setPadding(0, 20, 0, 20);
				ly.addView(ly1);

				ImageView iv1 = new ImageView(MainActivity.this);
				iv1.setImageDrawable(getResources().getDrawable(R.drawable.thumb1));
				iv1.setLayoutParams(new LinearLayout.LayoutParams(285, 240));
				iv1.setScaleType(ScaleType.CENTER_CROP);
				iv1.setPadding(20, 10, 10, 10);
				ly1.addView(iv1);

				ImageView iv2 = new ImageView(MainActivity.this);
				iv2.setImageDrawable(getResources().getDrawable(R.drawable.thumb2));
				iv2.setLayoutParams(new LinearLayout.LayoutParams(285, 240));
				iv2.setScaleType(ScaleType.CENTER_CROP);
				iv2.setPadding(10, 10, 10, 10);
				ly1.addView(iv2);

				LinearLayout ly2 = new LinearLayout(MainActivity.this);
				ly2.setOrientation(LinearLayout.HORIZONTAL);
				ly2.setBackgroundColor(Color.rgb(140, 140, 140));	
				ly2.setPadding(0, 0, 0, 20);
				ly.addView(ly2);		

				ImageView iv3 = new ImageView(MainActivity.this);
				iv3.setImageDrawable(getResources().getDrawable(R.drawable.thumb3));
				iv3.setLayoutParams(new LinearLayout.LayoutParams(285, 240));
				iv3.setScaleType(ScaleType.CENTER_CROP);
				iv3.setPadding(20, 10, 10, 10);
				ly2.addView(iv3);

				ImageView iv4 = new ImageView(MainActivity.this);
				//iv4.setImageDrawable(getResources().getDrawable(R.drawable.thumb4));
				iv4.setImageBitmap(image);
				iv4.setLayoutParams(new LinearLayout.LayoutParams(285, 240));
				iv4.setScaleType(ScaleType.CENTER_CROP);
				iv4.setPadding(10, 10, 10, 10);
				ly2.addView(iv4);

				iv1.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this, MyDressActivity.class);
						intent.putExtra("dressNum", 0);
						intent.putExtra("index", 0);
						startActivity(intent);
					}
				});
				iv2.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this, MyDressActivity.class);
						intent.putExtra("dressNum", 1);
						intent.putExtra("index", 1);
						startActivity(intent);
					}
				});
				iv3.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this, MyDressActivity.class);
						intent.putExtra("dressNum", 2);
						intent.putExtra("index", 2);
						startActivity(intent);
					}
				});
				iv4.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this, MyDressActivity.class);
						intent.putExtra("dressNum", 3);
						intent.putExtra("index", 3);
						startActivity(intent);
					}
				});

				return ly;
			}
			else{
				LinearLayout ly = new LinearLayout(MainActivity.this);
				ly.setOrientation(LinearLayout.VERTICAL);
				ly.setBackgroundColor(Color.rgb(140, 140, 140));

				LinearLayout ly1 = new LinearLayout(MainActivity.this);
				ly1.setOrientation(LinearLayout.HORIZONTAL);
				ly1.setBackgroundColor(Color.rgb(110, 110, 110));		
				ly1.setPadding(0, 20, 0, 20);
				ly.addView(ly1);

				ImageView iv1 = new ImageView(MainActivity.this);
				iv1.setImageDrawable(getResources().getDrawable(R.drawable.thumb10));
				iv1.setLayoutParams(new LinearLayout.LayoutParams(285, 240));
				iv1.setScaleType(ScaleType.CENTER_CROP);
				iv1.setPadding(20, 10, 10, 10);
				ly1.addView(iv1);

				ImageView iv2 = new ImageView(MainActivity.this);
				iv2.setImageDrawable(getResources().getDrawable(R.drawable.thumb6));
				iv2.setLayoutParams(new LinearLayout.LayoutParams(285, 240));
				iv2.setScaleType(ScaleType.CENTER_CROP);
				iv2.setPadding(10, 10, 10, 10);
				ly1.addView(iv2);

				LinearLayout ly2 = new LinearLayout(MainActivity.this);
				ly2.setOrientation(LinearLayout.HORIZONTAL);
				ly2.setBackgroundColor(Color.rgb(110, 110, 110));	
				ly2.setPadding(0, 0, 0, 20);
				ly.addView(ly2);		

				ImageView iv3 = new ImageView(MainActivity.this);
				iv3.setImageDrawable(getResources().getDrawable(R.drawable.thumb8));
				iv3.setLayoutParams(new LinearLayout.LayoutParams(285, 240));
				iv3.setScaleType(ScaleType.CENTER_CROP);
				iv3.setPadding(20, 10, 10, 10);
				ly2.addView(iv3);

				ImageView iv4 = new ImageView(MainActivity.this);
				iv4.setImageDrawable(getResources().getDrawable(R.drawable.thumb11));
				iv4.setLayoutParams(new LinearLayout.LayoutParams(285, 240));
				iv4.setScaleType(ScaleType.CENTER_CROP);
				iv4.setPadding(10, 10, 10, 10);
				ly2.addView(iv4);

				LinearLayout ly3 = new LinearLayout(MainActivity.this);
				ly3.setOrientation(LinearLayout.HORIZONTAL);
				ly3.setBackgroundColor(Color.rgb(110, 110, 110));	
				ly3.setPadding(0, 0, 0, 20);
				ly.addView(ly3);		

				ImageView iv5 = new ImageView(MainActivity.this);
				iv5.setImageDrawable(getResources().getDrawable(R.drawable.thumb13));
				iv5.setLayoutParams(new LinearLayout.LayoutParams(285, 240));
				iv5.setScaleType(ScaleType.CENTER_CROP);
				iv5.setPadding(20, 10, 10, 10);
				ly3.addView(iv5);

				ImageView iv6 = new ImageView(MainActivity.this);
				iv6.setImageDrawable(getResources().getDrawable(R.drawable.thumb9));
				iv6.setLayoutParams(new LinearLayout.LayoutParams(285, 240));
				iv6.setScaleType(ScaleType.CENTER_CROP);
				iv6.setPadding(10, 10, 10, 10);
				ly3.addView(iv6);

				iv1.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this, LikeDressActivity.class);
						intent.putExtra("dressNum", 5);
						intent.putExtra("index", 0);
						startActivity(intent);
					}
				});


				iv2.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this, LikeDressActivity.class);
						intent.putExtra("dressNum", 6);
						intent.putExtra("index", 1);
						startActivity(intent);
					}
				});


				iv3.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this, LikeDressActivity.class);
						intent.putExtra("dressNum", 7);
						intent.putExtra("index", 2);
						startActivity(intent);
					}
				});


				iv4.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this, LikeDressActivity.class);
						intent.putExtra("dressNum", 8);
						intent.putExtra("index", 3);
						startActivity(intent);
					}
				});


				iv5.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this, LikeDressActivity.class);
						intent.putExtra("dressNum", 9);
						intent.putExtra("index", 4);
						startActivity(intent);
					}
				});


				iv6.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this, LikeDressActivity.class);
						intent.putExtra("dressNum", 10);
						intent.putExtra("index", 5);
						startActivity(intent);
					}
				});

				return ly;
			}
		}

		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		public Object getGroup(int groupPosition) {
			return null;
		}

		public int getGroupCount() {
			return 3;
		}

		public long getGroupId(int groupPosition) {
			return 0;
		}

		public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
				ViewGroup parent) {
			if(groupPosition==0){
				LinearLayout ly = new LinearLayout(MainActivity.this);
				ly.setOrientation(LinearLayout.VERTICAL);
				ly.setBackgroundColor(Color.rgb(170, 170, 170));
				ly.setBackgroundDrawable(getResources().getDrawable(R.drawable.rightmenubook));
				
				LinearLayout divider = new LinearLayout(MainActivity.this);
				divider.setBackgroundColor(Color.BLACK);
				divider.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 1));
				ly.addView(divider);
/*
				TextView tv1 = new TextView(MainActivity.this);
				tv1.setText("방문 예약하기");
				tv1.setTextColor(Color.BLACK);
				tv1.setPadding(20, 15, 0, 0);
				tv1.setTextSize(20);
				ly.addView(tv1);

				TextView tv2 = new TextView(MainActivity.this);
				tv2.setText("방문 날짜만 입력하면 예약까지 한번에");
				tv2.setTextColor(Color.BLACK);
				tv2.setPadding(20, 0, 0, 15);
				tv2.setTextSize(10);
				ly.addView(tv2);
*/

				return ly;
			}
			else if(groupPosition==1){
				LinearLayout ly = new LinearLayout(MainActivity.this);
				ly.setOrientation(LinearLayout.VERTICAL);
				ly.setBackgroundColor(Color.rgb(140, 140, 140));	
				ly.setBackgroundDrawable(getResources().getDrawable(R.drawable.rightmenumy));			

				LinearLayout divider = new LinearLayout(MainActivity.this);
				divider.setBackgroundColor(Color.BLACK);
				divider.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 1));
				ly.addView(divider);
/*
				LinearLayout ly1 = new LinearLayout(MainActivity.this);
				ly1.setOrientation(LinearLayout.HORIZONTAL);
				ly1.setBackgroundColor(Color.rgb(140, 140, 140));	
				ly.addView(ly1);

				LinearLayout ly11 = new LinearLayout(MainActivity.this);
				ly11.setOrientation(LinearLayout.VERTICAL);
				ly11.setBackgroundColor(Color.rgb(140, 140, 140));		
				ly1.addView(ly11);

				TextView tv1 = new TextView(MainActivity.this);
				tv1.setText("입어 본 드레스");
				tv1.setTextColor(Color.BLACK);
				tv1.setPadding(20, 15, 0, 0);
				tv1.setTextSize(20);
				ly11.addView(tv1);

				TextView tv2 = new TextView(MainActivity.this);
				tv2.setText("샵에서 입은 드레스를 보여 드립니다");
				tv2.setTextColor(Color.BLACK);
				tv2.setPadding(20, 0, 0, 15);
				tv2.setTextSize(10);
				ly11.addView(tv2);

				LinearLayout ly12 = new LinearLayout(MainActivity.this);
				ly12.setOrientation(LinearLayout.VERTICAL);
				ly12.setBackgroundColor(Color.rgb(140, 140, 140));	
				ly1.addView(ly12);

				if(isExpanded){
					TextView tv3 = new TextView(MainActivity.this);
					tv3.setText("▼");
					tv3.setTextColor(Color.BLACK);
					tv3.setPadding(100, 25, 0, 0);
					tv3.setTextSize(20);
					ly12.addView(tv3);
				}
				else{
					TextView tv3 = new TextView(MainActivity.this);
					tv3.setText("▶");
					tv3.setTextColor(Color.BLACK);
					tv3.setPadding(100, 25, 0, 0);
					tv3.setTextSize(20);
					ly12.addView(tv3);
				}
*/
				return ly;
			}
			else{	
				LinearLayout ly = new LinearLayout(MainActivity.this);
				ly.setOrientation(LinearLayout.VERTICAL);
				ly.setPadding(0, 0, 0, 10);
				ly.setBackgroundColor(Color.rgb(110, 110, 110));	
				ly.setBackgroundDrawable(getResources().getDrawable(R.drawable.rightmenulike));			

				LinearLayout divider = new LinearLayout(MainActivity.this);
				divider.setBackgroundColor(Color.BLACK);
				divider.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 1));
				ly.addView(divider);
/*
				LinearLayout ly1 = new LinearLayout(MainActivity.this);
				ly1.setOrientation(LinearLayout.HORIZONTAL);
				ly1.setPadding(0, 10, 0, 0);
				ly1.setBackgroundColor(Color.rgb(110, 110, 110));	
				ly.addView(ly1);

				LinearLayout ly11 = new LinearLayout(MainActivity.this);
				ly11.setOrientation(LinearLayout.VERTICAL);
				ly11.setBackgroundColor(Color.rgb(110, 110, 110));		
				ly1.addView(ly11);

				TextView tv1 = new TextView(MainActivity.this);
				tv1.setText("내가 찜한 드레스");
				tv1.setTextColor(Color.BLACK);
				tv1.setPadding(20, 15, 0, 15);
				tv1.setTextSize(20);
				ly11.addView(tv1);

				LinearLayout ly12 = new LinearLayout(MainActivity.this);
				ly12.setOrientation(LinearLayout.VERTICAL);
				ly12.setBackgroundColor(Color.rgb(110, 110, 110));	
				ly1.addView(ly12);

				if(isExpanded){
					TextView tv3 = new TextView(MainActivity.this);
					tv3.setText("▼");
					tv3.setTextColor(Color.BLACK);
					tv3.setPadding(115, 15, 0, 15);
					tv3.setTextSize(20);
					ly12.addView(tv3);
				}
				else{
					TextView tv3 = new TextView(MainActivity.this);
					tv3.setText("▶");
					tv3.setTextColor(Color.BLACK);
					tv3.setPadding(115, 15, 0, 15);
					tv3.setTextSize(20);
					ly12.addView(tv3);
				}

*/
				return ly;
			}
		}

		public boolean hasStableIds() {
			return false;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

	}

}