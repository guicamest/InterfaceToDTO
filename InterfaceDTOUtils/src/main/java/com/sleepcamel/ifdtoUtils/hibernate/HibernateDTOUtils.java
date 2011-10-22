package com.sleepcamel.ifdtoUtils.hibernate;

import org.hibernate.Query;

import com.sleepcamel.ifdtoUtils.hibernate.transformers.BaseDTOResultTransformer;

public class HibernateDTOUtils<T,RT> {

	public static <T> HibernateDTOUtils<T,T> getFor(Class<T> interfaceClass) {
		return new HibernateDTOUtils<T,T>(interfaceClass);
	}

	protected Class<T> interfaceClass;

	protected HibernateDTOUtils(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	@SuppressWarnings("unchecked")
	public RT fromQuery(Query query) {
		BaseDTOResultTransformer<T> baseDTOResultTransformer = new BaseDTOResultTransformer<T>(interfaceClass);
		query.setResultTransformer(baseDTOResultTransformer);
		return (RT) query.uniqueResult();
	}

	public HibernateDTOListUtils<T> list() {
		return new HibernateDTOListUtils<T>(interfaceClass);
	}

}
