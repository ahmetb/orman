package org.orman.mapper.exception;

@SuppressWarnings("serial")
public class NotDeclaredDefaultConstructorException extends RuntimeException {
	private static String message = "The following class does not define a public default constructor (with no parameters): %s";
	
	private String s1;
	
	public NotDeclaredDefaultConstructorException(String s1){
		this.s1 = s1;
	}
	
	public String getMessage() {
		return String.format(message, s1);
	}
}
