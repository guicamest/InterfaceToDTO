package com.sleepcamel.ifdtoUtils;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sleepcamel.ifdtoUtils.json.InterfaceDTOJsonBuilder;
import com.sleepcamel.ifdtoUtils.json.InterfaceDTOJsonUtils;

public class InterfaceDTOBuilder<E> {

	private Class<E> interfaceClass;
	private boolean recursive = false;
	private List<Class<?>> otherInterfaces = new ArrayList<Class<?>>();

	private InterfaceDTOBuilder(Class<E> interfaceClass){
		this.interfaceClass = interfaceClass;
		validateInterfaceClass(interfaceClass);
	}
	
	private void validateInterfaceClass(Class<?> interfaceClass) {
		if ( interfaceClass == null ){
			throw new RuntimeException("Interface cannot be null");
		}
		if ( !interfaceClass.isInterface() ){
			throw new RuntimeException("Class must be an interface");
		}
	}
	
	public static <T> InterfaceDTOBuilder<T> builder(Class<T> interfaceClass){
		return new InterfaceDTOBuilder<T>(interfaceClass);
	}
	
	public InterfaceDTOBuilder<E> recursive(boolean recursive){
		this.recursive = recursive;
		return this;
	}
	
	public InterfaceDTOBuilder<E> add(Class<?> interfaceClass) {
		if ( interfaceClass.isInterface() && !otherInterfaces.contains(interfaceClass) ){
			otherInterfaces.add(interfaceClass);
		}
		return this;
	}
	
	public E dto(E object){
		if ( object == null ){
			return null;
		}
		return InterfaceDTOUtils.getFilledDto(interfaceClass, object, recursive, otherInterfaces);
	}
	
	public Iterable<E> dto(Iterable<E> objects) {
		if ( objects == null ){
			return null;
		}
		return InterfaceDTOUtils.getFilledDtos(interfaceClass, objects, recursive, otherInterfaces);
	}
	
	public GsonBuilder gsonBuilder() {
		return InterfaceDTOJsonUtils.getGsonBuilder(interfaceClass);
	}
	
	public Gson gson() {
		return gsonBuilder().create();
	}
	
	public String toJson(E object){
		return gson().toJson(object, interfaceClass);
	}
	
	public E fromJson(String json) {
		return gson().fromJson(json, interfaceClass);
	}

	public <T> InterfaceDTOJsonBuilder<T> withToken(TypeToken<T> tt){
		return InterfaceDTOJsonBuilder.withToken(gson(),tt);
	}

}
