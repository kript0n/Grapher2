package com.kripton.grapher.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import com.kripton.grapher.GraphCalculator.GraphCalculator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;


public class Graph {
	
	private Pool<Point> pool = null;
	
	private GraphCalculator graphCalculator = new GraphCalculator();;
	
	private List<Point> points = Collections.synchronizedList(new ArrayList<Point>());
	
	private int color = 0;
	private static boolean colorsInitialised = false;
	private static Stack<Integer> colors = new Stack<Integer>(); 
	
	
	public Graph() {
		
		initialiseColors();
		
		color = (colors.isEmpty()) ? Color.BLACK : colors.pop();
		Log.d("Info", Boolean.toString(colors.isEmpty()));
		Log.d("Info", Integer.toString(color));
		
		graphCalculator.setPointsList(points);
		graphCalculator.start();
	}
	
	
	public void freePoints() {
		synchronized(points) {
			pool.freeList(points);
		}
	}
	
	
	public void drawGraph(Canvas canvas, Paint paint, float centerX, float centerY, float scale) {
		
		paint.setStrokeWidth(3);
		paint.setColor(color);
		
		
		boolean firstPoint = true;			
		
		/* Used for draw line from last point to current one */
		float lastX = 0;										
		float lastY = 0;	
		
		synchronized(points) {
			ListIterator<Point> pointsIter = points.listIterator();
			while(pointsIter.hasNext()) {
				
				Point point = pointsIter.next();
				
				/* If we have interruption in graph */
				if(point == null) {									
					firstPoint = true;
					continue;
				}	
	
				float xPoint = (float)(centerX+point.x*scale);
				float yPoint = (float)(centerY+point.y*scale);
				
				if(firstPoint) {
					lastX = xPoint;
					lastY = yPoint;
					firstPoint = false;
					continue;
				}
				
				canvas.drawLine(lastX, lastY, xPoint, yPoint, paint);
				
				lastX = xPoint;
				lastY = yPoint;
				
			}
		}
		
	}
	
	
	public void setGraphViewInfo(int screenWidth, int screenHeight, int _scale) {
		graphCalculator.setGraphViewInfo(screenWidth, screenHeight, _scale);
	}
	
	
	public void setPointPool(Pool<Point> pool) {
		this.pool = pool;
		graphCalculator.setPointsPool(pool);
	}
	
	
	public void setExpression(String exp) {
		graphCalculator.expression= exp;
	}
	
	
	public String getExpression() {
		return graphCalculator.expression;
	}
	
	
	public boolean isCalculated() {
		return graphCalculator.isCalculated();
	}
	
	
	/* Called from ExpFuncView in 'afterTextChanged' */
	public void calculatePoints() {				
		graphCalculator.calculatePoints();
	}
	
	
	public void terminateCalculator() {			
		graphCalculator.terminate();
	}
	
	
	public void startCalculator() {
		graphCalculator.start();
	}
	

	private void initialiseColors() {
		if(!colorsInitialised) {
			colors.add(Color.BLACK);
			colors.add(Color.RED);
			colors.add(Color.BLUE);
			colors.add(Color.YELLOW);
			colors.add(Color.GREEN);
			colors.add(Color.GRAY);
			colors.add(Color.DKGRAY);
			
			colorsInitialised = true;
		}
	}
	
		
}
