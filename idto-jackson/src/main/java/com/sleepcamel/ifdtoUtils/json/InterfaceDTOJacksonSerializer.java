package com.sleepcamel.ifdtoUtils.json;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;

import com.sleepcamel.ifdtoutils.DTOClassGenerator;
import com.sleepcamel.ifdtoutils.methodUtil.InterfaceJavaMethodsUtil;

public class InterfaceDTOJacksonSerializer<T> extends SerializerBase<T> {

	private List<Method> exportableMethods;

	protected InterfaceDTOJacksonSerializer(Class<T> t) {
		super(t);
		exportableMethods = InterfaceJavaMethodsUtil.instance().getExportableMethods(handledType());
	}

	@Override
	public void serialize(T tInstance, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
		jgen.writeStartObject();
		for(Method method: exportableMethods){
			Object fieldValue = null;
			method.setAccessible(true);
			try {fieldValue = method.invoke(tInstance);} catch (Exception e) {e.printStackTrace();}
			jgen.writeObjectField(DTOClassGenerator.getFieldNameFromMethod(method.getName()), fieldValue);
		}
		jgen.writeEndObject();
	}

}
