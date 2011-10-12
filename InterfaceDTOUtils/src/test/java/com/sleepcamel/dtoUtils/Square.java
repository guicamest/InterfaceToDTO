package com.sleepcamel.dtoUtils;

class Square implements IDrawable{

	private double size;
	private String squareProperty1 = "";
	private String squareProperty2 = "";
	private String squareProperty3 = "";
	private String squareProperty4 = "";

	public Square(double d) {
		super();
		this.size = d;
	}

	@Override
	public double getArea() {
		return size*size;
	}

	@Override
	public boolean isHasSides() {
		return true;
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