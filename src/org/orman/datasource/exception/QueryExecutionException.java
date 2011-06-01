package org.orman.datasource.exception;

import org.orman.exception.OrmanException;

@SuppressWarnings("serial")
public class QueryExecutionException extends OrmanException {

	public QueryExecutionException(String err){
		super("Query execution error: %s", err);
	}
}
