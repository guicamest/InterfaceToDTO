package com.sleepcamel.ifdtoUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javassist.NotFoundException;

import com.sleepcamel.ifdtoutils.DTOClassGenerator;


public class InterfaceDTOUtils {

	private InterfaceDTOUtils(){}
	
	@SuppressWarnings("unchecked")
	public static <T> T getDto(Class<T> interfaceClass) {
		Class<T> dtoClass = null;
		try{
			try{
				dtoClass = (Class<T>) Thread.currentThread().getContextClassLoader().loadClass(DTOClassGenerator.getDTOName(interfaceClass,""));
			}catch(ClassNotFoundException e){
				dtoClass = (Class<T>) DTOClassGenerator.generateDTOForInterface(interfaceClass);
			}
			return dtoClass.newInstance();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> T getFilledDto(Class<T> interfaceClass, T object) {
		return getFilledDto(interfaceClass, object, false);
	}
	
	public static <T> Iterable<T> getFilledDtos(Class<T> interfaceClass, Iterable<T> object) {
		return getFilledDtos(interfaceClass, object, false);
	}
	
	public static <T> Iterable<T> getFilledDtos(Class<T> interfaceClass, Iterable<T> object, boolean generateForSameInterfaces) {
		List<T> dtos = new ArrayList<T>();
		Iterator<T> iterator = object.iterator();
		while(iterator.hasNext()){
			dtos.add(getFilledDto(interfaceClass, iterator.next(), generateForSameInterfaces));
		}
		return dtos;
	}
	
	public static <T> T getFilledDto(Class<T> interfaceClass, T object, boolean generateForSameInterfaces) {
		try{
			return fillInstance(interfaceClass, object, getDto(interfaceClass), generateForSameInterfaces);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T fillInstance(Class<T> interfaceClass, T source, T destiny, boolean recursively) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NotFoundException {
		Class<? extends Object> destinyClass = destiny.getClass();
		for(Method method:DTOClassGenerator.getExportableMethods(interfaceClass) ){
			Field field = destinyClass.getDeclaredField(DTOClassGenerator.getFieldNameFromMethod(method.getName()));
			Object fieldValue = method.invoke(source);
			if ( recursively && field.getType().equals(interfaceClass) && fieldValue != null ){
				fieldValue = getFilledDto(interfaceClass, (T)fieldValue, recursively);
			}
			field.set(destiny, fieldValue);
		}
		return destiny;
	}

}
