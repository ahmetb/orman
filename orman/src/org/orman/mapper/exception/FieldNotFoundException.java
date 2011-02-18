package org.orman.mapper.exception;

@SuppressWarnings("serial")
public class FieldNotFoundException extends RuntimeException {
	private static String message = "Could not find property (field) `%s` in %s.";
	private String f,c;
	
	public FieldNotFoundException(String type, String field){
		this.f = field;
		this.c = type;
	}
	
	public String getMessage() {
		return String.format(message, f, c);
	}
}
