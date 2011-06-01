package org.orman.exception;

import org.orman.util.logging.Log;

@SuppressWarnings("serial")
public class OrmanException extends RuntimeException {

	public OrmanException(){}
	
	public OrmanException(String message, Object... parameters) {
		super(String.format(message, parameters));
	}
	
	@Override
	public String getMessage() {
		// defer logging of exception to the end.
		Log.error(super.getMessage());
		return super.getMessage();
	}

}
