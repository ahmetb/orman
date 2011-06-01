package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class UnannotatedCollectionFieldException extends OrmanMappingException {
	public UnannotatedCollectionFieldException(String s1, String s2){
		super("Unannotated non-transient collection field `%s` on %s. Annotate it with @OneToMany or @ManyToMany.", s1, s2);
	}
}
