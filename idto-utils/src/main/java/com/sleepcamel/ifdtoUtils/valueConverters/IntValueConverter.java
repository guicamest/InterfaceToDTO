package com.sleepcamel.ifdtoUtils.valueConverters;

public class IntValueConverter implements IValueConverter<String, Integer> {

	public static IntValueConverter INSTANCE = new IntValueConverter();
	
	private IntValueConverter() {}
	
	@Override
	public Integer convertValue(Class<Integer> c, String s) {
		return Integer.parseInt(s);
	}

}
