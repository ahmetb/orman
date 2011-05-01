package org.orman.mapper.exception;


@SuppressWarnings("serial")
public class DuplicateColumnNamesException extends RuntimeException {
	private static String message = "Unable to map more than one fields to the same column name: %s, %s.";
	private String s1,s2;
	
	public DuplicateColumnNamesException(String s1, String s2){
		this.s1 = s1;
		this.s2 = s2;
	}
	
	public String getMessage() {
		return String.format(message, s1, s2);
	}
}
