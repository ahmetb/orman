package org.orman.mapper.exception;


@SuppressWarnings("serial")
public class NotDeclaredIdException extends RuntimeException {
	private static String message = "The following class does not define a @PrimaryKey(autoIncrement=true) field: %s."  +
			"It should define an autoincrement if it is a type of a field in some other Entity.";
	private String s1;
	
	public NotDeclaredIdException(String s1){
		this.s1 = s1;
	}
	
	public String getMessage() {
		return String.format(message, s1);
	}
}
