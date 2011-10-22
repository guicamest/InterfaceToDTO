package com.sleepcamel.ifdtoUtils.hibernate;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.transform.ResultTransformer;

import com.sleepcamel.ifdtoUtils.hibernate.transformers.MapDTOResultTransformer;

public class HibernateDTOMapUtils<T,K,V> extends HibernateDTOUtils<T, Map<K,V>> {

	private List<Entry<Integer, Class<?>>> keys;

	protected HibernateDTOMapUtils(Class<T> interfaceClass, List<Entry<Integer, Class<?>>> list) {
		super(interfaceClass);
		this.keys = list;
	}

	@Override
	protected ResultTransformer getDTOResultTransformer() {
		return new MapDTOResultTransformer<T>(interfaceClass, keys);
	}
	
	@SuppressWarnings("unchecked")
	protected Map<K,V> getResult(Query query) {
		return (Map<K,V>) query.list().get(0);
	}
	
//	public <E> HibernateDTOMapUtils<T,E,Map<K,V>> key(Class<E> keyClass, int position) {
//		return new HibernateDTOMapUtils<T,E,Map<K,V>>(interfaceClass);
//	}
}