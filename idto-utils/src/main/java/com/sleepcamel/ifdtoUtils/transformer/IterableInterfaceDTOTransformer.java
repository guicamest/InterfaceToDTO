package com.sleepcamel.ifdtoUtils.transformer;

import java.lang.reflect.Field;

import com.sleepcamel.ifdtoUtils.InterfaceDTOUtils;
import com.sleepcamel.ifdtoUtils.valueConverters.IValueConverter;
import com.sleepcamel.ifdtoutils.DTOClassGenerator;

abstract public class IterableInterfaceDTOTransformer<T, I> extends BaseInterfaceDTOTransformer<T, I>{

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
					Class<?> type = field.getType();
					IValueConverter valueConverter = getValueConverter(type);

					if ( valueConverter != null ){
						value = valueConverter.convertValue(type, value);
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
