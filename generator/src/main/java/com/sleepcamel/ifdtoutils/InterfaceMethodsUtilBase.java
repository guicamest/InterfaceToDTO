package com.sleepcamel.ifdtoutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javassist.NotFoundException;

public abstract class InterfaceMethodsUtilBase<MT, CT> {

	public List<MT> getExportableMethods(CT interfaceCtClass){
		List<MT> interfaceClassMethods = getInterfaceClassMethods(interfaceCtClass);
		try{
		Iterator<MT> iterator = interfaceClassMethods.iterator();
		while(iterator.hasNext()){
			MT ctMethod = iterator.next();
			if ( !isExportableMethod(ctMethod) ){
				iterator.remove();
			}
		}
		}catch(Exception e){
			throw new RuntimeException(e); 
		}
		return interfaceClassMethods;
	}
	
	abstract boolean isExportableMethod(MT method) throws NotFoundException;
	
	abstract MT[] getInterfaceMethods(CT clazz);
	
	abstract CT[] getInterfaceSuperInterfaces(CT clazz) throws Exception;

	protected List<MT> getInterfaceClassMethods(CT interfaceCtClass) {
		List<MT> methodsList = new ArrayList<MT>();
		methodsList.addAll(Arrays.asList(getInterfaceMethods(interfaceCtClass)));
		try {
			CT[] superclasses = getInterfaceSuperInterfaces(interfaceCtClass);
			for(CT superInterfaceClass:superclasses){
				List<MT> interfaceClassMethods = getInterfaceClassMethods(superInterfaceClass);
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
}
