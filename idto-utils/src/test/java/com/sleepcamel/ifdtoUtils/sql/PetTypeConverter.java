package com.sleepcamel.ifdtoUtils.sql;

import com.sleepcamel.ifdtoUtils.valueConverters.IValueConverter;

public class PetTypeConverter implements IValueConverter<String, PetType> {

	public static PetTypeConverter INSTANCE = new PetTypeConverter();
	
	private PetTypeConverter() {}

	@Override
	public PetType convertValue(String s) {
		return PetType.valueOf(s);
	}
	
}
