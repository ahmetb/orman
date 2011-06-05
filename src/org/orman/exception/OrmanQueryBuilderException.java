package org.orman.exception;


@SuppressWarnings("serial")
public class OrmanQueryBuilderException extends OrmanException {

	public OrmanQueryBuilderException(String message, Object... parameters) {
		super(message, parameters);
	}
}
