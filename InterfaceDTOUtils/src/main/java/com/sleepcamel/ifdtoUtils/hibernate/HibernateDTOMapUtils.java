package com.sleepcamel.ifdtoUtils.hibernate;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.transform.ResultTransformer;

import com.sleepcamel.ifdtoUtils.exception.DTOUtilsException;
import com.sleepcamel.ifdtoUtils.hibernate.transformers.MapDTOResultTransformer;

public class HibernateDTOMapUtils<T,K,V> extends HibernateDTOUtils<T, Map<K,V>> {

	private List<Entry<Integer, Class<?>>> keys;
	private boolean dtoIsList;

	protected HibernateDTOMapUtils(Class<T> interfaceClass, List<Entry<Integer, Class<?>>> list) {
		this(interfaceClass, list, false);
	}
	
	protected HibernateDTOMapUtils(Class<T> interfaceClass, List<Entry<Integer, Class<?>>> list, boolean dtoIsList) {
		super(interfaceClass);
		this.keys = list;
		this.dtoIsList = dtoIsList;
	}

	@Override
	protected ResultTransformer getDTOResultTransformer() {
		return new MapDTOResultTransformer<T>(interfaceClass, keys, dtoIsList);
	}
	
	@SuppressWarnings("unchecked")
	protected Map<K,V> getResult(Query query) {
		return (Map<K,V>) query.list().get(0);
	}
	
	/** 
	 * Don't USE!
	 */
	@Override
	@Deprecated
	public <E> HibernateDTOMapUtils<T, E, T> map(Class<E> keyClass, int position) {
		throw new DTOUtilsException("Method must not be called");
	}
	
	public <E> HibernateDTOMapUtils<T,E,Map<K,V>> key(Class<E> keyClass, int position) {
		addKeyToList(keys, keyClass, position);
		return new HibernateDTOMapUtils<T,E,Map<K,V>>(interfaceClass, keys);
	}
}
