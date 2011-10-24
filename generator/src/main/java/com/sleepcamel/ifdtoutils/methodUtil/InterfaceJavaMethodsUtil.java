package com.sleepcamel.ifdtoutils.methodUtil;

import java.lang.reflect.Method;

import com.sleepcamel.ifdtoutils.Pos;

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
	Method[] getInterfaceMethods(Class<?> clazz) {
		return clazz.getDeclaredMethods();
	}

	@Override
	Class<?>[] getSuperInterfaces(Class<?> clazz) {
		return clazz.getInterfaces();
	}

	@Override
	Class<?> getReturnType(Method method) {
		return method.getReturnType();
	}

	@Override
	String getName(Method method) {
		return method.getName();
	}

	@Override
	Class<?> getVoidClass() {
		return void.class;
	}

	@Override
	Class<?>[] getParameterTypes(Method method) {
		return method.getParameterTypes();
	}

	public Method getMethodWithPosition(Class<? extends Object> dtoClass, Integer methodIndex) {
		Method[] declaredMethods = dtoClass.getDeclaredMethods();
		for(Method method:declaredMethods){
			Pos annotation = method.getAnnotation(Pos.class);
			if ( annotation != null && annotation.value() == methodIndex )
				return method;
		}
		return null;
	}

}
