package com.sleepcamel.ifdtoUtils.valueConverters;

public class IntValueConverter implements IValueConverter<String, Integer> {

	public static IntValueConverter INSTANCE = new IntValueConverter();
	
	private IntValueConverter() {}
	
	@Override
	public Integer convertValue(String s) {
		return Integer.parseInt(s);
	}

}
