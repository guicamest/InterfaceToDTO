package com.sleepcamel.ifdtoUtils.json;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class InterfaceDTOJsonUtils {

	private InterfaceDTOJsonUtils(){}
	
	public static <T> GsonBuilder getGsonBuilder(Class<T> interfaceClass) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(interfaceClass, new InterfaceDTOSerializerDeserializerCreator<T>());
		return gsonBuilder;
	}
	
	public static <T> Gson getGson(Class<T> interfaceClass) {
		return getGsonBuilder(interfaceClass).create();
	}
	
	public static <T> String toJson(Class<T> interfaceClass, T drawable) {
		return getGson(interfaceClass).toJson(drawable, interfaceClass);
	}
	
	public static <T> T fromJson(Class<T> interfaceClass, String json) {
		return getGson(interfaceClass).fromJson(json, interfaceClass);
	}

	public static <T> String toJson(Class<T> interfaceClass, List<T> drawables) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(interfaceClass, new InterfaceDTOSerializerDeserializerCreator<T>());
		Gson create = gsonBuilder.create();
//		Type listType = new TypeToken<List<IDrawable>>() {}.getType();
//		return create.toJson(drawables,listType);
		return create.toJson(drawables);
	}
}
