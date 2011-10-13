package com.sleepcamel.ifdtoutils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.CtPrimitiveType;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

public class DTOClassGenerator {

	private static final String SUFFIX = "DTO";

	public static <T> Class<T> generateDTOForInterface(Class<T> interfaceClass) {
		return generateDTOForInterface(interfaceClass, "");
	}
	
	public static <T> Class<T> generateDTOForInterface(Class<T> interfaceClass, String dtoSubPackage) {
		return generateDTOForInterface(interfaceClass, dtoSubPackage, Thread.currentThread().getContextClassLoader(), null);
	}
	
	public static <T> Class<T> generateDTOForInterface(Class<T> interfaceClass, String dtoSubPackage, String outputDirectory) {
		return generateDTOForInterface(interfaceClass, dtoSubPackage, Thread.currentThread().getContextClassLoader(), outputDirectory);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Class<T> generateDTOForInterface(Class<T> interfaceClass, String dtoSubPackage, ClassLoader classLoader, String outputDirectory) {
		if ( dtoSubPackage == null ){
			dtoSubPackage = "";
		}
		
		if ( !dtoSubPackage.isEmpty() ){
			dtoSubPackage = "." + dtoSubPackage;
		}

		try{
		LoaderClassPath loaderClassPath = new LoaderClassPath(classLoader);
			
		ClassPool pool = ClassPool.getDefault();
		pool.appendClassPath(loaderClassPath);

		CtClass cc = pool.makeClass(getDTOName(interfaceClass, dtoSubPackage));
		
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
		for(CtMethod method:getInterfaceClassMethods(interfaceCtClass)){
			if ( !isExportableMethod(method) ){
				continue;
			}
			
			// Get Field
			CtField field = getFieldFromMethod(cc,method);
			field.setModifiers(field.getModifiers() | Modifier.PUBLIC);
			cc.addField(field);

			// Copy Interface Method
			CtMethod fieldMethod = CtNewMethod.copy(method, cc, null);
			fieldMethod.setModifiers(fieldMethod.getModifiers() - Modifier.ABSTRACT);
			fieldMethod.setBody("return "+field.getName()+";");
			
			cc.addMethod(fieldMethod);
		}
	}

	private static List<CtMethod> getInterfaceClassMethods(CtClass interfaceCtClass) {
		List<CtMethod> methodsList = new ArrayList<CtMethod>();
		methodsList.addAll(Arrays.asList(interfaceCtClass.getDeclaredMethods()));
		try {
			CtClass[] superclasses = interfaceCtClass.getInterfaces();
			for(CtClass superInterfaceClass:superclasses){
				List<CtMethod> interfaceClassMethods = getInterfaceClassMethods(superInterfaceClass);
				for(CtMethod method:interfaceClassMethods){
					if ( !methodsList.contains(method) ){
						methodsList.add(method);
					}
				}
			}
		} catch (NotFoundException e) {
		}
		return methodsList;
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


	private static boolean isExportableMethod(CtMethod method) throws NotFoundException{
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
	
	public static List<Method> getExportableMethods(Class<?> interfaceClass){
		List<Method> methods = new ArrayList<Method>();
		for(Method method:interfaceClass.getDeclaredMethods()){
			if ( isExportableMethod(method) ){
				methods.add(method);
			}
		}
		return methods;
	}
	
	public static boolean isExportableMethod(Method method){
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
	
	public static String getDTOName(Class<?> class1, String dtoSubPackage) {
		String packageName = class1.getPackage().getName();

		if ( !dtoSubPackage.isEmpty() && !dtoSubPackage.startsWith(".") ){
			dtoSubPackage = "." + dtoSubPackage;
		}

		String className = class1.getName();
		className = className.substring(class1.getName().indexOf(packageName)+packageName.length()+1);
		return packageName + dtoSubPackage + "." + className + SUFFIX;
	}
	
}
