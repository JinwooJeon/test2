package com.example.dressfolio3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class IntroActivity extends Activity {
	
	Handler h;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
		
		h = new Handler();
		h.postDelayed(irun, 2000);
		
	}
	Runnable irun = new Runnable(){
		public void run(){
			Intent introToMain = new Intent(IntroActivity.this, MainActivity.class);
			startActivity(introToMain);
			finish();
			
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
	};
	
	// ���� 3�� ������ �ڷΰ��� ������ ����ȭ������ ���ư��� �ڵ鷯 ����.
	public void onBackPressed(){
		super.onBackPressed();
		h.removeCallbacks(irun);
	}
}