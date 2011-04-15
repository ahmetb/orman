package org.orman.mapper.exception;


@SuppressWarnings("serial")
public class MappingSessionAlreadyStartedException extends RuntimeException {
	@Override
	public String getMessage() {
		String message = "Mapping session has already been started. Do not use start method more than once.";
		return message;
	}
}
