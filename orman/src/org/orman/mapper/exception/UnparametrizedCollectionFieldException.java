package org.orman.mapper.exception;

@SuppressWarnings("serial")
public class UnparametrizedCollectionFieldException extends RuntimeException {
	private static String message = "Unparametrized collection types (List, ArrayList, LinkedList) are not allowed as persistent field `%s` in %s. Parametrize them with classes of entity types.";
	private String s1,s2;
	
	public UnparametrizedCollectionFieldException(String s1, String s2){
		this.s1 = s1;
		this.s2 = s2;
	}
	
	public String getMessage() {
		return String.format(message, s1, s2);
	}
}
