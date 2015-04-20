package com.kripton.grapher.ui;


import com.kripton.grapher.handlers.MultiTouchHandler;
import com.kripton.grapher.imp.Graph;
import com.kripton.grapher.imp.Point;
import com.kripton.grapher.imp.Pool;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager.LayoutParams;


public class GraphView extends SurfaceView implements SurfaceHolder.Callback {

	
	public GraphView(Context context, Pool<Point> pool) {
		super(context);
        getHolder().addCallback(this);
	}
	

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("Events", "Surface Created");
		float viewWidth = this.getWidth();
		float viewHeight = this.getHeight();
		float centerX = viewWidth / 2;
		float centerY = viewHeight / 2;

        Log.d("screen", "Width: "+Float.toString(viewWidth));
        Log.d("screen", "Height: "+Float.toString(viewHeight));
		
		drawThread = new DrawThread(getHolder());
		
		drawThread.setDrawInfo(centerX, centerY, scale, viewWidth, viewHeight);
		drawThread.start();
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		drawThread.terminate();
	}
	
	
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void addGraph(Graph graph) {
		drawThread.addGraph(graph);
	}

	public void deleteGraph(Graph graph) {
		drawThread.deleteGraph(graph);
	}

	public int getScale() {
		return scale;
	}

    public void setMultiTouchHandler(MultiTouchHandler handler) {
        multiTouchHandler = handler;
    }



    private DrawThread drawThread;

    /* pixels per one step */
    private int scale = 50;

    private MultiTouchHandler multiTouchHandler;
}
