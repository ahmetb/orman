package org.orman.mapper.exception;


@SuppressWarnings("serial")
public class NotDeclaredGetterException extends RuntimeException {
	private static String message = "There is no getter method (non-void and without arguments) declared for non-public field `%s` in %s.";
	private String f,c;
	
	public NotDeclaredGetterException(String field, String clazz){
		this.f = field;
		this.c = clazz;
	}
	
	public String getMessage() {
		return String.format(message, f, c);
	}
}
