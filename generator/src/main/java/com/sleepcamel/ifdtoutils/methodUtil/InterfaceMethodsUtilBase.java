package com.sleepcamel.ifdtoutils.methodUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.sleepcamel.ifdtoutils.methodInfo.MethodInfo;

import javassist.NotFoundException;

public abstract class InterfaceMethodsUtilBase<MT, CT> {

	public List<MT> getExportableMethods(CT ctClass, Comparator<MT> comparator){
		List<MT> exportableMethods = getExportableMethods(ctClass);
		Collections.sort(exportableMethods, comparator);
		return exportableMethods;
	}
	
	public List<MT> getExportableMethods(CT ctClass){
		return getMethods(ctClass).getGetters();
	}
	
	public MethodInfo<MT> getMethods(CT ctClass){
		List<MT> classMethods = getClassMethods(ctClass);
		MethodInfo<MT> methodInfo = new MethodInfo<MT>();
		int i = 0;

		try{
		Iterator<MT> iterator = classMethods.iterator();
		while(iterator.hasNext()){
			MT method = iterator.next();
			if ( isExportable(method) ){
				methodInfo.addToGetters(getName(method), i, method);
			}else{
				if ( isSetter(method) ){
					methodInfo.addToSetters(getName(method), i, method);
				}
			}
			i++;
		}
		}catch(Exception e){
			throw new RuntimeException(e); 
		}
		return methodInfo;
	}
	
	boolean isExportable(MT method) throws NotFoundException {
		// Skip methods with parameters
		if ( getParameterTypes(method).length != 0 ){
			return false;
		}
		
		// Skip methods with void return type
		if ( getReturnType(method).equals(getVoidClass()) ){
			return false;
		}
		return true;
	}

	boolean isSetter(MT method) throws NotFoundException {
		return getParameterTypes(method).length == 1 && getReturnType(method).equals(getVoidClass());
	}
	
	public boolean isSetter(MT method, CT paramType) throws NotFoundException {
		return isSetter(method) && getParameterTypes(method)[0].equals(paramType);
	}
	
	protected List<MT> getClassMethods(CT ctClass) {
		List<MT> methodsList = new ArrayList<MT>();
		methodsList.addAll(Arrays.asList(getInterfaceMethods(ctClass)));
		try {
			CT[] superclasses = getSuperInterfaces(ctClass);
			for(CT interfaceClass:superclasses){
				List<MT> interfaceClassMethods = getClassMethods(interfaceClass);
				for(MT method:interfaceClassMethods){
					if ( !methodsList.contains(method) ){
						methodsList.add(method);
					}
				}
			}
		} catch (Exception e) {
		}
		return methodsList;
	}
	
	abstract CT getReturnType(MT method) throws NotFoundException;
	
	abstract String getName(MT method);
	
	abstract CT getVoidClass();
	
	abstract CT[] getParameterTypes(MT method) throws NotFoundException;
	
	abstract MT[] getInterfaceMethods(CT clazz);
	
	abstract CT[] getSuperInterfaces(CT clazz) throws Exception;
}
