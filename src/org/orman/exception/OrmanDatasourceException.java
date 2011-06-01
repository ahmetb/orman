package org.orman.exception;

import org.orman.exception.OrmanDatasourceException;

@SuppressWarnings("serial")
public class OrmanDatasourceException extends OrmanException {
	public OrmanDatasourceException(String message, Object... parameters){
		super(message, parameters);
	}
}
