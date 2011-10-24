package com.sleepcamel.ifdtoutils.methodInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodInfo<MT> {

	private Map<MethodInfoKey, MT> getters = new HashMap<MethodInfoKey, MT>();
	private Map<MethodInfoKey, MT> setters = new HashMap<MethodInfoKey, MT>();
	
	public void addToGetters(String methodName, int i, MT method){
		MethodInfoKey methodInfoKey = new MethodInfoKey(methodName, i);
		if ( !getters.containsKey(methodInfoKey) ){
			getters.put(methodInfoKey, method);
		}
	}
	
	public void addToSetters(String methodName, int i, MT method){
		MethodInfoKey methodInfoKey = new MethodInfoKey(methodName, i);
		if ( !setters.containsKey(methodInfoKey) ){
			setters.put(methodInfoKey, method);
		}
	}
	
	public Map<MethodInfoKey, MT> getSettersInfo() {
		return setters;
	}
	
	public Map<MethodInfoKey, MT> getGettersInfo() {
		return getters;
	}

	public List<MT> getGetters() {
		return new ArrayList<MT>(getGettersInfo().values());
	}
	
}
