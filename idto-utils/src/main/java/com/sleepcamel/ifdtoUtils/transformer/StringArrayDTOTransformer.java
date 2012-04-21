package com.sleepcamel.ifdtoUtils.transformer;

public class StringArrayDTOTransformer<T> extends IterableInterfaceDTOTransformer<T, String[]>{

	public StringArrayDTOTransformer(Class<T> interfaceClass, boolean useConverters) {
		super(interfaceClass, true, useConverters);
	}

	@Override
	protected Object getValue(int i, String[] iterable) {
		String value = iterable[i];
		if ( value != null ){
			value = value.trim();
		}
		return value;
	}

	@Override
	protected boolean hasMoreValues(int i, String[] iterable) {
		return i < iterable.length;
	}

}
