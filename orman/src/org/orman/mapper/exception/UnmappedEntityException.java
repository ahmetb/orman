package org.orman.mapper.exception;

public class UnmappedEntityException extends RuntimeException {
	private static String message = "No table name has been generated for the following entity: %s";
	private String s;
	
	public UnmappedEntityException(String s){
		this.s = s;
	}
	
	public String getMessage() {
		return String.format(message, s);
	}
}
