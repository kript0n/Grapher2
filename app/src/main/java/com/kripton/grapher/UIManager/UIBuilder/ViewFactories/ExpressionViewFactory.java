package com.kripton.grapher.UIManager.UIBuilder.ViewFactories;

import com.kripton.grapher.R;
import com.kripton.grapher.UIManager.UIBuilder.UIBuilder.ViewFactory;
import com.kripton.grapher.imp.Point;
import com.kripton.grapher.imp.Pool;
import com.kripton.grapher.ui.CustomKeyboardView;
import com.kripton.grapher.ui.ExpressionView;
import com.kripton.grapher.ui.GraphView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class ExpressionViewFactory implements ViewFactory {

	
	private Context context = null;
	private GraphView graphView = null;
	private Pool<Point> pointsPool = null;
	private CustomKeyboardView keyboard = null;
	
	
	
	public ExpressionViewFactory(Context _context, GraphView _graphView, Pool<Point> _pointsPool,
			CustomKeyboardView _keyboard) {
		context = _context;
		graphView = _graphView;
		pointsPool = _pointsPool;
		keyboard = _keyboard;
		
	}
	

	@SuppressLint("InflateParams")
	@Override
	public View createView()
    {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ExpressionView nView = (ExpressionView) inflater.inflate(R.layout.expression_view, null, false);
		nView.setGraphView(graphView);
		nView.setPointsPool(pointsPool);


        nView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus)
                {
                    openKeyboard(v);
                }
                else
                {
                    hideKeyboard();
                }

            }
        });


        /* There is the bug with opening default keyboard */
        nView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                EditText edittext = (EditText) v;
                int inType = edittext.getInputType();       // Backup the input type
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);              // Restore input type
                return true;                                // Consume touch event
            }
        });

        return nView;
	}

	
	
	private void openKeyboard(View v) {

		keyboard.setVisibility(View.VISIBLE);
        keyboard.setEnabled(true);
		keyboard.setEditableView(v);
		if(v != null) {
			((InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}


    private void hideKeyboard() {
        keyboard.setVisibility(View.GONE);
        keyboard.setEnabled(false);
    }


}
