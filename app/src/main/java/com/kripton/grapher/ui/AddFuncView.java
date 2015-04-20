package com.kripton.grapher.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

public class AddFuncView extends ImageView {

	LayoutParams lp = new LayoutParams();
		
	public AddFuncView(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}
	
	public AddFuncView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public AddFuncView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	
	private void init() {
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		lp.verticalMargin = 0.0f;
		lp.horizontalMargin = 0.0f;
		lp.gravity = Gravity.TOP;
		this.setLayoutParams(lp);
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
