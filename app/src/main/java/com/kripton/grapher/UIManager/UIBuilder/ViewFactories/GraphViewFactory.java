package com.kripton.grapher.UIManager.UIBuilder.ViewFactories;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.kripton.grapher.UIManager.UIBuilder.UIBuilder.ViewFactory;
import com.kripton.grapher.imp.Point;
import com.kripton.grapher.imp.Pool;
import com.kripton.grapher.ui.GraphView;

public class GraphViewFactory implements ViewFactory {
	
	private Context context;
	private Pool<Point> pointsPool;
	
	
	public GraphViewFactory(Context context, Pool<Point> pointsPool) {
		this.context = context;
		this.pointsPool = pointsPool;
	}
	
	
	@Override
	public View createView() {
        GraphView nview = new GraphView(context, pointsPool);
        nview.setBackgroundColor(Color.WHITE);
		return nview;

	}
	

}
