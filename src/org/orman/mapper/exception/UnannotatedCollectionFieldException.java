package org.orman.mapper.exception;


@SuppressWarnings("serial")
public class UnannotatedCollectionFieldException extends RuntimeException {
	private static String message = "Unannotated non-transient collection field `%s` on %s. Annotate it with @OneToMany or @ManyToMany.";
	private String s1,s2;
	
	public UnannotatedCollectionFieldException(String s1, String s2){
		this.s1 = s1;
		this.s2 = s2;
	}
	
	public String getMessage() {
		return String.format(message, s1, s2);
	}
}
