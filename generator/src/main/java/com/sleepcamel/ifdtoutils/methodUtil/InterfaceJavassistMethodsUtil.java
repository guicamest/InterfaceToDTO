package com.sleepcamel.ifdtoutils.methodUtil;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class InterfaceJavassistMethodsUtil extends InterfaceMethodsUtilBase<CtMethod, CtClass> {

	private static InterfaceJavassistMethodsUtil instance;

	private InterfaceJavassistMethodsUtil() {
	}
	
	public static InterfaceJavassistMethodsUtil instance(){
		if ( instance == null ){
			instance = new InterfaceJavassistMethodsUtil();
		}
		return instance;
	}
	
	@Override
	CtClass getReturnType(CtMethod method) throws NotFoundException {
		return method.getReturnType();
	}

	@Override
	String getName(CtMethod method) {
		return method.getName();
	}

	@Override
	CtClass getVoidClass() {
		return CtClass.voidType;
	}

	@Override
	CtClass[] getParameterTypes(CtMethod method) throws NotFoundException {
		return method.getParameterTypes();
	}
	
	@Override
	CtMethod[] getInterfaceMethods(CtClass clazz) {
		return clazz.getDeclaredMethods();
	}

	@Override
	CtClass[] getSuperInterfaces(CtClass clazz) throws Exception {
		return clazz.getInterfaces();
	}

}
