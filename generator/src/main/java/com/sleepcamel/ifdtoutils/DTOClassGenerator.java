package com.sleepcamel.ifdtoutils;

import java.lang.reflect.Modifier;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

public class DTOClassGenerator {

	public static <T> Class<T> generateDTOForInterface(Class<T> interfaceClass) {
		return generateDTOForInterface(interfaceClass, null);
	}
	
	public static <T> Class<T> generateDTOForInterface(Class<T> interfaceClass, String outputDirectory) {
		return generateDTOForInterface(interfaceClass, Thread.currentThread().getContextClassLoader(), outputDirectory);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Class<T> generateDTOForInterface(Class<T> interfaceClass, ClassLoader classLoader, String outputDirectory) {
		try{
		LoaderClassPath loaderClassPath = new LoaderClassPath(classLoader);
			
		ClassPool pool = ClassPool.getDefault();
		pool.appendClassPath(loaderClassPath);

		CtClass cc = pool.makeClass(InterfaceDTOInfo.getInfo(interfaceClass).getDTOCanonicalName());
		
		// Add default constructor, it's just a DTO
		CtConstructor defaultConstructor = CtNewConstructor.defaultConstructor(cc);
		cc.addConstructor(defaultConstructor);
		
		// Set it implements the given interface
		CtClass interfaceCtClass = pool.get(interfaceClass.getName());
		cc.addInterface(interfaceCtClass);
		
		// Add interface methods and fields
		addMethodsAndFields(cc, interfaceCtClass);

		// Persist class
		if ( outputDirectory == null || outputDirectory.isEmpty() ){
			cc.writeFile();
		}else{
			cc.writeFile(outputDirectory);
		}
		return cc.toClass();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private static void addMethodsAndFields(CtClass cc, CtClass interfaceCtClass) throws CannotCompileException, NotFoundException {
		for(CtMethod method:InterfaceJavassistMethodsUtil.instance().getExportableMethods(interfaceCtClass)){
			// Get Field
			CtField field = getFieldFromMethod(cc,method);
			field.setModifiers(field.getModifiers() | Modifier.PUBLIC);
			cc.addField(field);

			// Copy Interface Method
			CtMethod fieldMethod = new CtMethod(method.getReturnType(), method.getName(), method.getParameterTypes(), cc);
			fieldMethod.setModifiers(fieldMethod.getModifiers() - Modifier.ABSTRACT);
			fieldMethod.setBody("return "+field.getName()+";");
			
			cc.addMethod(fieldMethod);
		}
	}
	
	private static CtField getFieldFromMethod(CtClass cc, CtMethod method) throws CannotCompileException, NotFoundException {
		return new CtField(method.getReturnType(), getFieldNameFromMethod(method.getName()), cc);
	}

	public static String getFieldNameFromMethod(String name) {
		int i=0;
		for(Character c:name.toCharArray()){
			if ( Character.isUpperCase(c) ){
				return Character.toLowerCase(c) + name.substring(i+1);
			}
			i++;
		}
		return name;
	}
	
}
