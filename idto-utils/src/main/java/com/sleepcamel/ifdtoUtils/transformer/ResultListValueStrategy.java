package com.sleepcamel.ifdtoUtils.transformer;

import java.util.ArrayList;
import java.util.List;

public class ResultListValueStrategy implements ILastValueStrategy {

	private static ILastValueStrategy instance;

	private ResultListValueStrategy(){}
	
	public static ILastValueStrategy instance() {
		if ( instance == null ){
			instance = new ResultListValueStrategy();
		}
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object get(Object object, Object result) {
		if ( object == null ){
			object = new ArrayList<Object>();
		}
		((List<Object>)object).add(result);
		return object;
	}

}
