package org.orman.dbms.exception;

import org.orman.exception.OrmanDatasourceException;

@SuppressWarnings("serial")
public class IllegalConnectionOpenCallException extends OrmanDatasourceException {
	public IllegalConnectionOpenCallException(String message) {
		super(message);
	}
}
