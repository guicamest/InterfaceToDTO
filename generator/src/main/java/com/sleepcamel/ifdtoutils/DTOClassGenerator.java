package com.sleepcamel.ifdtoutils;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Map.Entry;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import org.apache.commons.lang3.StringUtils;

import com.sleepcamel.ifdtoutils.methodInfo.MethodInfo;
import com.sleepcamel.ifdtoutils.methodInfo.MethodInfoKey;
import com.sleepcamel.ifdtoutils.methodUtil.InterfaceJavassistMethodsUtil;
import com.sleepcamel.ifdtoutils.processor.ExportProcessor;
import com.sleepcamel.ifdtoutils.processor.IAnnotationProcessor;
import com.sleepcamel.ifdtoutils.processor.ISetterProcessor;
import com.sleepcamel.ifdtoutils.processor.NullProcessor;

public class DTOClassGenerator {

	public static <T> Class<T> generateDTOForInterface(Class<T> interfaceClass) {
		return generateDTOForInterface(interfaceClass, null);
	}
	
	public static <T> Class<T> generateDTOForInterface(Class<T> interfaceClass, boolean generateMethodPositions) {
		return generateDTOForInterface(interfaceClass, classLoader(), generateMethodPositions, false, null);
	}
	
	public static <T> Class<T> generateDTOForInterface(Class<T> interfaceClass, boolean generateMethodPositions, boolean generateSetters) {
		return generateDTOForInterface(interfaceClass, classLoader(), generateMethodPositions, generateSetters, null);
	}
	
	public static <T> Class<T> generateDTOForInterface(Class<T> interfaceClass, String outputDirectory) {
		return generateDTOForInterface(interfaceClass, classLoader(), false, false, outputDirectory);
	}
	
	private static ClassLoader classLoader(){
		return Thread.currentThread().getContextClassLoader();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Class<T> generateDTOForInterface(Class<T> interfaceClass, ClassLoader classLoader, boolean generateMethodPositions, boolean generateSetters, String outputDirectory) {
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
		addMethodsAndFields(cc, interfaceCtClass, generateMethodPositions, generateSetters);

		// Persist class
		if ( StringUtils.isBlank(outputDirectory) ){
			cc.writeFile();
		}else{
			cc.writeFile(outputDirectory);
		}
		return cc.toClass();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private static void addMethodsAndFields(CtClass cc, CtClass interfaceCtClass, boolean generateMethodPositions, boolean generateSetters) throws CannotCompileException, NotFoundException {
		ISetterProcessor setterProcessor = generateSetters ? ExportProcessor.getInstance() : NullProcessor.getInstance();
		IAnnotationProcessor annotationProcessor = generateMethodPositions ? ExportProcessor.getInstance() : NullProcessor.getInstance();

		MethodInfo<CtMethod> methods = InterfaceJavassistMethodsUtil.instance().getMethods(interfaceCtClass);
		Map<MethodInfoKey, CtMethod> gettersInfo = methods.getGettersInfo();
		Map<MethodInfoKey, CtMethod> settersInfo = methods.getSettersInfo();

		for(Entry<MethodInfoKey, CtMethod> methodEntry:gettersInfo.entrySet()){
			String methodName = methodEntry.getKey().getName();
			int methodIndex = methodEntry.getKey().getIndex();
			
			CtMethod method = methodEntry.getValue();
			CtClass returnType = method.getReturnType();
			
			// Get Field
			CtField field = getFieldFromMethod(cc, returnType, methodName);
			String fieldName = field.getName();
			field.setModifiers(field.getModifiers() | Modifier.PUBLIC);
			cc.addField(field);

			// Copy Interface Method
			CtMethod fieldMethod = new CtMethod(returnType, methodName, method.getParameterTypes(), cc);
			fieldMethod.setModifiers(fieldMethod.getModifiers() - Modifier.ABSTRACT);
			fieldMethod.setBody("return "+fieldName+";");
			
			annotationProcessor.process(cc, fieldMethod, methodIndex);
			
			cc.addMethod(fieldMethod);
			
			setterProcessor.process(cc,fieldName, settersInfo);
		}
	}
	
	private static CtField getFieldFromMethod(CtClass cc, CtClass methodReturnType, String methodName) throws CannotCompileException, NotFoundException {
		return new CtField(methodReturnType, getFieldNameFromMethod(methodName), cc);
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
