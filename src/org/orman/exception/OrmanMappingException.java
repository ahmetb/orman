package org.orman.exception;


@SuppressWarnings("serial")
public class OrmanMappingException extends OrmanException {
	
	public OrmanMappingException(String message, Object... parameters){
		super(String.format(message, parameters));
	}
}
