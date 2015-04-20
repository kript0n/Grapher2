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

import com.kripton.grapher.imp.Graph;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;


public class DrawThread implements Runnable {
	

	public DrawThread(SurfaceHolder surfaceHolder) {
		this.surfaceHolder = surfaceHolder;
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

        running = false;
		Log.d("Threads", "Running has been set to false. Start terminating calculators");
		/* Terminating calculators */
		ListIterator<Graph> iter  = GraphList.listIterator();
		while(iter.hasNext()) {
			iter.next().terminateCalculator();
		}
		Log.d("Threads", "Calculators has been terminated");
		

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
		
		while(running) {
			
			try {
				
				canvas = surfaceHolder.lockCanvas();
				
				if(!surfaceHolder.getSurface().isValid()) {
					continue;
				}
					
				drawAxises(canvas);

				synchronized(GraphList) {
					if(!GraphList.isEmpty()) {
						ListIterator<Graph> graphIter = GraphList.listIterator(); 
						Graph graph = null;
						while(graphIter.hasNext()) {
							graph = graphIter.next();
							if(graph.isCalculated()) {
								graph.drawGraph(canvas, paint, centerX, centerY, scale);
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
	private void drawAxises (Canvas canvas) {
		
		//Clear screen
		paint.setColor(Color.WHITE);
		canvas.drawPaint(paint);
		
		//Set paint for drawing axises
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(2);
		
		//Draw axises with shift
		canvas.drawLine(0, centerY+shiftY, viewWidth , centerY+shiftY, paint);
		canvas.drawLine(centerX+shiftX, 0, centerX+shiftX, viewHeight, paint);
		
		//Set font for writing marking
		Typeface font = Typeface.defaultFromStyle(Typeface.ITALIC);
		paint.setTypeface(font);		
		paint.setTextSize(20);
		
		
		//Draw zero in the middle
		canvas.drawText("0", centerX-15, centerY+20, paint);
		canvas.drawCircle(centerX, centerY, 2, paint);
		
		//Draw horizontal marking
		int maxX =(int) (viewWidth / scale / 2)+1;
		for(int i=1;i<maxX;i++) {
			
			//Write right horizontal marking
			canvas.drawText(String.valueOf(i), centerX+(i*scale)-8, centerY+20, paint);		
			canvas.drawCircle(centerX+(i*scale), centerY, 2, paint);
			
			//Write left horizontal marking
			canvas.drawText(String.valueOf(-i), centerX-(i*scale)-8, centerY+20, paint);
			canvas.drawCircle(centerX-(i*scale), centerY, 2, paint);			
			
		}
		
		//Draw vertical marking
		int maxY = (int) (viewHeight / scale / 2)+1;
		for(int j = 1; j < maxY; j++) {
			
			//Write top vertical marking
			canvas.drawText(String.valueOf(j), centerX+8, centerY-(j*scale)+8, paint);
			canvas.drawCircle(centerX, centerY-(j*scale), 2, paint);
			
			//Write bottom vertical marking
			canvas.drawText(String.valueOf(-j), centerX+8, centerY+(j*scale)+8, paint);
			canvas.drawCircle(centerX, centerY+(j*scale), 2, paint);	
			
		}
	}
	/*-----------------------------------------------------------------*/
	
	
	public void addGraph(Graph graph) {

        graph.calculatePoints();

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

            while(iter.hasNext()) {
				graphFromList = iter.next();
				if(graph == graphFromList) {
					graphFromList.freePoints();
					iter.remove();
					break;
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
	
	
	private Thread renderThread;
	private List<Graph> GraphList = Collections.synchronizedList(new ArrayList<Graph>());

	private SurfaceHolder surfaceHolder;
	private Paint paint = new Paint();
	
	public volatile boolean running = false;
	
	private float centerX;
	private float centerY;
	private float scale;
	private float shiftY;
	private float shiftX;
	private float viewHeight;
	private float viewWidth;

}