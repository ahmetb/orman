package org.orman.datasource.exception;

import org.orman.exception.OrmanException;

public class DatasourceConnectionException extends OrmanException {
	private static final String format = "Datasource connection error: %s"; 
	
	private String err;
	
	public DatasourceConnectionException(String err){
		this.err = err;
	}
	 
	@Override
	public String getMessage() {
		return String.format(format, this.err);
	}
}
