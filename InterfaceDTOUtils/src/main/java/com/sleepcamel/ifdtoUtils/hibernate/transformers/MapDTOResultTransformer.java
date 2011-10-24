package com.sleepcamel.ifdtoUtils.hibernate.transformers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sleepcamel.ifdtoUtils.exception.DTOUtilsException;

public class MapDTOResultTransformer<T> extends BaseDTOResultTransformer<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -256660858458018183L;
	
	private List<Entry<Integer, Class<?>>> keys;
	private ILastValueStrategy lastValueStrategy;

	public MapDTOResultTransformer(Class<T> interfaceClass, List<Entry<Integer, Class<?>>> keys, boolean dtoIsList) {
		super(interfaceClass);
		this.keys = keys;

		if ( dtoIsList ){
			lastValueStrategy = ResultListValueStrategy.instance();
		}else{
			lastValueStrategy = ResultValueStrategy.instance();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List transformList(List paramList) {
		Map<Object,Object> map = new HashMap<Object, Object>();
		
		for(Object object:paramList){
			fillMapWithResult(map,(T) object);
		}

		List returnList = new ArrayList();
		returnList.add(map);
		return returnList;
	}

	@SuppressWarnings("unchecked")
	private void fillMapWithResult(Map<Object, Object> map, T result) {
		int size = keys.size();
		Map<Object, Object> lastMap = map;

		for(int i=0; i < size; i++){
			Entry<Integer, Class<?>> pair = keys.get(i);
			Object keyToUse = getObjectValue(result, pair.getKey());
			
			Object lastMapValue = lastMap.get(keyToUse);
			if ( i < keys.size() - 1 ){
				if ( lastMapValue == null ){
					lastMapValue = new HashMap<Object, Object>();
					lastMap.put(keyToUse, lastMapValue);
				}
				lastMap = (Map<Object, Object>) lastMapValue;
			}else{
				lastMap.put(keyToUse, lastValueStrategy.get(lastMapValue, result));
			}
			
		}
	}

	private Object getObjectValue(T result, Integer methodIndex) {
		try{
			Method method = dtoMethods.get(methodIndex);
			return method.invoke(result);
		}catch(Exception e){
			throw new DTOUtilsException(e);
		}
	}

}
