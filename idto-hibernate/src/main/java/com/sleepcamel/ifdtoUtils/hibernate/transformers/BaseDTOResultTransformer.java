package com.sleepcamel.ifdtoUtils.hibernate.transformers;

import org.hibernate.transform.ResultTransformer;

import com.sleepcamel.ifdtoUtils.ArrayInterfaceDTOTransformer;

abstract public class BaseDTOResultTransformer<T> implements ResultTransformer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ArrayInterfaceDTOTransformer<T> transformer;

	public BaseDTOResultTransformer(Class<T> interfaceClass) {
		transformer = new ArrayInterfaceDTOTransformer<T>(interfaceClass);
	}
	
	@Override
	public Object transformTuple(Object[] selectObjects, String[] selectAliases) {
		return transformer.transformArrayToDTO(selectObjects);
	}

}
