package com.kripton.grapher.GraphCalculator;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.kripton.grapher.exceptions.CalculateException;
import com.kripton.grapher.imp.Point;
import com.kripton.grapher.imp.Pool;

/*----------------------------CALCULATOR BEGIN----------------------------*/
public class Calculator extends GraphCalculatorParent  {
	
	
	public Calculator(float viewWidth, float scaledViewHeight, float scale, Pool<Point> pool, List<Point> pointsList) {
		this.graphViewWidth = viewWidth;
		this.scaledViewHeight = scaledViewHeight;
		this.scale = scale;
		this.pointsPool = pool;
		this.pointsList = pointsList;
	}
	
	
	public Calculator() {
		// TODO Auto-generated constructor stub
	}


	/*---------------------------------CALCULATE pointsListList FOR DRAWING------------------------------*/
	@SuppressWarnings("unchecked")
	public void calculatePoints(Stack<String> stackRPN, float shiftX, float shiftY) throws CalculateException {
		
		calculated = false;
		accessRangeError = false;
		
		Stack<String> stackRPNCopy = new Stack<String>();
		stackRPNCopy = (Stack<String>)stackRPN.clone();
		
		boolean first = true;
		
		Point lastPoint = new Point(0, 0);
		
		/* get max value on 'x' axis */
		//int maxX = (int)(graphViewWidth/scale/2)+1;
		int maxX = (int)((graphViewWidth/2 - shiftX)/scale)+1;
		int minX = (int)(((graphViewWidth/2 + shiftX)/scale)+1)*-1;

		/* Get max and min values on 'y' axis */
		float maxY = (graphViewHeight/2 + shiftY)/scale;
		float minY = (graphViewHeight/2 - shiftY)/scale*-1;

		Log.d("TEvents", "X: " + Integer.toString(maxX) + ":" + Integer.toString(minX));
		Log.d("TEvents", "Y: " + Float.toString(maxY) + ":" + Float.toString(minY));

		synchronized(pointsList) {
			for(float i=minX; i<maxX; i++) {
				for(float j=0; j < scale; j++) {
					
					float xPoint = i+j/scale;
					
					stackRPN = (Stack<String>)stackRPNCopy.clone();
					stackCalculate.clear();
					
					//Get y coordinate for current point
					calculatePostNotation(stackRPN, xPoint);
					
					//if we have no point with this x we add null point
					//this way we tell that we have interruption
					if(accessRangeError) {
						accessRangeError = false;
						pointsList.add(null);
						continue;
					}


					if(!stackCalculate.isEmpty()) {
						
						/* Get new point from pool */
						/* If point has been created before it mays contain first point field as true */
                        /* from last operations on it */
						Point currentPoint =  pointsPool.newObject();
						currentPoint.firstPoint = false;
						
						/* Multiple on -1 because y-axis is reversed */
						currentPoint.x = xPoint;
						currentPoint.y = -1*stackCalculate.pop();
						
						/* if after calculating we have more than one number in result */
						if(!stackCalculate.empty()) {												 
							throw new CalculateException("Unknown error while calculating");		
						}						
						
						
						/* if current point is first we continue */
						if(first) {
							currentPoint.firstPoint = true;
							lastPoint = currentPoint;
							first = false;
							continue;
						} 

						
						/* if last and current pointsListList are both out of visible part of screen */
						if((lastPoint.y > maxY || lastPoint.y < minY)
								&& (currentPoint.y > maxY || currentPoint.y < minX)) {

							/* if pointsListList are placed besides different parts of screen */
							/* dividing height by 2 because pointsListList coordinates calculated from middle */
							if((lastPoint.y > maxY && currentPoint.y < minY)
									|| (lastPoint.y < minY && currentPoint.y > maxY)) {
								
								if(lastPoint.firstPoint) {
									pointsList.add(lastPoint);
								}
								pointsList.add(currentPoint);
								
							}
							else {
								if(lastPoint.firstPoint) {
									currentPoint.firstPoint = true;
								}
								lastPoint = currentPoint;
								pointsList.add(null);
								continue;
							}
						}
						
						if(lastPoint.firstPoint) {
							pointsList.add(lastPoint);
						}
						
						pointsList.add(currentPoint);
						lastPoint = currentPoint;
					}
					else {
						throw new CalculateException("Result stack is empty");
					}
				}
			}
			pointsPool.logFree();
			calculated = true;
		}

	}	
	
	
	
	/* CALCULATE FUNCTION IN POST NOTATION AFTER PARSING. USED IN CALCULATE pointsListList FUNCTION FOR EACH POINT. */
	private void calculatePostNotation(Stack<String> stackRPN, float xPoint) throws CalculateException {
		
		float buf = 0.0f;
		
		while(!stackRPN.isEmpty()) {
			
			if(isNumber(stackRPN.lastElement())) {
				buf = Float.parseFloat(stackRPN.pop());
				stackCalculate.push(buf);
			}
			else if(isVariable(stackRPN.lastElement())) {
				
				if(stackRPN.lastElement().charAt(0) == 'x') {
					stackCalculate.push(xPoint);
					stackRPN.pop();
				}
				else if(variables.containsKey(stackRPN.lastElement())) {
					buf = variables.get(stackRPN.pop());       
					stackCalculate.push(buf);
				}
				else {
					throw new CalculateException("Unknown variable");
				}
				
			}
			else if(stackRPN.lastElement().equals("+")) {
				
				if(stackCalculate.size() >= 2) {
					stackRPN.pop();
					float first = stackCalculate.pop();
					
					if(stackCalculate.isEmpty()) {
						throw new CalculateException("Less than 2 numbers in stack when trying to substract");
					}
					
					float second = stackCalculate.pop();
					stackCalculate.push(first+second);
				}
				else {
					throw new CalculateException("Less than 2 numbers in stack when trying to sum");
				}
				
			}
			else if(stackRPN.lastElement().equals("-")) {
				
				if(stackCalculate.size() >= 2) {					
					stackRPN.pop();
					float first = stackCalculate.pop();
					
					if(stackCalculate.isEmpty()) {
						throw new CalculateException("Less than 2 numbers in stack when trying to substract");
					}
					
					float second = stackCalculate.pop();
					stackCalculate.push(first-second);
				}
				else {
					throw new CalculateException("Less than 2 numbers in stack when trying to substract");						
				}
				
			}
			else if(stackRPN.lastElement().equals("*")) {
				
				if(stackCalculate.size() >= 2) {
					stackRPN.pop();
					float first = stackCalculate.pop(); 
					float second = stackCalculate.pop();
					stackCalculate.push(first*second);
				}
				else {
					throw new CalculateException("Less than 2 numbers in calculate stack when trying to multiple");
				}
				
			}
			else if(stackRPN.lastElement().equals("/")) {
				
				if(stackCalculate.size() >= 2)
				{
					stackRPN.pop();
					float first = stackCalculate.pop();
					float second = stackCalculate.pop();
					if(second == 0)
					{
						accessRangeError = true;
						break;
					}
					stackCalculate.push(second/first);
				}
				else
				{
					throw new CalculateException("Less than 2 numbers in stack when trying to divide");
				}
				
			}
			else if(stackRPN.lastElement().equals("^"))
			{
				stackRPN.pop();
				float first = stackCalculate.pop();
				
				if(stackCalculate.isEmpty())
				{
					throw new CalculateException("Less than 2 numbers in stack");
				}
				
				float second = stackCalculate.pop();
				stackCalculate.push((float)Math.pow(first, second));
				
			}
			else if(isFunction(stackRPN.lastElement()))
			{
				String function = stackRPN.pop();
				
				//If we have no number to call function with
				if(stackCalculate.isEmpty())
				{
					throw new CalculateException("Calculate stack is empty when call function");
				}
				
				float first = stackCalculate.pop();
				stackCalculate.push((float)callFunction(function, first));
				if(accessRangeError)
				{
					break;
				}
				
			}
			else if(stackRPN.lastElement().equals("=")) {
				
				return;
				
			}
			else {
				
				throw new CalculateException("Unknown symbol: "+stackRPN.lastElement());
				
			}
		}
		
	}
	/*---------------------------END CALCULATE POST NOTATION---------------------------------*/
	
	
	/*----------------------------CALL FUNCTION BY IT'S NAME--------------------------- */
	private double callFunction(String func, float val) {
		if(func.equals("abs")) {
			return Math.abs(val);
		}
		else if(func.equals("sin")) {
			return Math.sin(val);		
		}
		else if(func.equals("asin")) {
			if(val < -1 || val > 1) {
				accessRangeError = true;
				return 0;
			}
			return Math.asin(val);
		}
		else if(func.equals("sinh")) {
			return Math.sinh(val);
		}
		else if(func.equals("cos")) {
			return Math.cos(val);
		}
		else if(func.equals("acos")) {
			if(val < -1 || val > 1) {
				accessRangeError = true;
				return 0;
			}
			return Math.acos(val);
		}
		else if(func.equals("cosh")) {
			return Math.cosh(val);
		}
		else if(func.equals("tan")) {
			return Math.tan(val);
		}
		else if(func.equals("atan")) {
			return Math.atan(val);
		}
		else if(func.equals("tanh")) {
			return Math.tanh(val);
		}
		else if(func.equals("cot")) {
			return 1.0 / Math.tan(val);
		}
		else if(func.equals("acot")) {        
			float cot = (float)(1.0 / Math.tan(val));
			return Math.atan(1.0 / cot);
		}		
		else if(func.equals("coth")) {
			return 1/Math.tanh(val);     
		}
		else if(func.equals("ln")) {
			if(val <= 0 ) {
				accessRangeError = true;
				return 0;
			}
			return Math.log(val);
		}
		else if(func.equals("log")) {
			if(val <= 0) {
				accessRangeError = true;
				return 0;
			}
		
			return Math.log10(val);
		}
		else if(func.equals("sqrt")) {
			if(val < 0) {
				accessRangeError = true;
				return 0;
			}
			return Math.sqrt(val);
		}
		else if(func.equals("exp")) {
			return Math.exp(val);
		}
		
		return 0;
	}
	
	
	/*
	private boolean outOfScreen(Point point) {
		if(point.y > maxY || point.y < minY) {
			return true;
		}
		return false;
	}
	*/
	
	
	public boolean isCalculated() {
		return calculated;
	}
	
	
	public boolean hasAccessRangeError() {
		return accessRangeError;
	}
	
	
	public void setPointsList(List<Point> pointsList) {
		this.pointsList = pointsList;
	}
	
	
	public void setPointsPool(Pool<Point> pointsPool) {
		this.pointsPool = pointsPool;
	}
	
	
	public void setGraphViewInfo(int viewWidth, int viewHeight, int _scale) {
		graphViewWidth = viewWidth;
		graphViewHeight = viewHeight;
		scaledViewHeight = viewHeight / _scale;
		scale = _scale;
	}
	
	
	private boolean calculated = false;
	private boolean accessRangeError = false;
	private float scaledViewHeight = 0.0f;
	private float graphViewWidth = 0.0f;
	private float graphViewHeight = 0.0f;
	private float scale = 0.0f;

	private Stack<Float> stackCalculate = new Stack<Float>();
	private Map<String, Float> variables = new HashMap<String, Float>();
	
	private List<Point> pointsList = null;
	
	private Pool<Point> pointsPool = null;
	
}
