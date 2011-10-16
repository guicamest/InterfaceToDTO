package com.sleepcamel.ifdtoUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javassist.NotFoundException;

import com.sleepcamel.ifdtoutils.DTOClassGenerator;
import com.sleepcamel.ifdtoutils.InterfaceDTOInfo;
import com.sleepcamel.ifdtoutils.InterfaceJavaMethodsUtil;


public class InterfaceDTOUtils {

	private InterfaceDTOUtils(){}
	
	@SuppressWarnings("unchecked")
	public static <T> T getDto(Class<T> interfaceClass) {
		Class<T> dtoClass = null;
		try{
			try{
				dtoClass = (Class<T>) Thread.currentThread().getContextClassLoader().loadClass(InterfaceDTOInfo.getInfo(interfaceClass).getDTOCanonicalName());
			}catch(ClassNotFoundException e){
				dtoClass = (Class<T>) DTOClassGenerator.generateDTOForInterface(interfaceClass);
			}
			return dtoClass.newInstance();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> Iterable<T> getFilledDtos(Class<T> interfaceClass, Iterable<T> object, boolean generateForSameInterfaces, List<Class<?>> interfaceList, boolean useFullPackage) {
		List<T> dtos = new ArrayList<T>();
		Iterator<T> iterator = object.iterator();
		while(iterator.hasNext()){
			dtos.add(getFilledDto(interfaceClass, iterator.next(), generateForSameInterfaces, interfaceList, useFullPackage));
		}
		return dtos;
	}
	
	public static <T> T getFilledDto(Class<T> interfaceClass, T object, boolean generateForSameInterfaces, List<Class<?>> interfaceList, boolean useFullPackage) {
		try{
			return fillInstance(interfaceClass, object, getDto(interfaceClass), generateForSameInterfaces, interfaceList, useFullPackage );
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T fillInstance(Class<T> interfaceClass, T source, T destiny, boolean recursively, List<Class<?>> interfaceList, boolean useFullPackage) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NotFoundException {
		Class<? extends Object> destinyClass = destiny.getClass();
		Class<? extends Object> sourceClass = null;
		
		if ( source != null ){
			sourceClass = source.getClass();
		}
		
		for(Method method:InterfaceJavaMethodsUtil.instance().getExportableMethods(interfaceClass) ){
			
			Field field = destinyClass.getDeclaredField(DTOClassGenerator.getFieldNameFromMethod(method.getName()));
			Object fieldValue = method.invoke(source);
			
			if ( EntityTypeProcessor.processes(method) ){
				fieldValue = EntityTypeProcessor.process(fieldValue, sourceClass.getName(), useFullPackage);
			}else{
				if (fieldValue != null){
					Class<T> interfaceClassToUse = null;
	
					if ( recursively && field.getType().equals(interfaceClass) ){
						interfaceClassToUse = interfaceClass;
					}else{
						interfaceClassToUse = findMatchingInterfaceForField(field.getType(), interfaceList);
					}
	
					if ( interfaceClassToUse != null ){
						fieldValue = getFilledDto(interfaceClassToUse, (T)fieldValue, recursively, interfaceList, useFullPackage);
					}
				}
			}
			
			field.set(destiny, fieldValue);

		}
		return destiny;
	}

	@SuppressWarnings("unchecked")
	private static <T> Class<T> findMatchingInterfaceForField(Class<?> type, List<Class<?>> interfaceList) {
		if ( !interfaceList.isEmpty() ){
			for(Class<?> interfaceClass:interfaceList){
				if ( interfaceClass.isAssignableFrom(type) )
					return (Class<T>) interfaceClass;
			}
		}
		return null;
	}

}
