package com.sleepcamel.ifdtoUtils.valueConverters;

public class DoubleValueConverter implements IValueConverter<String, Double> {

	public static DoubleValueConverter INSTANCE = new DoubleValueConverter();
	
	private DoubleValueConverter() {}
	
	@Override
	public Double convertValue(Class<Double> c, String s) {
		return Double.parseDouble(s);
	}

}
