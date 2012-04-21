package com.sleepcamel.ifdtoUtils.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sleepcamel.ifdtoUtils.transformer.ResultSetDTOTransformer;
import com.sleepcamel.ifdtoUtils.valueConverters.IValueConverter;


public class InterfaceDTOSqlUtils<T,RT> {

	public static <T> InterfaceDTOSqlUtils<T,T> getFor(Class<T> interfaceClass) {
		return new InterfaceDTOSqlUtils<T,T>(interfaceClass);
	}

	protected Class<T> interfaceClass;
	protected ResultSetDTOTransformer<T> transformer;
	protected Statement statement;
	protected String queryString;

	protected InterfaceDTOSqlUtils(Class<T> interfaceClass) {
		this(interfaceClass, new ResultSetDTOTransformer<T>(interfaceClass), null, "");
	}
	
	protected InterfaceDTOSqlUtils(Class<T> interfaceClass, ResultSetDTOTransformer<T> transformer, Statement statement, String queryString) {
		this.interfaceClass = interfaceClass;
		this.transformer = transformer;
		this.statement = statement;
		this.queryString = queryString;
	}

	public <E> InterfaceDTOSqlUtils<T,RT> converter(Class<E> clazz, IValueConverter<String, E> valueConverter) {
		transformer.addCustomValueConverter(clazz, valueConverter);
		return this;
	}

	public InterfaceDTOSqlUtils<T,RT> fromQuery(String queryString) {
		this.queryString = queryString;
		return this;
	}
	
	public InterfaceDTOSqlUtils<T,RT> withStatement(Statement statement) {
		this.statement = statement;
		return this;
	}

	public InterfaceDTOSqlUtils<T,RT> withConnection(Connection connection) throws SQLException {
		return withStatement(connection.createStatement());
	}

	@SuppressWarnings("unchecked")
	public RT result() throws SQLException {
		ResultSet resultSet = statement.executeQuery(queryString);
		if ( !resultSet.next() )
			return null;
		return (RT) transformer.transformToDTO(resultSet);
	}
	
	public InterfaceDTOSqlListUtils<T> list() {
		return new InterfaceDTOSqlListUtils<T>(interfaceClass, transformer, statement, queryString);
	}
	
	public <E> InterfaceDTOSqlMapUtils<T,E,T> map(Class<E> keyClass, int position) {
		return new InterfaceDTOSqlMapUtils<T,E,T>(interfaceClass, transformer, statement, queryString, getListFor(keyClass, position));
	}
	
	public <E> InterfaceDTOSqlMapUtils<T,E,List<T>> mapList(Class<E> keyClass, int position) {
		return new InterfaceDTOSqlMapUtils<T,E,List<T>>(interfaceClass, transformer, statement, queryString, getListFor(keyClass, position), true);
	}

	private List<Map.Entry<Integer,Class<?>>> getListFor(Class<?> keyClass, int position) {
		List<Map.Entry<Integer,Class<?>>> list = new ArrayList<Map.Entry<Integer,Class<?>>>();
		addKeyToList(list, keyClass, position);
		return list;
	}
	
	protected void addKeyToList(List<Map.Entry<Integer,Class<?>>> list, Class<?> keyClass, int position) {
		list.add(0, new AbstractMap.SimpleEntry<Integer,Class<?>>(position, keyClass));
	}

}
