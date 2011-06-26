package org.orman.dbms.exception;

import org.orman.exception.OrmanDatasourceException;

@SuppressWarnings("serial")
public class DatasourceConnectionException extends OrmanDatasourceException {
	public DatasourceConnectionException(String err){
		super("Datasource connection error. Unable to establish connection to database: %s", err);
	}
}
