package org.orman.mapper.exception;

public class MappingSessionAlreadyStartedException extends RuntimeException {
	@Override
	public String getMessage() {
		return "Mapping session has already been started. Do not use start method more than once.";
	}
}
