package com.sleepcamel.ifdtoUtils.valueConverters;

public interface IValueConverter<F,T> {

	T convertValue(Class<T> type, F s);

}
