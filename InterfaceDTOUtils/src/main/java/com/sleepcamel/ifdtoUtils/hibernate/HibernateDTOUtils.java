package com.sleepcamel.ifdtoUtils.hibernate;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.ResultTransformer;

import com.sleepcamel.ifdtoUtils.hibernate.transformers.ListDTOResultTransformer;

public class HibernateDTOUtils<T,RT> {

	public static <T> HibernateDTOUtils<T,T> getFor(Class<T> interfaceClass) {
		return new HibernateDTOUtils<T,T>(interfaceClass);
	}

	protected Class<T> interfaceClass;

	protected HibernateDTOUtils(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public RT fromQuery(Query query) {
		query.setResultTransformer(getDTOResultTransformer());
		return getResult(query);
	}

	protected ResultTransformer getDTOResultTransformer() {
		return new ListDTOResultTransformer<T>(interfaceClass);
	}

	@SuppressWarnings("unchecked")
	protected RT getResult(Query query) {
		return (RT) query.uniqueResult();
	}

	public HibernateDTOListUtils<T> list() {
		return new HibernateDTOListUtils<T>(interfaceClass);
	}
	
	public <E> HibernateDTOMapUtils<T,E,T> map(Class<E> keyClass, int position) {
		return new HibernateDTOMapUtils<T,E,T>(interfaceClass, getListFor(keyClass, position));
	}

	private List<Map.Entry<Integer,Class<?>>> getListFor(Class<?> keyClass, int position) {
		List<Map.Entry<Integer,Class<?>>> list = new ArrayList<Map.Entry<Integer,Class<?>>>();
		addKeyToList(list, keyClass, position);
		return list;
	}
	
	protected void addKeyToList(List<Map.Entry<Integer,Class<?>>> list, Class<?> keyClass, int position) {
		list.add(0, new AbstractMap.SimpleEntry<Integer,Class<?>>(position, keyClass));
	}
}
