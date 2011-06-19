package org.orman.datasource.exception;

import org.orman.exception.OrmanDatasourceException;

public class IllegalConnectionOpenCallException extends OrmanDatasourceException {
	public IllegalConnectionOpenCallException(String message) {
		super(message);
	}
}
