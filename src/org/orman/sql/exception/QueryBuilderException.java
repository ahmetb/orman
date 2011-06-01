package org.orman.sql.exception;

import org.orman.exception.OrmanException;

@SuppressWarnings("serial")
public class QueryBuilderException extends OrmanException {

	public QueryBuilderException(String message, Object... parameters) {
		super(message, parameters);
	}
}
