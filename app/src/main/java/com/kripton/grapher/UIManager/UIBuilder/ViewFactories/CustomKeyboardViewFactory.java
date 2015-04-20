package com.kripton.grapher.UIManager.UIBuilder.ViewFactories;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kripton.grapher.R;
import com.kripton.grapher.UIManager.UIBuilder.UIBuilder.ViewFactory;
import com.kripton.grapher.ui.CustomKeyboardView;

public class CustomKeyboardViewFactory implements ViewFactory  {


	
	public CustomKeyboardViewFactory(Context _context) {

        inflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	
	@Override
	public View createView() {

		if(customKeyboardView == null)
        {
			customKeyboardView = (CustomKeyboardView)inflater.inflate(R.layout.custom_keyboard, null);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                   RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            customKeyboardView.setLayoutParams(lp);
		}

		return customKeyboardView;

	}


    private LayoutInflater inflater = null;
    private KeyboardView customKeyboardView = null;

}
