package com.kripton.grapher.GraphCalculator;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;


import android.util.Log;

import com.kripton.grapher.exceptions.CalculateException;
import com.kripton.grapher.exceptions.ParseErrorException;
import com.kripton.grapher.imp.Point;
import com.kripton.grapher.imp.Pool;

public class GraphCalculator implements Runnable {
	
	public GraphCalculator() {
		
	}

	
	@Override
	public void run() {
		while(true) {
			synchronized(mutex) {
				
				while(!running) {
					try {
						mutex.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				/* If thread should be terminated */
				if(finished) {
					return;
				}
				
				/* Calculate points */
				if(shouldBeCalculated) {
					try {
						changer.infixToPostNotation(expression);
						calculator.calculatePoints(changer.getStackRPN(), shiftX, shiftY);
					} catch (ParseErrorException e) {
						Log.d("ParseErrorException", e.getMessage());
						e.printStackTrace();
					} catch (CalculateException e) {
						Log.d("CalculateException", e.getMessage());
						e.printStackTrace();
					}
					finally {
						shouldBeCalculated = false;
					}
				}
				
				/* When points are calculated pausing this thread */
				pause();
				
			}
		}
		
	}
	
	
	public void start() {
		running = false;
		shouldBeCalculated = false;
		graphCalculatorThread = new Thread(this);
		graphCalculatorThread.start();
	}
	
	
	public void terminate() {
		finished = true;
		while(true) {
			Log.d("Threads", "Trying to terminate calculator");
			try {
				synchronized(mutex) {
					if(running = false) {
                        resume();
                        graphCalculatorThread.join();
                    }
				}
				break;
			} catch (InterruptedException e) {
				Log.d("Threads", "Terminating exception");
				e.printStackTrace();
			}
		}
		Log.d("Threads", "Calculator is terminated");
	}
	


	/* Used for pausing thread when there is nothing to calculate */
	public void pause() {
		running = false;
	}
	
	
	/* Used for resuming thread when new data is prepared for calculation.*/
	/* SHOULD BE USED IN SYNCHRONIZED ON 'mutex' BLOCK */
	private void resume() {
		running = true;	
		mutex.notify();
	}
	
	
	/* Called from UI thread when new data has already been prepared */
	/* Should set pool, points list and graph view info before calculating */
	public void calculatePoints(float _shiftX, float _shiftY) {

		shiftX = _shiftX;
		shiftY = _shiftY;

		synchronized(mutex) {
			shouldBeCalculated = true;
			if(running == false) {
				resume();
			}
		}
	}
	
	
	/* SHOULD BE CALLED BEFORE CALCULATING  */
	/* Called from Graph */
	public void setGraphViewInfo(int viewWidth, int viewHeight, int _scale) {
		calculator.setGraphViewInfo(viewWidth, viewHeight, _scale);
	}
	

	/* SHOULD BE CALLED BEFORE CALCULATING */
	/* Called from Graph */
	public void setPointsPool(Pool<Point> pointsPool) {
		calculator.setPointsPool(pointsPool);
	}
	
	
	/* SHOULD BE CALLED BEFORE CALCULATING */
	/* Called from Graph */
	public void setPointsList(List<Point> pointsList) {
		calculator.setPointsList(pointsList);
	}
	
	
	public boolean isCalculated() {
		return calculator.isCalculated();
	}



	/*------------COUNTING THREADS FOR DEBUG-------------*/
	public static int threadCount = 0;
	/*---------------------------------------------------*/


	/*--------------FUNCTION EXPRESISON-------------*/
	public String expression = "";
	/*----------------------------------------------*/


	/*---------------COLLECTIONS FOR SHUNTING-YARD ALGHORITHM------------*/

	private Map<String, Double> variables = new HashMap<String, Double>();
	/*-------------------------------------------------------------------*/


	/*-----------------------VARIABLES FOR SHANTING-YARD ALGHORITHM------------------------*/
	private final String OPERATIONS = "+-*/^";
	private final String SEPARATOR = ",";
	private final String[] FUNCTIONS = {"abs", "sin", "asin", "sinh", "cos", "acos","cosh", "tan", "atan", "tanh", "cot", "acot", "coth", "exp",
			"ln", "log", "pow", "sqrt",};
	/*-------------------------------------------------------------------------------------*/


	/*------------------Errors----------------------*/
	private boolean accessRangeError = false;
	/*-----------------End errors-------------------*/


	/*------------IF THIS FLAG IS SET UP TRUE WE WILL CALCULATE POINTS------------*/
	private volatile boolean shouldBeCalculated = false;
	/*----------------------------------------------------------------------------*/


	/*-----------------THREAD, RUN FLAG AND MUTEX---------------*/
	private Thread graphCalculatorThread;
	private volatile boolean running = false;
	private volatile boolean finished = false;
	private final Object mutex = new Object();
	/*----------------------------------------------------------*/


	/*-----------------OBJECTS FOR CALCULATION---------------*/
	private NotationChanger changer = new NotationChanger();
	private Calculator calculator = new Calculator();
	/*-------------------------------------------------------*/


	/* Axises shifts */
	private float shiftX = 0.0f;
	private float shiftY = 0.0f;

}
