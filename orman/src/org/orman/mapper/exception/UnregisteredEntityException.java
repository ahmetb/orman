package org.orman.mapper.exception;

@SuppressWarnings("serial")
public class UnregisteredEntityException extends RuntimeException {

	private static final String MESSAGE = "Unable to retrieve Entity of an unregistered class. Register the following class to MappingSession as Entity first: %s.";
	private String clazz;
	
	public UnregisteredEntityException(String cl){
		this.clazz = cl;
	}
	
	@Override
	public String getMessage() {
		return String.format(MESSAGE, this.clazz);
	}
}
