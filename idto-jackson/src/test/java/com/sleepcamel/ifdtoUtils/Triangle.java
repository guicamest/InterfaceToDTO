package com.sleepcamel.ifdtoUtils;

public class Triangle implements IDrawable{

	private double a;
	private double b;
	private double c;

	public Triangle(double a, double b, double c) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
	}

	@Override
	public double getArea() {
		double p = (a+b+c)/2;
		return Math.sqrt(p*(p-a)*(p-b)*(p-c));
	}

	@Override
	public boolean isHasSides() {
		return false;
	}

	@Override
	public boolean nonUsableMethod(String something) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void calculate() {
		// TODO Auto-generated method stub
		
	}
}