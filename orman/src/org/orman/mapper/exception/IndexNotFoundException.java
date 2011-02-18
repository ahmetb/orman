package org.orman.mapper.exception;

@SuppressWarnings("serial")
public class IndexNotFoundException extends RuntimeException {
	private static final String message = "Unable to create index from a non-indexed field. Use @Index annotation on the following field: %s";
	
	private String s;
	
	public IndexNotFoundException(String name){
		this.s = name;
	}
	
	@Override
	public String getMessage() {
		return String.format(message, this.s);
	}
}
