package org.orman.datasource.exception;

public class QueryExecutionException extends RuntimeException {

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
