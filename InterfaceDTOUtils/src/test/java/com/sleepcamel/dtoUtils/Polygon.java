package com.sleepcamel.dtoUtils;

import java.util.ArrayList;
import java.util.List;

public class Polygon implements IDrawable {

	List<Double> sides = new ArrayList<Double>();
	
	public Polygon(List<Double> sides) {
		super();
		this.sides = sides;
	}

	@Override
	public double getArea() {
		// No idea
		return -1;
	}

	@Override
	public boolean isHasSides() {
		return true;
	}

	@Override
	public boolean nonUsableMethod(String something) {
		return false;
	}

	@Override
	public void calculate() {
	}

}
