package com.sleepcamel.ifdtoUtils.valueConverters;

public class DoubleValueConverter implements IValueConverter<Double> {

	public static DoubleValueConverter INSTANCE = new DoubleValueConverter();
	
	private DoubleValueConverter() {}
	
	@Override
	public Double convertValue(String s) {
		return Double.parseDouble(s);
	}

}
