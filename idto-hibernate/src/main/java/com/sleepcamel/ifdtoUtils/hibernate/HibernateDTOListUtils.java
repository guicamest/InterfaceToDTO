package com.sleepcamel.ifdtoUtils.hibernate;

import java.util.Comparator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.ResultTransformer;

import com.sleepcamel.ifdtoUtils.hibernate.transformers.ListDTOResultTransformer;

public class HibernateDTOListUtils<T> extends HibernateDTOUtils<T,List<T>>{

	private Comparator<T> resultComparator;

	protected HibernateDTOListUtils(Class<T> interfaceClass) {
		super(interfaceClass);
	}
	
	@Override
	protected ResultTransformer getDTOResultTransformer() {
		ListDTOResultTransformer<T> dtoResultTransformer = new ListDTOResultTransformer<T>(interfaceClass);
		dtoResultTransformer.setComparator(resultComparator);
		return dtoResultTransformer;
	}
	
	@SuppressWarnings("unchecked")
	protected List<T> getResult(Query query) {
		return (List<T>) query.list();
	}

	public HibernateDTOListUtils<T> sort(Comparator<T> resultComparator) {
		this.resultComparator = resultComparator;
		return this;
	}

	public HibernateDTOListUtils<T> list() {
		return this;
	}
}
