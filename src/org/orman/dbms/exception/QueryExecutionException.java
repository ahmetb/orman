package org.orman.dbms.exception;

import org.orman.exception.OrmanDatasourceException;

@SuppressWarnings("serial")
public class QueryExecutionException extends OrmanDatasourceException {

	public QueryExecutionException(String err){
		super("Query execution error: %s", err);
	}
}
