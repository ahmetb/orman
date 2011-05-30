package org.orman.mapper.exception;


@SuppressWarnings("serial")
public class UnsupportedPrimaryKeyFieldTypeException extends RuntimeException {
	private static final String message = "Given type is not supported on @Id field: `%s` in %s.";
	
	private String s,t;
	
	public UnsupportedPrimaryKeyFieldTypeException(String name, String type){
		this.s = name;
		this.t = type;
	}
	
	@Override
	public String getMessage() {
		return String.format(message, this.s, this.t);
	}
}
