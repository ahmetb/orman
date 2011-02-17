package org.orman.mapper.exception;

public class GenericOrmanException extends RuntimeException {
	private static final String message = "Unable to create index from a non-indexed field. Use @Index annotation on the following field: %s";
	
	private String s;
	
	public GenericOrmanException(String message){
		this.s = message;
	}
	
	@Override
	public String getMessage() {
		return s;
	}
}
