package com.sleepcamel.ifdtoUtils.transformer;

public class ObjectArrayDTOTransformer<T> extends IterableInterfaceDTOTransformer<T, Object[]>{

	public ObjectArrayDTOTransformer(Class<T> interfaceClass, boolean useConverters) {
		super(interfaceClass, true, useConverters);
	}

	@Override
	protected Object getValue(int i, Object[] iterable) {
		return iterable[i];
	}

	@Override
	protected boolean hasMoreValues(int i, Object[] iterable) {
		return i < iterable.length;
	}

}
