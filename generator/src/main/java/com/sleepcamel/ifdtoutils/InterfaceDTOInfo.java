package com.sleepcamel.ifdtoutils;


public class InterfaceDTOInfo<T> {

	private static final String SUFFIX = "DTO";

	private static final String PACKAGE_SEPARATOR = ".";
	
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
			subpackageName = annotation.packageSuffix();
		}
		
		subpackageName = normalizePackage(subpackageName);
		if ( !subpackageName.isEmpty() ){
			subpackageName = PACKAGE_SEPARATOR + subpackageName;
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
		String packageName = interfaceClass.getPackage().getName();
		String className = interfaceClass.getName();
		
		className = className.substring(className.indexOf(packageName)+packageName.length()+1) + SUFFIX;
		
		return className;
	}

	public String getDTOCanonicalName() {
		return getDTOPackage() + PACKAGE_SEPARATOR + getDTOClassName();
	}

}
