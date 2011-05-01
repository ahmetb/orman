package org.orman.mapper.exception;


@SuppressWarnings("serial")
public class UnmappedDataTypeException extends RuntimeException {
	private static String message = "There is no corresponding data type found for column: %s (%s).";
	private String f,t;
	
	public UnmappedDataTypeException(String f, String t){
		this.f = f;
		this.t = t;
	}
	
	public String getMessage() {
		return String.format(message, f, t);
	}
}
