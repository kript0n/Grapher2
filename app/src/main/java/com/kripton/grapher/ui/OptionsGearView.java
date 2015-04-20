package com.kripton.grapher.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

public class OptionsGearView extends ImageView  {
	
	LayoutParams lp = new LayoutParams();
	
	public OptionsGearView(Context context) {																																																											
		super(context);
		lp.gravity = Gravity.RIGHT;
		this.setLayoutParams(lp);
		//this.setBackground(getResources().getDrawable(R.drawable.options_gear));
	}

}
