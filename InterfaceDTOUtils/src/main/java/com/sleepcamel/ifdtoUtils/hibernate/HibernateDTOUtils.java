package com.sleepcamel.ifdtoUtils.hibernate;

import java.util.Comparator;
import java.util.List;

import org.hibernate.Query;

import com.sleepcamel.ifdtoUtils.hibernate.transformers.BaseDTOResultTransformer;

public class HibernateDTOUtils<T> {

	public static <T> HibernateDTOUtils<T> getFor(Class<T> interfaceClass) {
		return new HibernateDTOUtils<T>(interfaceClass);
	}

	private Class<T> interfaceClass;
	private Comparator<T> resultComparator;

	public HibernateDTOUtils(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	@SuppressWarnings("unchecked")
	public List<T> fromQuery(Query query) {
		BaseDTOResultTransformer<T> baseDTOResultTransformer = new BaseDTOResultTransformer<T>(interfaceClass);
		baseDTOResultTransformer.setComparator(resultComparator);
			
		query.setResultTransformer(baseDTOResultTransformer);
		return query.list();
	}

	public HibernateDTOUtils<T> sort(Comparator<T> resultComparator) {
		this.resultComparator = resultComparator;
		return this;
	}

}
