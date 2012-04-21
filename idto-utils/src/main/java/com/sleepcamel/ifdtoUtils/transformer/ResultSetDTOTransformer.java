package com.sleepcamel.ifdtoUtils.transformer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetDTOTransformer<T> extends IterableInterfaceDTOTransformer<T, ResultSet>{

	public ResultSetDTOTransformer(Class<T> interfaceClass) {
		super(interfaceClass, false, true);
	}
	
	@Override
	protected Object getValue(int i, ResultSet iterable) {
		try {
			return iterable.getObject(i+1);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected boolean hasMoreValues(int i, ResultSet iterable) {
		try {
			return i < iterable.getMetaData().getColumnCount();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
