package com.kripton.grapher.ui;



import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.kripton.grapher.R;


/* C */


public class CustomKeyboardView extends KeyboardView implements OnKeyboardActionListener {

	/*
    public CustomKeyboardView() {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customKeyboardView = (KeyboardView)inflater.inflate(R.layout.custom_keyboard, null);

        customKeyboardView.setOnKeyboardActionListener(this);
    }
	*/
	

    public CustomKeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	public CustomKeyboardView(Context context, AttributeSet attrd, int defStyle) {
		super(context, attrd, defStyle);
	}

	

	public void init(Activity activity) {
        setOnKeyboardActionListener(this);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	
	
	/* In this view we will insert text */
	public void setEditableView(View v) {
        editableView = (ExpressionView)v;
	}
	
	
	
	@Override
	public void onKey(int keyCode, int[] arg1) {
		
		String key = keys[keyCode];
		String append = "";
        boolean shiftCursor = false;
        boolean deleted = false;

        int currentCursorPos = editableView.getSelectionEnd();

		if(editableView != null)
        {
			String expression = editableView.getText().toString();


			if(expression != null) {
				
				/* Hide keyboard key */
				if(key.equals("OK"))
                {
					this.setVisibility(View.GONE);
					this.setEnabled(false);
				}
				
				/* Backspace key */
				else if(key.equals("DEL"))
                {
					if(expression.length() > 0)
                    {
                        /* Delete one symbol before current position */
                        String firstPart = expression.substring(0, currentCursorPos-1);
						String secondPart = expression.substring(currentCursorPos);

                        expression = firstPart+secondPart;
                        deleted = true;
					}
				}
				
				/* Change keyboard to text keyboard */
				else if(key.equals("SET_TEXT")) {
					
				}
				
				/* Change keyboard to function keyboard */
				else if(key.equals("SET_FUNC")) {
					
				}

				/* Any other key */
				else
                {
					append = key;
				}

                String resultExpression = "";

                if(key.equals("sqrt()") || key.equals("abs()"))
                {
                    shiftCursor = true;
                }



                /* If we just delete one symbol or have unexpected */
                /* cutting from expression */
                if(currentCursorPos <= expression.length() && !deleted)
                {
                    resultExpression = expression.substring(0, currentCursorPos);
                    resultExpression += append + expression.substring(currentCursorPos);
                }
                else
                {
                    resultExpression = expression;
                }

				/* Set new expression and move cursor */
				editableView.setText(resultExpression);

                /* For positioning current selection */
                int appendLength = append.length();

                if(!shiftCursor && !deleted)
                {
                    editableView.setSelection(currentCursorPos+appendLength);
                }
                else
                {
                    editableView.setSelection(currentCursorPos+appendLength-1);
                }
			}
		}
		
	}

	
	@Override
	public void onPress(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRelease(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onText(CharSequence arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void swipeDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void swipeLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void swipeRight() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void swipeUp() {
		// TODO Auto-generated method stub
		
	}


    private KeyboardView customKeyboardView;

    private final String[] keys =  {"1","2","3", "+", "x", "SET_TEXT",
            "4", "5", "6", "-", "(", "SET_FUNC",
            "7", "8", "9", "*", ")", "DEL",
            "0", ".", "sqrt()", "/", "abs()", "OK" };


    private ExpressionView editableView = null;

}
