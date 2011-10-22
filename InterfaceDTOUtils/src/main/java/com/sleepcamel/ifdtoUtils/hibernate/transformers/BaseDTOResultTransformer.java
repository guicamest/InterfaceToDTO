package com.sleepcamel.ifdtoUtils.hibernate.transformers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.transform.ResultTransformer;

import com.sleepcamel.ifdtoUtils.InterfaceDTOUtils;
import com.sleepcamel.ifdtoutils.DTOClassGenerator;
import com.sleepcamel.ifdtoutils.InterfaceJavaMethodsUtil;

public class BaseDTOResultTransformer<T> implements ResultTransformer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Class<T> interfaceClass;
	private Comparator<T> resultComparator;
	
	public BaseDTOResultTransformer(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}
	
	public void setComparator(Comparator<T> resultComparator) {
		this.resultComparator = resultComparator;
	}

	@Override
	public Object transformTuple(Object[] selectObjects, String[] selectAliases) {
		T dto = InterfaceDTOUtils.getDto(interfaceClass, true);
		Class<? extends Object> dtoClass = dto.getClass();
		int qty = selectObjects.length;

		// Fill fields
		int i = 0;
		List<Method> exportableMethods = InterfaceJavaMethodsUtil.instance().getExportableMethods(dtoClass, MethodPosComparator.instance());
		for(Method method:exportableMethods ){
			if ( qty == i ){
				break;
			}
			try {
				Field field = dtoClass.getDeclaredField(DTOClassGenerator.getFieldNameFromMethod(method.getName()));
				field.set(dto, selectObjects[i++]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dto;
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
