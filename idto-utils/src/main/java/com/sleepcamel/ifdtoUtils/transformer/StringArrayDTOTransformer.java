package com.sleepcamel.ifdtoUtils.transformer;

import com.sleepcamel.ifdtoUtils.valueConverters.DoubleValueConverter;
import com.sleepcamel.ifdtoUtils.valueConverters.EnumValueConverter;
import com.sleepcamel.ifdtoUtils.valueConverters.IntValueConverter;
import com.sleepcamel.ifdtoUtils.valueConverters.LongValueConverter;

public class StringArrayDTOTransformer<T> extends IterableInterfaceDTOTransformer<T, String[]>{

	static{
		valueConverters.put(int.class, IntValueConverter.INSTANCE);
		valueConverters.put(Integer.class, IntValueConverter.INSTANCE);
		valueConverters.put(long.class, LongValueConverter.INSTANCE);
		valueConverters.put(Long.class, LongValueConverter.INSTANCE);
		valueConverters.put(double.class, DoubleValueConverter.INSTANCE);
		valueConverters.put(Double.class, DoubleValueConverter.INSTANCE);
		superClassValueConverters.put(Enum.class, EnumValueConverter.INSTANCE);
	}
	
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
