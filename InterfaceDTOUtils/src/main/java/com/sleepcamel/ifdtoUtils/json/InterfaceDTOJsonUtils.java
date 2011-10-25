package com.sleepcamel.ifdtoUtils.json;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

import com.sleepcamel.ifdtoUtils.exception.DTOUtilsException;

public class InterfaceDTOJsonUtils {

	private static final String INTERFACE_DTO_JACKSON_MODULE = "InterfaceDTOJacksonModule";

	private InterfaceDTOJsonUtils(){}
	
	protected static <T> ObjectMapper getMapper(Class<T> interfaceClass) {
		if ( !interfaceClass.isInterface() ){
			throw new DTOUtilsException(interfaceClass+" is not an interface");
		}
			
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleModule simpleModule = new SimpleModule(INTERFACE_DTO_JACKSON_MODULE, new Version(1, 0, 0, null));
		
		simpleModule.addDeserializer(interfaceClass, new InterfaceDTOJacksonDeserializer<T>(interfaceClass));
		simpleModule.addSerializer(interfaceClass, new InterfaceDTOJacksonSerializer<T>(interfaceClass));
		
		objectMapper.registerModule(simpleModule);
		return objectMapper;
	}
	
	public static <T> String toJson(Class<T> interfaceClass, T tInstance) {
		try{return getMapper(interfaceClass).writerWithType(interfaceClass).writeValueAsString(tInstance);}
		catch(Exception e){throw new DTOUtilsException(e);}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T fromJson(Class<T> interfaceClass, String json) {
		// Cast here is necessary to avoid bug in sun compiler... http://stackoverflow.com/questions/314572/bug-in-eclipse-compiler-or-in-javac
		try{return (T)getMapper(interfaceClass).reader(interfaceClass).readValue(json);}
		catch(Exception e){throw new DTOUtilsException(e);}
	}

}
