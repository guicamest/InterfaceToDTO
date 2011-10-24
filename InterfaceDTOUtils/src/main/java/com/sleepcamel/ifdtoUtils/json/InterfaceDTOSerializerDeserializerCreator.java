package com.sleepcamel.ifdtoUtils.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sleepcamel.ifdtoUtils.InterfaceDTOUtils;
import com.sleepcamel.ifdtoutils.DTOClassGenerator;
import com.sleepcamel.ifdtoutils.methodUtil.InterfaceJavaMethodsUtil;

public class InterfaceDTOSerializerDeserializerCreator<T> implements JsonSerializer<T>, JsonDeserializer<T>, InstanceCreator<T>{

	@Override
	public JsonElement serialize(T object, Type typeOfArg, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		for(Method method:InterfaceJavaMethodsUtil.instance().getExportableMethods((Class<?>) typeOfArg) ){
			Object fieldValue = null;
			method.setAccessible(true);
			try {fieldValue = method.invoke(object);} catch (Exception e) {e.printStackTrace();}
			jsonObject.add(DTOClassGenerator.getFieldNameFromMethod(method.getName()), context.serialize(fieldValue));
		}
		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	public T createInstance(Type paramType) {
		Class<T> classType = (Class<T>) paramType;
		return InterfaceDTOUtils.getDto(classType);
	}

	@Override
	public T deserialize(JsonElement paramJsonElement, Type paramType, JsonDeserializationContext paramJsonDeserializationContext) throws JsonParseException {
		T instance = createInstance(paramType);
		JsonObject jsonObject = paramJsonElement.getAsJsonObject();
		for(Field field:instance.getClass().getDeclaredFields() ){
			if ( jsonObject.has(field.getName()) ){
				JsonElement jsonElement = jsonObject.get(field.getName());
				Object deserialize = paramJsonDeserializationContext.deserialize(jsonElement, field.getType());
				try { field.set(instance,deserialize); } catch (Exception e) {}
			}
		}
		return instance;
	}

}
