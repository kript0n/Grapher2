package com.kripton.grapher.UIManager.UIBuilder;

import android.inputmethodservice.Keyboard;
import android.view.View;



public class UIBuilder {
	
	
	public UIBuilder() {

	}	
	
	public interface ViewFactory {		
		public View createView();
	}
	

	public interface KeyboardFactory {
		public Keyboard createKeyboard();	
	}


	
	
	public static View createView(ViewFactory factory) {
		return factory.createView();
	}
	
	
	public static Keyboard createKeyboard(KeyboardFactory factory) {
		return factory.createKeyboard();
	}
	
	
}
