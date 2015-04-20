package com.kripton.grapher.UIManager.UIBuilder.ViewFactories;

import android.content.Context;
import android.view.View;

import com.kripton.grapher.R;
import com.kripton.grapher.UIManager.UIBuilder.UIBuilder.ViewFactory;
import com.kripton.grapher.ui.OptionsGearView;

public class OptionsGearViewFactory implements ViewFactory {

	
	private Context context;
	
	public OptionsGearViewFactory(Context context) {
		this.context = context;
	}
	
	
	@Override
	public View createView() {
		OptionsGearView optionsGearView = new OptionsGearView(context);
		optionsGearView.setImageResource(R.drawable.options_gear);
		return optionsGearView;
	}
	

}
