package com.sleepcamel.ifdtoUtils.transformer;

import java.lang.reflect.Field;

import com.sleepcamel.ifdtoUtils.InterfaceDTOUtils;
import com.sleepcamel.ifdtoUtils.valueConverters.DoubleValueConverter;
import com.sleepcamel.ifdtoUtils.valueConverters.IValueConverter;
import com.sleepcamel.ifdtoUtils.valueConverters.IntValueConverter;
import com.sleepcamel.ifdtoUtils.valueConverters.LongValueConverter;
import com.sleepcamel.ifdtoutils.DTOClassGenerator;

abstract public class IterableInterfaceDTOTransformer<T, I> extends BaseInterfaceDTOTransformer<T, I>{

	static{
		valueConverters.put(int.class, IntValueConverter.INSTANCE);
		valueConverters.put(Integer.class, IntValueConverter.INSTANCE);
		valueConverters.put(long.class, LongValueConverter.INSTANCE);
		valueConverters.put(Long.class, LongValueConverter.INSTANCE);
		valueConverters.put(double.class, DoubleValueConverter.INSTANCE);
		valueConverters.put(Double.class, DoubleValueConverter.INSTANCE);
	}
	
	public IterableInterfaceDTOTransformer(Class<T> interfaceClass) {
		super(interfaceClass, false, false);
	}
	
	public IterableInterfaceDTOTransformer(Class<T> interfaceClass, boolean useDefaultConverters, boolean useCustomConverters) {
		super(interfaceClass, useDefaultConverters, useCustomConverters);
	}
	
 	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T transformToDTO(I iterable) {
		T dto = InterfaceDTOUtils.getDto(interfaceClass, true, true);
		
		int i = 0;

		// Fill fields
		while(hasMoreValues(i, iterable)){
			try {
				Field field = dtoClass.getDeclaredField(DTOClassGenerator.getFieldNameFromMethod(dtoMethods.get(i).getName()));
				Object value = getValue(i, iterable);
				if ( useConverters() ){
					IValueConverter iValueConverter = getValueConverter(field.getType());

					if ( iValueConverter != null ){
						value = iValueConverter.convertValue(value);
					}
				}
				field.set(dto, value);
				i++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dto;
	}
	
 	private boolean useConverters() {
		return useCustomConverters || useDefaultConverters;
	}

	abstract protected Object getValue(int i, I iterable);

 	abstract protected boolean hasMoreValues(int i, I iterable);

}
