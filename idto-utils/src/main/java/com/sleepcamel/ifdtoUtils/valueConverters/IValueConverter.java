package com.sleepcamel.ifdtoUtils.valueConverters;

public interface IValueConverter<F,T> {

	T convertValue(F s);

}
