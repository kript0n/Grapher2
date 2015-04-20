package com.kripton.grapher.main;

import com.kripton.grapher.R;
import com.kripton.grapher.UIManager.UIManager;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	
	private UIManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);	
		manager = new UIManager(this, getBaseContext());
		manager.onCreate();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		manager.onStart();
	}
	
	
	protected void onRestart() {
		super.onRestart();
		manager.onRestart();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		manager.onResume();
	}
	
	
	@Override 
	protected void onPause() {
		super.onPause();
		manager.onPause();
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
		manager.onStop();
	}
	
	
	@Override 
	protected void onDestroy() {
		super.onDestroy();
		manager.onDestroy();
	}
	
	
	
}
