package com.sleepcamel.ifdtoUtils.hibernate.transformers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListDTOResultTransformer<T> extends BaseDTOResultTransformer<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9097005255584947496L;

	public ListDTOResultTransformer(Class<T> interfaceClass) {
		super(interfaceClass);
	}

	private Comparator<T> resultComparator;

	public void setComparator(Comparator<T> resultComparator) {
		this.resultComparator = resultComparator;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List transformList(List list) {
		if ( resultComparator != null ){
			Collections.sort(list,resultComparator);
		}
		return list;
	}
	
}
