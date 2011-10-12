package com.sleepcamel.ifdtoUtils;

class Circle implements IDrawable{

	private double radius;
	private String circleProperty1 = "";
	private String circleProperty2 = "";
	private String circleProperty3 = "";
	private String circleProperty4 = "";

	public Circle(double radius) {
		super();
		this.radius = radius;
	}

	@Override
	public double getArea() {
		return Math.PI*radius*radius;
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