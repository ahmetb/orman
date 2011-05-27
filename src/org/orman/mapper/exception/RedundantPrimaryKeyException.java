package org.orman.mapper.exception;


@SuppressWarnings("serial")
public class RedundantPrimaryKeyException extends RuntimeException {
	private static String message = "Redundant @PrimaryKey fields found on entity %s while there is already an auto-increment exists.";
	private String s1;
	
	public RedundantPrimaryKeyException(String e){
		this.s1 = e;
	}
	
	public String getMessage() {
		return String.format(message, s1);
	}
}
