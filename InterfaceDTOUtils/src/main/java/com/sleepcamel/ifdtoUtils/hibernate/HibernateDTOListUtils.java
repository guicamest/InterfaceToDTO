package com.sleepcamel.ifdtoUtils.hibernate;

import java.util.Comparator;
import java.util.List;

import org.hibernate.Query;

import com.sleepcamel.ifdtoUtils.hibernate.transformers.BaseDTOResultTransformer;

public class HibernateDTOListUtils<T> extends HibernateDTOUtils<T,List<T>>{

	private Comparator<T> resultComparator;

	protected HibernateDTOListUtils(Class<T> interfaceClass) {
		super(interfaceClass);
	}

	@SuppressWarnings("unchecked")
	public List<T> fromQuery(Query query) {
		BaseDTOResultTransformer<T> baseDTOResultTransformer = new BaseDTOResultTransformer<T>(interfaceClass);
		baseDTOResultTransformer.setComparator(resultComparator);
			
		query.setResultTransformer(baseDTOResultTransformer);
		return query.list();
	}

	public HibernateDTOListUtils<T> sort(Comparator<T> resultComparator) {
		this.resultComparator = resultComparator;
		return this;
	}

	public HibernateDTOListUtils<T> list() {
		return this;
	}
}
