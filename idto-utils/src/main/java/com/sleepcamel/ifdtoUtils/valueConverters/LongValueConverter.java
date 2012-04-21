package com.sleepcamel.ifdtoUtils.valueConverters;

public class LongValueConverter implements IValueConverter<String, Long> {

	public static LongValueConverter INSTANCE = new LongValueConverter();
	
	private LongValueConverter() {}
	
	@Override
	public Long convertValue(String s) {
		return Long.parseLong(s);
	}

}
