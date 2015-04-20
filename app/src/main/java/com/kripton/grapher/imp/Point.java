package com.kripton.grapher.imp;

public class Point {
	
	public double x;
	public double y;
	
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
		
	}
	
	
	public Point() {
		// TODO Auto-generated constructor stub
	}
	
	
	public void getCopy(Point p) {
		this.x = p.x;
		this.y = p.y;
		this.firstPoint = p.firstPoint;
	}
	
	
	public volatile boolean firstPoint = false;
}