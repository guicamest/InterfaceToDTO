package com.sleepcamel.ifdtoUtils.valueConverters;

import java.lang.reflect.Method;


@SuppressWarnings("rawtypes")
public class EnumValueConverter implements IValueConverter<String, Enum> {

	public static EnumValueConverter INSTANCE = new EnumValueConverter();
	
	private EnumValueConverter() {}
	
	@Override
	public Enum convertValue(Class<Enum> type, String s) {
		Method m;
		try {
			m = type.getDeclaredMethod("valueOf", String.class);
		m.setAccessible(true); //if security settings allow this
		return (Enum) m.invoke(null, s); //use null if the method is static
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
