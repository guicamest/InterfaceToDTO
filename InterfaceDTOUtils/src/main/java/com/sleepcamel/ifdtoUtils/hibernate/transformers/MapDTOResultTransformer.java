package com.sleepcamel.ifdtoUtils.hibernate.transformers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sleepcamel.ifdtoUtils.exception.DTOUtilsException;
import com.sleepcamel.ifdtoutils.InterfaceJavaMethodsUtil;

public class MapDTOResultTransformer<T> extends BaseDTOResultTransformer<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -256660858458018183L;
	
	private List<Entry<Integer, Class<?>>> keys;

	public MapDTOResultTransformer(Class<T> interfaceClass, List<Entry<Integer, Class<?>>> keys) {
		super(interfaceClass);
		this.keys = keys;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List transformList(List paramList) {
		Map<Object,Object> map = new HashMap<Object, Object>();
		Entry<Integer, Class<?>> pair = keys.get(0);
		for(Object object:paramList){
			T result = (T) object;
			map.put(getObjectValue(pair.getKey(),result), object);
		}

		List returnList = new ArrayList();
		returnList.add(map);
		return returnList;
	}

	private Object getObjectValue(Integer methodIndex, T result) {
		Class<? extends Object> dtoClass = result.getClass();
		Method methodWithPosition = InterfaceJavaMethodsUtil.instance().getMethodWithPosition(dtoClass, methodIndex);
		if ( methodWithPosition == null )
			throw new DTOUtilsException("Invalid index "+methodIndex);
			
		try{
			return methodWithPosition.invoke(result);
		}catch(Exception e){
			throw new DTOUtilsException(e);
		}
	}

}
