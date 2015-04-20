package com.kripton.grapher.UIManager.UIBuilder.ViewFactories;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.kripton.grapher.R;
import com.kripton.grapher.UIManager.UIManager;
import com.kripton.grapher.UIManager.UIBuilder.UIBuilder.ViewFactory;
import com.kripton.grapher.ui.AddFuncView;

public class AddFunctionViewFactory implements ViewFactory, OnClickListener {

	
	private Context context = null;
	private UIManager manager = null;
	
	public AddFunctionViewFactory(Context context, UIManager manager) {
		this.context = context;
		this.manager = manager;
	}
	
	
	@Override
	public View createView() {
		
		AddFuncView addFunctionView = new AddFuncView(context);
		addFunctionView.setImageResource(R.drawable.add_function_button);
		addFunctionView.setOnClickListener(this);
		return addFunctionView;
	
	}


	@Override
	public void onClick(View v) {
		manager.createExpressionView();
	}

}
