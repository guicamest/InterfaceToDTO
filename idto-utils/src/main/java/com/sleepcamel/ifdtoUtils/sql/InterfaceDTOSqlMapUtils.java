package com.sleepcamel.ifdtoUtils.sql;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sleepcamel.ifdtoUtils.exception.DTOUtilsException;
import com.sleepcamel.ifdtoUtils.transformer.ILastValueStrategy;
import com.sleepcamel.ifdtoUtils.transformer.ResultListValueStrategy;
import com.sleepcamel.ifdtoUtils.transformer.ResultSetDTOTransformer;
import com.sleepcamel.ifdtoUtils.transformer.ResultValueStrategy;

public class InterfaceDTOSqlMapUtils<T,K,V> extends InterfaceDTOSqlUtils<T, Map<K,V>> {

	private List<Entry<Integer, Class<?>>> keys;
	private boolean dtoIsList;
	private ILastValueStrategy lastValueStrategy;

	protected InterfaceDTOSqlMapUtils(Class<T> interfaceClass, ResultSetDTOTransformer<T> transformer, Statement statement, String queryString, List<Entry<Integer, Class<?>>> list) {
		this(interfaceClass, transformer, statement, queryString, list, false);
	}
	
	protected InterfaceDTOSqlMapUtils(Class<T> interfaceClass, ResultSetDTOTransformer<T> transformer, Statement statement, String queryString, List<Entry<Integer, Class<?>>> list, boolean dtoIsList) {
		super(interfaceClass, transformer, statement, queryString);
		this.keys = list;
		this.dtoIsList = dtoIsList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<K, V> result() throws SQLException {
		if ( dtoIsList ){
			lastValueStrategy = ResultListValueStrategy.instance();
		}else{
			lastValueStrategy = ResultValueStrategy.instance();
		}

		ResultSet resultSet = statement.executeQuery(queryString);
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		while ( resultSet.next() ){
			fillMapWithResult(map, transformer.transformToDTO(resultSet));
		}
		return (Map<K, V>) map;
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
			Method method = transformer.getDtoMethods().get(methodIndex);
			return method.invoke(result);
		}catch(Exception e){
			throw new DTOUtilsException(e);
		}
	}

	/** 
	 * Don't USE!
	 */
	@Override
	@Deprecated
	public <E> InterfaceDTOSqlMapUtils<T, E, T> map(Class<E> keyClass, int position) {
		throw new DTOUtilsException("Method must not be called");
	}
	
	public <E> InterfaceDTOSqlMapUtils<T,E,Map<K,V>> key(Class<E> keyClass, int position) {
		addKeyToList(keys, keyClass, position);
		return new InterfaceDTOSqlMapUtils<T,E,Map<K,V>>(interfaceClass, transformer, statement, queryString, keys);
	}
}
