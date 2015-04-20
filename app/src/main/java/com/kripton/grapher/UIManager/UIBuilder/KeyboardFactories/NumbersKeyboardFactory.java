package com.kripton.grapher.UIManager.UIBuilder.KeyboardFactories;

import com.kripton.grapher.R;
import com.kripton.grapher.UIManager.UIBuilder.UIBuilder.KeyboardFactory;
import com.kripton.grapher.ui.NumbersKeyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;

public class NumbersKeyboardFactory implements KeyboardFactory {

	Context context = null;
	NumbersKeyboard keyboard = null;
	
	
	public NumbersKeyboardFactory(Context context) {
		this.context = context; 
	}
	

	@Override
	public Keyboard createKeyboard() {
		if(keyboard == null) {
			keyboard = new NumbersKeyboard(context, R.layout.numbers_keyboard);
		}
		
		return keyboard;
	}

	
}
