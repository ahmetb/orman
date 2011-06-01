package org.orman.datasource.exception;

import org.orman.exception.OrmanException;

@SuppressWarnings("serial")
public class DatasourceConnectionException extends OrmanException {
	public DatasourceConnectionException(String err){
		super("Datasource connection error. Unable to establish connection to database: %s", err);
	}
}
