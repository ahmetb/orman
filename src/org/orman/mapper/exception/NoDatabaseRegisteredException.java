package org.orman.mapper.exception;


@SuppressWarnings("serial")
public class NoDatabaseRegisteredException extends RuntimeException {
	
	public static final String message = "No database registered to the mapping session. Use registerDatabase() once.";
	@Override
	public String getMessage() {
		return message;
	}
	
}
