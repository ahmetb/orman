package org.orman.mapper.exception;

public class NotAnEntityException extends RuntimeException {
	private String message = "This class is not eligible to be an entity. Use @Entity annotation.";
	
	@Override
	public String getMessage() {
		return message;
	}
}
