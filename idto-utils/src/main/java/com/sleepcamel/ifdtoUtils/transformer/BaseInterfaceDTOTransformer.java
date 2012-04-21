package com.sleepcamel.ifdtoUtils.transformer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sleepcamel.ifdtoUtils.InterfaceDTOUtils;
import com.sleepcamel.ifdtoUtils.MethodPosComparator;
import com.sleepcamel.ifdtoUtils.valueConverters.IValueConverter;
import com.sleepcamel.ifdtoutils.methodUtil.InterfaceJavaMethodsUtil;

abstract public class BaseInterfaceDTOTransformer<T, RV> {

	protected Class<T> interfaceClass;
	protected Class<? extends Object> dtoClass;
	protected List<Method> dtoMethods;
	protected boolean useDefaultConverters;
	protected boolean useCustomConverters;
	
	private Map<Class<?>, IValueConverter<?,?>> customValueConverters = new HashMap<Class<?>, IValueConverter<?,?>>();
	
	static protected Map<Class<?>, IValueConverter<?,?>> valueConverters = new HashMap<Class<?>, IValueConverter<?,?>>();
	
	public BaseInterfaceDTOTransformer(Class<T> interfaceClass) {
		this(interfaceClass, false, false);
	}
	
	public BaseInterfaceDTOTransformer(Class<T> interfaceClass, boolean useDefaultConverters, boolean useCustomConverters) {
		this.interfaceClass = interfaceClass;
		this.useDefaultConverters = useDefaultConverters;
		this.useCustomConverters = useCustomConverters;
		T dto = InterfaceDTOUtils.getDto(interfaceClass, true, true);
		dtoClass = dto.getClass();
		dtoMethods = InterfaceJavaMethodsUtil.instance().getExportableMethods(dtoClass, MethodPosComparator.instance());
	}
	
	public void addCustomValueConverter(Class<?> clazz, IValueConverter<?,?> valueConverter){
		customValueConverters.put(clazz, valueConverter);
	}

	abstract public T transformToDTO(RV arrayOfValues);

	public List<Method> getDtoMethods() {
		return dtoMethods;
	}

	protected IValueConverter<?,?> getValueConverter(Class<?> type) {
		IValueConverter<?,?> iValueConverter = null;
		if ( useCustomConverters ){
			iValueConverter = customValueConverters.get(type);
		}
		if ( iValueConverter == null && useDefaultConverters ){
			iValueConverter = valueConverters.get(type);
		}
		return iValueConverter;
	}
}
