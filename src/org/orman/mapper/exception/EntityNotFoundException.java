package org.orman.mapper.exception;


@SuppressWarnings("serial")
public class EntityNotFoundException extends RuntimeException {
	private static final String message = "The entity type `%s` could not be found in MappingSession context. Register this @Entity to MappingSession first.";
	
	private String type;
	
	public EntityNotFoundException(String type){
		this.type = type;
	}
	
	@Override
	public String getMessage() {
		return String.format(message, this.type);
	}
}
