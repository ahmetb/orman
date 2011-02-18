package org.orman.mapper.exception;

@SuppressWarnings("serial")
public class NotAnEntityException extends RuntimeException {
	private static final String message = "The following class is not eligible to be an entity. Use @Entity annotation.: %s";
	
	private String s;
	
	public NotAnEntityException(String name){
		this.s = name;
	}
	
	@Override
	public String getMessage() {
		return String.format(message, this.s);
	}
}
