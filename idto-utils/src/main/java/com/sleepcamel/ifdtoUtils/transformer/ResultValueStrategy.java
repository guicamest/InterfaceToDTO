package com.sleepcamel.ifdtoUtils.transformer;


public class ResultValueStrategy implements ILastValueStrategy {

	private static ILastValueStrategy instance;

	private ResultValueStrategy(){}
	
	public static ILastValueStrategy instance() {
		if ( instance == null ){
			instance = new ResultValueStrategy();
		}
		return instance;
	}

	@Override
	public Object get(Object object, Object result) {
		return result;
	}

}
