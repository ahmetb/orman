package org.orman.mapper.exception;

public class TooManyIdException extends RuntimeException {
	private static String message = "Too many @Id annotations used for this entity. Allowed only one: %s";
	private String s1;
	
	public TooManyIdException(String s1){
		this.s1 = s1;
	}
	
	public String getMessage() {
		return String.format(message, s1);
	}
}
