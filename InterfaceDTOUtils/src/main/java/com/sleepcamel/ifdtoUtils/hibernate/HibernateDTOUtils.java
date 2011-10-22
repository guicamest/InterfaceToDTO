package com.sleepcamel.ifdtoUtils.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.sleepcamel.ifdtoUtils.hibernate.transformers.BaseDTOResultTransformer;

public class HibernateDTOUtils<T> {

	public static <T> HibernateDTOUtils<T> getFor(Class<T> interfaceClass) {
		return new HibernateDTOUtils<T>(interfaceClass);
	}

	private Class<T> interfaceClass;

	public HibernateDTOUtils(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	@SuppressWarnings("unchecked")
	public List<T> fromQuery(Query query) {
		query.setResultTransformer(new BaseDTOResultTransformer<T>(interfaceClass));
		return query.list();
	}

}
