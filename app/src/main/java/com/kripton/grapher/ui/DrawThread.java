package com.kripton.grapher.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.SurfaceHolder;

import com.kripton.grapher.handlers.MultiTouchHandler;
import com.kripton.grapher.imp.Graph;
import com.kripton.grapher.interfaces.Input;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;


public class DrawThread implements Runnable {
	

	public DrawThread(SurfaceHolder _surfaceHolder, MultiTouchHandler _touchHandler) {

		surfaceHolder = _surfaceHolder;
		touchHandler = _touchHandler;
		shiftY = 0;
		shiftX = 0;

	}
	
	
	public void start() {

		running = true;
		renderThread = new Thread(this);
		renderThread.start();
		
		/* Starting calculators if resumed */
		ListIterator<Graph> iter = GraphList.listIterator();
		while(iter.hasNext()) {
			iter.next().startCalculator();
		}
	}
	
	
	public void terminate() {

		Log.d("Threads", "Start terminating calculators");

		/* Terminating calculators */
		ListIterator<Graph> iter  = GraphList.listIterator();
		while(iter.hasNext())
		{
			iter.next().terminateCalculator();
		}
		Log.d("Threads", "Calculators has been terminated");

		running = false;
		while(true) {
			try {
				renderThread.join();
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}


	}
	
	
	@Override
	public void run() {

		Canvas canvas = null;
		
		/* Set anti-aliasing */
		paint.setFlags(ANTI_ALIAS_FLAG);

		int firstMoveX = 0;
		int firstMoveY = 0;
		int lastMoveX = 0;
		int lastMoveY = 0;

		Graph graph = null;

		while(running)
		{
			boolean hasFirstMove = false;
			boolean dragged = false;

			List<Input.TouchEvent> events = touchHandler.getTouchEvents();

			for(int i=0; i<events.size(); i++)
			{
				Input.TouchEvent event = events.get(i);

				if (event.type == Input.TouchEvent.TOUCH_DRAGGED)
				{
					if (!hasFirstMove)
					{
						firstMoveX = event.x;
						firstMoveY = event.y;
						hasFirstMove = true;
					} else
					{
						lastMoveX = event.x;
						lastMoveY = event.y;
						dragged = true;
					}
				}
			}

			if(dragged)
			{
				shiftX += lastMoveX - firstMoveX;
				shiftY += lastMoveY - firstMoveY;

				calcWithNewShift(shiftX, shiftY);
			}

			float shiftedCenterY = centerY + shiftY;
			float shiftedCenterX = centerX + shiftX;

			try {
				
				canvas = surfaceHolder.lockCanvas();
				
				if(!surfaceHolder.getSurface().isValid()) {
					continue;
				}
					
				drawAxises(canvas, shiftedCenterX, shiftedCenterY);

				synchronized(GraphList)
				{
					if(!GraphList.isEmpty())
					{
						//ListIterator<Graph> graphIter = GraphList.listIterator();

						//while(graphIter.hasNext())
						for(int i=0; i<GraphList.size(); i++)
						{
							graph = GraphList.get(i);
							//graph = graphIter.next();
							if(graph.isCalculated())
							{
								graph.drawGraph(canvas, paint, shiftedCenterX, shiftedCenterY, scale);
							}
						}
					}
				}
			} 
			finally {
				if(canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
	

	
	/*------------------DRAW X AND Y AXISES--------------------------*/
	private void drawAxises (Canvas canvas, float shiftedCenterX, float shiftedCenterY) {

		//Clear screen
		if(paint != null)
		{
			paint.setColor(Color.WHITE);
			canvas.drawPaint(paint);
		}

		//Set paint for drawing axises
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(2);
		
		//Draw axises with shift
		canvas.drawLine(0, shiftedCenterY, viewWidth , shiftedCenterY, paint);
		canvas.drawLine(shiftedCenterX, 0, shiftedCenterX, viewHeight, paint);
		
		//Set font for writing marking
		Typeface font = Typeface.defaultFromStyle(Typeface.ITALIC);
		paint.setTypeface(font);		
		paint.setTextSize(20);
		
		
		//Draw zero in the middle
		canvas.drawText("0", shiftedCenterX-15, shiftedCenterY+20, paint);
		canvas.drawCircle(shiftedCenterX, shiftedCenterY, 2, paint);
		
		//Draw horizontal marking
		int maxX = (int)((viewWidth/2 - shiftX)/scale)+1;
		int minX = (int)(((viewWidth/2 + shiftX)/scale)+1)*-1;

		for(int i=minX;i<maxX;i++)
		{
			//Write right horizontal marking
			canvas.drawText(String.valueOf(i), shiftedCenterX+(i*scale)-8, shiftedCenterY+20, paint);
			canvas.drawCircle(shiftedCenterX+(i*scale), shiftedCenterY, 2, paint);
		}
		
		//Draw vertical marking
		int maxY = (int)((viewHeight/2 + shiftY)/scale)+1;
		int minY = (int)((viewHeight/2 - shiftY)/scale+1)*-1;

		for(int j = minY; j < maxY; j++)
		{
			//Write top vertical marking
			canvas.drawText(String.valueOf(j), shiftedCenterX+8, shiftedCenterY-(j*scale)+8, paint);
			canvas.drawCircle(shiftedCenterX, shiftedCenterY-(j*scale), 2, paint);
		}
	}
	/*-----------------------------------------------------------------*/
	
	
	public void addGraph(Graph graph) {

        graph.calculatePoints(shiftX, shiftY);

        synchronized(GraphList)
        {
			GraphList.add(graph);
		}

	}
	
	
	public void deleteGraph(Graph graph) {

        synchronized(GraphList)
        {
			ListIterator<Graph> iter = GraphList.listIterator();
			Graph graphFromList = null;

            while(iter.hasNext())
			{
				graphFromList = iter.next();
				if(graph == graphFromList)
				{
					graphFromList.freePoints();
					iter.remove();
					break;
				}
			}
		}
	}


	public void calcWithNewShift(int _shiftX, int _shiftY) {

		synchronized (GraphList)
		{
			shiftX = _shiftX;
			shiftY = _shiftY;

			ListIterator<Graph> iter = GraphList.listIterator();
			Graph graphFromList = null;

			while(iter.hasNext())
			{
				graphFromList = iter.next();
				if(graphFromList.isCalculated())
				{
					graphFromList.calculatePoints(_shiftX, _shiftY);
				}
			}
		}
	}
	
	
	
	public void setDrawInfo(float centerX, float centerY, float scale, float viewWidth, float viewHeight) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.scale = scale;
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;
	}


	private MultiTouchHandler touchHandler;
	
	private Thread renderThread;
	private List<Graph> GraphList = Collections.synchronizedList(new ArrayList<Graph>());

	private SurfaceHolder surfaceHolder;
	private Paint paint = new Paint();
	
	public volatile boolean running = false;
	
	private float centerX;
	private float centerY;
	private float scale;
	private volatile int shiftY;
	private volatile int shiftX;
	private float viewHeight;
	private float viewWidth;

}