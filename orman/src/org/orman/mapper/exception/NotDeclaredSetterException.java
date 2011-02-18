package org.orman.mapper.exception;

@SuppressWarnings("serial")
public class NotDeclaredSetterException extends RuntimeException {
	private static String message = "There is no single-argument setter method declared for non-public field `%s` in %s.";
	private String f,c;
	
	public NotDeclaredSetterException(String field, String clazz){
		this.f = field;
		this.c = clazz;
	}
	
	public String getMessage() {
		return String.format(message, f, c);
	}
}
