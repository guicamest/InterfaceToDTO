package com.sleepcamel.ifdtoutils;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtPrimitiveType;
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
	boolean isExportableMethod(CtMethod method) throws NotFoundException {
		// Skip methods with parameters
		if ( method.getParameterTypes().length != 0 ){
			return false;
		}
		
		// Skip methods with void return type
		if ( method.getReturnType().equals(CtPrimitiveType.voidType) ){
			return false;
		}
		return true;
	}

	@Override
	CtMethod[] getInterfaceMethods(CtClass clazz) {
		return clazz.getDeclaredMethods();
	}

	@Override
	CtClass[] getInterfaceSuperInterfaces(CtClass clazz) throws Exception {
		return clazz.getInterfaces();
	}

}
