package org.orman.mapper.exception;

@SuppressWarnings("serial")
public class GenericOrmanException extends RuntimeException {
	private String s;
	
	public GenericOrmanException(String message){
		this.s = message;
	}
	
	@Override
	public String getMessage() {
		return s;
	}
}
