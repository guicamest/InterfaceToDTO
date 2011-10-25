package com.sleepcamel.ifdtoUtils.json;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.type.TypeReference;

import com.sleepcamel.ifdtoUtils.exception.DTOUtilsException;

public class InterfaceDTOJsonBuilder<T> {

	private ObjectReader reader;
	private ObjectWriter writer;
	private Class<T> interfaceClass;
	private ObjectMapper mapper;

	public static <T> InterfaceDTOJsonBuilder<T> builder(Class<T> interfaceClass){
		return new InterfaceDTOJsonBuilder<T>(InterfaceDTOJsonUtils.getMapper(interfaceClass), interfaceClass);
	}
	
	private InterfaceDTOJsonBuilder(ObjectMapper mapper, Class<T> interfaceClass) {
		this.mapper = mapper;
		this.reader = mapper.reader(interfaceClass);
		this.writer = mapper.writerWithType(interfaceClass);
		this.interfaceClass = interfaceClass;
	}

	private InterfaceDTOJsonBuilder(ObjectMapper mapper, TypeReference<T> tt) {
		this.mapper = mapper;
		this.reader = mapper.reader(tt);
		this.writer = mapper.writerWithType(tt);
	}
	
	public <O> InterfaceDTOJsonBuilder<O> withToken(TypeReference<O> tt) {
		return new InterfaceDTOJsonBuilder<O>(InterfaceDTOJsonUtils.getMapper(interfaceClass), tt);
	}
	
	public ObjectMapper mapper(){
		return mapper;
	}

	public String toJson(T object) {
		try{return writer.writeValueAsString(object);}
		catch(Exception e){throw new DTOUtilsException(e);}
	}
	
	@SuppressWarnings("unchecked")
	public T fromJson(String json) {
		// Cast here is necessary to avoid bug in sun compiler... http://stackoverflow.com/questions/314572/bug-in-eclipse-compiler-or-in-javac
		try{return (T)reader.readValue(json);}
		catch(Exception e){throw new DTOUtilsException(e);}
	}

}
