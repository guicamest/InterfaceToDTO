package com.sleepcamel.ifdtoutils;

import com.sleepcamel.ifdtoutils.utils.ClassUtils;


public class InterfaceDTOInfo<T> {

	private static final String SUFFIX = "DTO";

	private Class<T> interfaceClass;

	private InterfaceDTOInfo(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public static <T> InterfaceDTOInfo<T> getInfo(Class<T> interfaceClass) {
		return new InterfaceDTOInfo<T>(interfaceClass);
	}

	private String getDTOPackage() {
		String packageName = interfaceClass.getPackage().getName();
		String subpackageName = null;
		ToDTO annotation = interfaceClass.getAnnotation(ToDTO.class);
		if ( annotation != null ){
			String fullPackage = annotation.fullPackage();
			if ( fullPackage != null && !fullPackage.isEmpty() ){
				return normalizePackage(fullPackage);
			}
			subpackageName = annotation.packageSuffix();
		}
		
		subpackageName = normalizePackage(subpackageName);
		if ( !subpackageName.isEmpty() ){
			subpackageName = ClassUtils.PACKAGE_SEPARATOR + subpackageName;
		}

		return packageName + subpackageName;
	}

	protected String normalizePackage(String subpackageName) {
		if ( subpackageName == null ){
			return "";
		}
		if ( subpackageName.startsWith(".") ){
			subpackageName = subpackageName.substring(1);
		}
		if ( subpackageName.endsWith(".") ){
			subpackageName = subpackageName.substring(0, subpackageName.length()-1);
		}
		return subpackageName;
	}

	private String getDTOClassName() {
		String className = interfaceClass.getName();
		
		String suffixToUse = SUFFIX;
		ToDTO annotation = interfaceClass.getAnnotation(ToDTO.class);
		if ( annotation != null && annotation.dtoSuffix() != null && !annotation.dtoSuffix().isEmpty() ){
			suffixToUse = annotation.dtoSuffix();
		}

		className = ClassUtils.getClassNameFromFullPackage(className) + suffixToUse;
		
		return className;
	}

	public String getDTOCanonicalName() {
		return getDTOPackage() + ClassUtils.PACKAGE_SEPARATOR + getDTOClassName();
	}

}
