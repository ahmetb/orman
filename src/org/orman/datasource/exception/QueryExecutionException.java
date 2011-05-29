package org.orman.datasource.exception;

import org.orman.exception.OrmanException;

public class QueryExecutionException extends OrmanException {

	private static final String format = "Query execution error: %s"; 
	
	private String err;
	
	public QueryExecutionException(String err){
		this.err = err;
	}
	 
	@Override
	public String getMessage() {
		return String.format(format, this.err);
	}
}
