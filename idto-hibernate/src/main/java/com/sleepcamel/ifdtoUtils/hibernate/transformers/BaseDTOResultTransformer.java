package com.sleepcamel.ifdtoUtils.hibernate.transformers;

import org.hibernate.transform.ResultTransformer;

import com.sleepcamel.ifdtoUtils.transformer.ObjectArrayDTOTransformer;

abstract public class BaseDTOResultTransformer<T> implements ResultTransformer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ObjectArrayDTOTransformer<T> transformer;

	public BaseDTOResultTransformer(Class<T> interfaceClass) {
		transformer = new ObjectArrayDTOTransformer<T>(interfaceClass, false);
	}
	
	@Override
	public Object transformTuple(Object[] selectObjects, String[] selectAliases) {
		return transformer.transformToDTO(selectObjects);
	}

}
