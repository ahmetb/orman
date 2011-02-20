package org.orman.mapper.exception;

@SuppressWarnings("serial")
public class UnableToPersistDetachedEntityException extends RuntimeException {
	private static final String message = "Unable to persist detached (non-persistent) entity `%s`. Save the entity with insert() or update() before using it on other queries.";
	
	private String type;
	
	public UnableToPersistDetachedEntityException(String type){
		this.type = type;
	}
	
	@Override
	public String getMessage() {
		return String.format(message, this.type);
	}
}
