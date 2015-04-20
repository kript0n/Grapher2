package com.kripton.grapher.ui;

import android.content.Context;
import android.inputmethodservice.Keyboard;

public class NumbersKeyboard extends Keyboard {

	
	public NumbersKeyboard(Context context, int xmlLayoutResId) {
		super(context, xmlLayoutResId);
	}
	
	
	public NumbersKeyboard(Context context, int xmlLayoutResId, int modeId, int width, int height) {
		super(context, xmlLayoutResId, modeId, width, height);
	}
	
	
	public NumbersKeyboard(Context context, int xmlLayoutResId, int modeId) {
		super(context, xmlLayoutResId, modeId);
	}
	
	
	public NumbersKeyboard(Context context, int layoutTemplateResId, CharSequence characters,
			int columns, int horizontalPadding) {
		super(context, layoutTemplateResId, characters, columns, horizontalPadding);
	}

}
