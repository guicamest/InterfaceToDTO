package com.sleepcamel.ifdtoUtils;

import java.lang.reflect.Method;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import com.sleepcamel.ifdtoutils.DTOInterface;

public class EntityTypeProcessor {

	public static boolean processes(Method method) {
		return method.getName().equals(DTOInterface.class.getMethods()[0].getName());
	}

	public static Object process(Object fieldValue, String className, boolean useFullPackage) {
		Object toReturn = fieldValue;
		if ( StringUtils.isBlank((CharSequence) fieldValue) ){
			toReturn = className;
			if ( !useFullPackage ){
				toReturn = ClassUtils.getShortClassName(className);
			}
		}
		return toReturn;
	}

}
