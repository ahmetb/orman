package org.orman.mapper.exception;

public class UnsupportedIdFieldTypeException extends RuntimeException {
	private static final String message = "Given type is not supported on @Id field: %s";
	
	private String s;
	
	public UnsupportedIdFieldTypeException(String name){
		this.s = name;
	}
	
	@Override
	public String getMessage() {
		return String.format(message, this.s);
	}
}
