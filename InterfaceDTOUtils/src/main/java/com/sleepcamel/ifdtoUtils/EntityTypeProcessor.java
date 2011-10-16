package com.sleepcamel.ifdtoUtils;

import java.lang.reflect.Method;

import com.sleepcamel.ifdtoutils.DTOInterface;
import com.sleepcamel.ifdtoutils.utils.ClassUtils;

public class EntityTypeProcessor {

	public static boolean processes(Method method) {
		return method.getName().equals(DTOInterface.class.getMethods()[0].getName());
	}

	public static Object process(Object fieldValue, String className, boolean useFullPackage) {
		Object toReturn = fieldValue;
		if ( fieldValue == null || fieldValue.equals("") ){
			toReturn = className;
			if ( !useFullPackage ){
				toReturn = ClassUtils.getClassNameFromFullPackage(className);
			}
		}
		return toReturn;
	}

}
