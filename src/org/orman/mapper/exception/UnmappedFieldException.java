package org.orman.mapper.exception;


@SuppressWarnings("serial")
public class UnmappedFieldException extends RuntimeException {
	private static String message = "No column name or data type has been generated for the following field: %s";
	private String s;
	
	public UnmappedFieldException(String s){
		this.s = s;
	}
	
	public String getMessage() {
		return String.format(message, s);
	}
}
