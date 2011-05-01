package org.orman.mapper.exception;


@SuppressWarnings("serial")
public class MappingSessionNotStartedException extends RuntimeException {
	@Override
	public String getMessage() {
		String message = "Mapping session has not been started. Please start() session before using this method.";
		return message;
	}
}
