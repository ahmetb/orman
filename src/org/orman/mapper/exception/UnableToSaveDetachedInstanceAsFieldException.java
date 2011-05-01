package org.orman.mapper.exception;


@SuppressWarnings("serial")
public class UnableToSaveDetachedInstanceAsFieldException extends RuntimeException {
	private static String message = "Unable to a save non-persistent (detached) instance on field `%s` of type %s. Save it first if you have not saved yet, or made changes on it.";
	private String f,c;
	
	public UnableToSaveDetachedInstanceAsFieldException(String field, String clazz){
		this.f = field;
		this.c = clazz;
	}
	
	public String getMessage() {
		return String.format(message, f, c);
	}
}
