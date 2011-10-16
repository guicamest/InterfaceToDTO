package com.sleepcamel.ifdtoutils.utils;

public class ClassUtils {

	public static final String PACKAGE_SEPARATOR = ".";
	
	public static String getClassNameFromFullPackage(String className) {
		return className.substring(className.lastIndexOf(PACKAGE_SEPARATOR)+1);
	}
	
}
