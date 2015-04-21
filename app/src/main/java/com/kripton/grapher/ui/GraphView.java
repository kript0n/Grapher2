package com.kripton.grapher.ui;


import com.kripton.grapher.handlers.MultiTouchHandler;
import com.kripton.grapher.imp.Graph;
import com.kripton.grapher.imp.Point;
import com.kripton.grapher.imp.Pool;
import com.kripton.grapher.interfaces.Input;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager.LayoutParams;

import java.util.List;
import java.util.ListIterator;


public class GraphView extends SurfaceView implements SurfaceHolder.Callback, Runnable
{
	
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

		multiTouchHandler = new MultiTouchHandler(this, 1, 1);

		drawThread = new DrawThread(getHolder(), multiTouchHandler);
		
		drawThread.setDrawInfo(centerX, centerY, scale, viewWidth, viewHeight);
		drawThread.start();

		eventsThread = new Thread(this);
		running = true;
		eventsThread.start();
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


	@Override
	public void run() {

		int firstMoveX = 0;
		int firstMoveY = 0;
		int lastMoveX = 0;
		int lastMoveY = 0;

		while(running)
		{
			boolean hasFirstMove = false;
			boolean dragged = false;

			//synchronized (multiTouchHandler)
			//{
				List<Input.TouchEvent> events = multiTouchHandler.getTouchEvents();
				ListIterator<Input.TouchEvent> eventsIter = events.listIterator();

				while (eventsIter.hasNext()) {
					Input.TouchEvent event = eventsIter.next();

					if (event.type == Input.TouchEvent.TOUCH_DRAGGED) {
						if (!hasFirstMove) {
							firstMoveX = event.x;
							firstMoveY = event.y;
							hasFirstMove = true;
						} else {
							lastMoveX = event.x;
							lastMoveY = event.y;
							dragged = true;
						}
					}
				}
			//}

			if(dragged)
			{
				shiftX += lastMoveX - firstMoveX;
				shiftY += lastMoveY - firstMoveY;

				drawThread.calcWithNewShift(shiftX, shiftY);
			}
		}


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
	private Thread eventsThread;

    /* pixels per one step */
    private int scale = 50;

	private int shiftX = 0;
	private int shiftY = 0;

    private MultiTouchHandler multiTouchHandler;
	private volatile boolean running = false;



}
