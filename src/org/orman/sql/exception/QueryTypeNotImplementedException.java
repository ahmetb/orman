package org.orman.sql.exception;

@SuppressWarnings("serial")
public class QueryTypeNotImplementedException extends RuntimeException {
	private final String mesg = "Query not implemented in running DBMS: %s";
	
	String type;
	
	public QueryTypeNotImplementedException(String type){
		this.type = type;
	}
	
	@Override
	public String getMessage() {
		return String.format(this.mesg, this.type);
	}
}
