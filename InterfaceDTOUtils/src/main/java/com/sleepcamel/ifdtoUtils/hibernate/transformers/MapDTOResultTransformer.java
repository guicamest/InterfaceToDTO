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

	private boolean dtoIsList;

	public MapDTOResultTransformer(Class<T> interfaceClass, List<Entry<Integer, Class<?>>> keys, boolean dtoIsList) {
		super(interfaceClass);
		this.keys = keys;
		this.dtoIsList = dtoIsList;
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
			Object keyToUse = getObjectValue(pair.getKey(),result);
			
			Object object = lastMap.get(keyToUse);
			if ( i < keys.size() - 1 ){
				if ( object == null ){
					object = new HashMap<Object, Object>();
					lastMap.put(keyToUse, object);
				}
				lastMap = (Map<Object, Object>) object;
			}else{
				if ( dtoIsList ){
					if ( object == null ){
						object = new ArrayList<Object>();
					}
					((List<Object>)object).add(result);
				}else{
					object = result;
				}
				lastMap.put(keyToUse, object);
			}
			
		}
	}

	private Object getObjectValue(Integer methodIndex, T result) {
		Class<? extends Object> dtoClass = result.getClass();
		Method methodWithPosition = InterfaceJavaMethodsUtil.instance().getMethodWithPosition(dtoClass, methodIndex);
		if ( methodWithPosition == null ){
			throw new DTOUtilsException("Invalid index "+methodIndex);
		}
			
		try{
			return methodWithPosition.invoke(result);
		}catch(Exception e){
			throw new DTOUtilsException(e);
		}
	}

}
