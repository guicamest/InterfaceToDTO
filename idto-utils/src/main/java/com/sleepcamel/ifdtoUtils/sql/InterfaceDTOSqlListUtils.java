package com.sleepcamel.ifdtoUtils.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.sleepcamel.ifdtoUtils.transformer.ResultSetDTOTransformer;

public class InterfaceDTOSqlListUtils<T> extends InterfaceDTOSqlUtils<T,List<T>>{

	private Comparator<T> resultComparator;

	protected InterfaceDTOSqlListUtils(Class<T> interfaceClass, ResultSetDTOTransformer<T> transformer, Statement statement, String queryString) {
		super(interfaceClass, transformer, statement, queryString);
	}
	
	public List<T> result() throws SQLException {
		ResultSet resultSet = statement.executeQuery(queryString);
		List<T> results = new ArrayList<T>();
		while ( resultSet.next() ){
			results.add(transformer.transformToDTO(resultSet));
		}
		if (resultComparator != null){
			Collections.sort(results, resultComparator);
		}
		return results;
	}

	public InterfaceDTOSqlListUtils<T> sort(Comparator<T> resultComparator) {
		this.resultComparator = resultComparator;
		return this;
	}

	public InterfaceDTOSqlListUtils<T> list() {
		return this;
	}
}
