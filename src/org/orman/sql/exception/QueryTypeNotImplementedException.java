package org.orman.sql.exception;

import org.orman.exception.OrmanException;

@SuppressWarnings("serial")
public class QueryTypeNotImplementedException extends OrmanException {
	public QueryTypeNotImplementedException(String type){
		super("Query not implemented in running DBMS: %s", type);
	}
}
