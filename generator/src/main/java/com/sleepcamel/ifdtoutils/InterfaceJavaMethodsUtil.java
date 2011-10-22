package com.sleepcamel.ifdtoutils;

import java.lang.reflect.Method;

public class InterfaceJavaMethodsUtil extends InterfaceMethodsUtilBase<Method, Class<?>> {

	private static InterfaceJavaMethodsUtil instance;

	private InterfaceJavaMethodsUtil() {
	}
	
	public static InterfaceJavaMethodsUtil instance(){
		if ( instance == null ){
			instance = new InterfaceJavaMethodsUtil();
		}
		return instance;
	}
	
	@Override
	boolean isExportableMethod(Method method) {
		// Skip methods with parameters
		if ( method.getParameterTypes().length != 0 ){
			return false;
		}
		
		// Skip methods with void return type
		if ( method.getReturnType().equals(void.class) ){
			return false;
		}
		return true;
	}

	@Override
	Method[] getInterfaceMethods(Class<?> clazz) {
		return clazz.getDeclaredMethods();
	}

	@Override
	Class<?>[] getInterfaceSuperInterfaces(Class<?> clazz) {
		return clazz.getInterfaces();
	}

	public Method getMethodWithPosition(Class<? extends Object> dtoClass, Integer methodIndex) {
		Method[] declaredMethods = dtoClass.getDeclaredMethods();
		for(Method method:declaredMethods){
			Pos annotation = method.getAnnotation(Pos.class);
			if ( annotation.value() == methodIndex )
				return method;
		}
		return null;
	}

}
