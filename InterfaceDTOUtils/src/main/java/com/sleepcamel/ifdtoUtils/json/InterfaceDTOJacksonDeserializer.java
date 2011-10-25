package com.sleepcamel.ifdtoUtils.json;

import java.io.IOException;
import java.lang.reflect.Field;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.std.StdDeserializer;

import com.sleepcamel.ifdtoUtils.InterfaceDTOUtils;

public class InterfaceDTOJacksonDeserializer<T> extends StdDeserializer<T> {

	private Field[] declaredFields;

	protected InterfaceDTOJacksonDeserializer(Class<T> vc) {
		super(vc);
		declaredFields = createInstance().getClass().getDeclaredFields();
	}
	
	@SuppressWarnings("unchecked")
	public T createInstance() {
		return InterfaceDTOUtils.getDto((Class<T>) getValueClass());
	}

	@Override
	public T deserialize(JsonParser jParser, DeserializationContext deserializationCtx) throws IOException, JsonProcessingException {
		T instance = createInstance();
		JsonNode jsonNode = jParser.readValueAsTree();
		for(Field field:declaredFields ){
			String fieldName = field.getName();
			if ( jsonNode.has(fieldName) ){
				JsonNode fieldNode = jsonNode.get(fieldName);
				Object fieldValue = jParser.getCodec().treeToValue(fieldNode, field.getType());
				try { field.set(instance,fieldValue); } catch (Exception e) {}
			}
		}
		return instance;
	}

}
