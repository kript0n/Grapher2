package com.kripton.grapher.UIManager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kripton.grapher.R;
import com.kripton.grapher.UIManager.UIBuilder.UIBuilder;
import com.kripton.grapher.UIManager.UIBuilder.KeyboardFactories.NumbersKeyboardFactory;
import com.kripton.grapher.UIManager.UIBuilder.ViewFactories.AddFunctionViewFactory;
import com.kripton.grapher.UIManager.UIBuilder.ViewFactories.CustomKeyboardViewFactory;
import com.kripton.grapher.UIManager.UIBuilder.ViewFactories.ExpressionViewFactory;
import com.kripton.grapher.UIManager.UIBuilder.ViewFactories.GraphViewFactory;
import com.kripton.grapher.UIManager.UIBuilder.ViewFactories.OptionsGearViewFactory;
import com.kripton.grapher.imp.Point;
import com.kripton.grapher.imp.Pool;
import com.kripton.grapher.imp.Pool.PoolObjectFactory;
import com.kripton.grapher.ui.AddFuncView;
import com.kripton.grapher.ui.CustomKeyboardView;
import com.kripton.grapher.ui.ExpressionView;
import com.kripton.grapher.ui.GraphView;
import com.kripton.grapher.ui.NumbersKeyboard;
import com.kripton.grapher.ui.OptionsGearView;

public class UIManager {
	
	
	public UIManager(Activity _activity, Context _context) {
		activity = _activity;
		context = _context;
	}


    /* Create and show UI elements */
    public void createUI() {
        init();
        functionLayout.addView(addFunctionView);
        graphLayout.addView(mainGraphView);
        contentLayout.addView(customKeyboardView);
        //graphLayout.addView(optionsGearView);
    }

	
	/* Creates all UI elements and factories */
	private void init() {
		
		getBaseUI();
		createPointsPool();

		/* Create factories and views */
		graphViewFactory = new GraphViewFactory(context, pointsPool);
		addFunctionViewFactory = new AddFunctionViewFactory(context, this);
		optionsGearViewFactory = new OptionsGearViewFactory(context);
		customKeyboardViewFactory = new CustomKeyboardViewFactory(context);
		numbersKeyboardFactory = new NumbersKeyboardFactory(context);
		
		/* Get views for creating expression view factory */
		mainGraphView = (GraphView) UIBuilder.createView(graphViewFactory);
		customKeyboardView = (CustomKeyboardView) UIBuilder.createView(customKeyboardViewFactory);
        customKeyboardView.init(activity);

		
		/* Create expression view factory */
        /* Connects KeyboardView with expression view */
		expressionViewFactory = new ExpressionViewFactory(context, mainGraphView, pointsPool, customKeyboardView);
		
		/* Create another views */
		addFunctionView = (AddFuncView) UIBuilder.createView(addFunctionViewFactory);
		//optionsGearView = (OptionsGearView) UIBuilder.createView(optionsGearViewFactory);
		
		/* Create keyboards */
		numbersKeyboard = (NumbersKeyboard) UIBuilder.createKeyboard(numbersKeyboardFactory);
		
		/* Keyboard by default */
		customKeyboardView.setKeyboard(numbersKeyboard);
	}


    /* Get layouts and root view */
    private void getBaseUI() {
        rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        contentLayout = (RelativeLayout) rootView.findViewById(R.id.content_layout);
        graphLayout = (RelativeLayout) rootView.findViewById(R.id.graph_layout);
        functionLayout = (LinearLayout) rootView.findViewById(R.id.func_layout);
    }


    /* Create points pool */
    private void createPointsPool() {
        pointsFactory = createPoolFactory();
        pointsPool = new Pool<Point>(pointsFactory, POINTS_POOL_MAX);
    }



	/* Creates and add to function layout new expression view */
	public void createExpressionView() {
		ExpressionView expressionView = (ExpressionView) UIBuilder.createView(expressionViewFactory);
		functionLayout.addView(expressionView, 0);
	}

	
	/* Creates points pool factory */
	private PoolObjectFactory<Point> createPoolFactory() {
		return new PoolObjectFactory<Point>() {

			@Override
			public Point createObject() {
				return new Point();
			}
			
		};
	}
	
	
	
	
	
	
	public void onCreate() {
		this.createUI();
	}
	
	
	public void onStart() {
	}
	
	
	public void onStop() {
	}
	
	
	public void onRestart() {
	}
	
	
	public void onResume() {
	}
	
	
	public void onPause() {
	}

	
	
	public void onDestroy() {
	}
	
	
	
	/* For finding views by ID */
	private Activity activity = null;
	private Context context = null;
	private View rootView = null;
	
	
	/* Points pool and pool factory */
	private Pool<Point> pointsPool;
	private PoolObjectFactory<Point> pointsFactory;
	
	/* LAYOUTS */
    private RelativeLayout contentLayout = null;
	private RelativeLayout graphLayout = null;
	private LinearLayout functionLayout = null;
	
	/* VIEWS */
	private OptionsGearView optionsGearView = null;
	private GraphView mainGraphView = null;
	private AddFuncView addFunctionView = null;
	private CustomKeyboardView customKeyboardView = null;
	
	/* VIEW FACTORIES */
	private ExpressionViewFactory expressionViewFactory = null;
	private GraphViewFactory graphViewFactory = null;
	private AddFunctionViewFactory addFunctionViewFactory = null;
	private OptionsGearViewFactory optionsGearViewFactory = null;
	private CustomKeyboardViewFactory customKeyboardViewFactory = null;
	
	/* KEYBOARD FACTORIES */
	private NumbersKeyboardFactory numbersKeyboardFactory = null;
	
	/* KEYBOARDS */
	NumbersKeyboard numbersKeyboard = null;
	
	
	/* CONSTANTS */
	private final int POINTS_POOL_MAX = 10000;
	
}
