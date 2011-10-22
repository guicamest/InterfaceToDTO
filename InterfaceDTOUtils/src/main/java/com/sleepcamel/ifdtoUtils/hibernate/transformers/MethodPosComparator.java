package com.sleepcamel.ifdtoUtils.hibernate.transformers;

import java.lang.reflect.Method;
import java.util.Comparator;

import com.sleepcamel.ifdtoutils.Pos;

public class MethodPosComparator implements Comparator<Method> {

	private static MethodPosComparator instance;

	@Override
	public int compare(Method o1, Method o2) {
		Integer o1Pos = -Integer.MAX_VALUE;
		Integer o2Pos = Integer.MAX_VALUE;
		Pos o1annotation = o1.getAnnotation(Pos.class);
		Pos o2annotation = o2.getAnnotation(Pos.class);
		if ( o1annotation != null ){
			o1Pos = o1annotation.value();
		}
		if ( o2annotation != null ){
			o2Pos = o2annotation.value();
		}
		return o1Pos.compareTo(o2Pos);
	}

	public static Comparator<Method> instance() {
		if ( instance == null ){
			instance = new MethodPosComparator();
		}
		return instance;
	}

}
