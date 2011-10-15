package com.sleepcamel.ifdtoUtils.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class InterfaceDTOJsonBuilder<T> {

	private TypeToken<T> typeToken;
	private Gson gson;

	private InterfaceDTOJsonBuilder(Gson gson, TypeToken<T> tt) {
		this.gson = gson;
		this.typeToken = tt;
	}

	public static <T> InterfaceDTOJsonBuilder<T> withToken(Gson gson, TypeToken<T> tt) {
		return new InterfaceDTOJsonBuilder<T>(gson, tt);
	}

	public String toJson(T object) {
		return gson.toJson(object, typeToken.getType());
	}
	
	@SuppressWarnings("unchecked")
	public T fromJson(String json) {
		// Cast here is necessary to avoid bug in sun compiler... http://stackoverflow.com/questions/314572/bug-in-eclipse-compiler-or-in-javac
		return (T)gson.fromJson(json, typeToken.getType());
	}
}
