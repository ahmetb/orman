package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class DuplicateTableNamesException extends OrmanMappingException {
	public DuplicateTableNamesException(String s1, String s2){
		super("Unable to map more than one entities to the same table name: %s, %s.", s1, s2);
	}
}
