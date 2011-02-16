package org.orman.mapper.exception;

public class NotDeclaredIdException extends RuntimeException {
	private static String message = "The following class does not define a @Id field: %s";
	private String s1;
	
	public NotDeclaredIdException(String s1){
		this.s1 = s1;
	}
	
	public String getMessage() {
		return String.format(message, s1);
	}
}
