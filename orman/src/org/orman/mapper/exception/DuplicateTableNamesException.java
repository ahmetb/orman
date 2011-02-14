package org.orman.mapper.exception;

public class DuplicateTableNamesException extends RuntimeException {
	private static String message = "Unable to map more than one entities to the same table name: %s, %s.";
	private String s1,s2;
	
	public DuplicateTableNamesException(String s1, String s2){
		this.s1 = s1;
		this.s2 = s2;
	}
	
	public String getMessage() {
		return String.format(message, s1, s2);
	}
}
