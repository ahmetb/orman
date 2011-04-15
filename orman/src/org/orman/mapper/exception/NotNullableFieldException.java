package org.orman.mapper.exception;


@SuppressWarnings("serial")
public class NotNullableFieldException extends RuntimeException {
	private static String message = "Trying to save instance with a null value on a @NotNull (not nullable) field: `%s` (%s)";
	private String f,c;
	
	public NotNullableFieldException(String type, String field){
		this.f = field;
		this.c = type;
	}
	
	public String getMessage() {
		return String.format(message, f, c);
	}
}
