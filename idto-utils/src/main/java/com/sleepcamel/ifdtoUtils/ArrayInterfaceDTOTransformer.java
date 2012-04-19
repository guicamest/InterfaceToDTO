package com.sleepcamel.ifdtoUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sleepcamel.ifdtoUtils.valueConverters.DoubleValueConverter;
import com.sleepcamel.ifdtoUtils.valueConverters.IValueConverter;
import com.sleepcamel.ifdtoUtils.valueConverters.IntValueConverter;
import com.sleepcamel.ifdtoUtils.valueConverters.LongValueConverter;
import com.sleepcamel.ifdtoutils.DTOClassGenerator;
import com.sleepcamel.ifdtoutils.methodUtil.InterfaceJavaMethodsUtil;

public class ArrayInterfaceDTOTransformer<T> {

	private Class<T> interfaceClass;
	private Class<? extends Object> dtoClass;
	protected List<Method> dtoMethods;
	private boolean useConverters;
	
	
	private Map<Class<?>, IValueConverter<?>> customValueConverters = new HashMap<Class<?>, IValueConverter<?>>();
	private boolean trimIfString;
	
	static protected Map<Class<?>, IValueConverter<?>> valueConverters = new HashMap<Class<?>, IValueConverter<?>>();
	static{
		valueConverters.put(int.class, IntValueConverter.INSTANCE);
		valueConverters.put(Integer.class, IntValueConverter.INSTANCE);
		valueConverters.put(long.class, LongValueConverter.INSTANCE);
		valueConverters.put(Long.class, LongValueConverter.INSTANCE);
		valueConverters.put(double.class, DoubleValueConverter.INSTANCE);
		valueConverters.put(Double.class, DoubleValueConverter.INSTANCE);
	}
	
	public ArrayInterfaceDTOTransformer(Class<T> interfaceClass) {
		this(interfaceClass, false);
	}
	
	public ArrayInterfaceDTOTransformer(Class<T> interfaceClass, boolean useConverters) {
		this(interfaceClass, useConverters, false);
	}
	
	public ArrayInterfaceDTOTransformer(Class<T> interfaceClass, boolean useConverters, boolean trimIfString) {
		this.interfaceClass = interfaceClass;
		this.useConverters = useConverters;
		this.trimIfString = trimIfString;
		T dto = InterfaceDTOUtils.getDto(interfaceClass, true, true);
		dtoClass = dto.getClass();
		dtoMethods = InterfaceJavaMethodsUtil.instance().getExportableMethods(dtoClass, MethodPosComparator.instance());
	}
	
	public void addCustomValueConverter(Class<?> clazz, IValueConverter<?> valueConverter){
		customValueConverters.put(clazz, valueConverter);
	}
	
	public T transformArrayToDTO(Object[] arrayOfValues) {
		T dto = InterfaceDTOUtils.getDto(interfaceClass, true, true);
		int qty = arrayOfValues.length;

		// Fill fields
		for(int i = 0; i < qty; i++){
			try {
				Field field = dtoClass.getDeclaredField(DTOClassGenerator.getFieldNameFromMethod(dtoMethods.get(i).getName()));
				Object value = arrayOfValues[i];
				if ( useConverters ){
					if ( value != null && value.getClass().equals(String.class) && trimIfString ){
						value = ((String)value).trim();
					}

					IValueConverter<?> iValueConverter = getValueConverter(field.getType());

					if ( iValueConverter != null ){
						value = iValueConverter.convertValue((String) value);
					}
				}
				field.set(dto, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dto;
	}
	
	public List<Method> getDtoMethods() {
		return dtoMethods;
	}

	private IValueConverter<?> getValueConverter(Class<?> type) {
		IValueConverter<?> iValueConverter = customValueConverters.get(type);
		if ( iValueConverter == null ){
			iValueConverter = valueConverters.get(type);
		}
		return iValueConverter;
	}
}
