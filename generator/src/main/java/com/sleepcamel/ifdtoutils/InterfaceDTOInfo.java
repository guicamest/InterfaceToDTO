package com.sleepcamel.ifdtoutils;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;


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
			if ( StringUtils.isNotBlank(fullPackage) ){
				return normalizePackage(fullPackage);
			}
			subpackageName = annotation.packageSuffix();
		}
		
		subpackageName = normalizePackage(subpackageName);
		if ( StringUtils.isNotBlank(subpackageName) ){
			subpackageName = ClassUtils.PACKAGE_SEPARATOR + subpackageName;
		}

		return packageName + subpackageName;
	}

	protected String normalizePackage(String subpackageName) {
		if ( StringUtils.isBlank(subpackageName) ){
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
		
		if ( annotation != null ){
			if ( StringUtils.isNotBlank(annotation.dtoName()) ){
				className = annotation.dtoName();
				suffixToUse = "";
			}else{
				if ( StringUtils.isNotBlank(annotation.dtoSuffix()) ){
					suffixToUse = annotation.dtoSuffix();
				}
			}
		}

		className = ClassUtils.getShortClassName(className) + suffixToUse;
		
		return className;
	}

	public String getDTOCanonicalName() {
		return getDTOPackage() + ClassUtils.PACKAGE_SEPARATOR + getDTOClassName();
	}

}
