package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class DuplicateColumnNamesException extends OrmanMappingException {
	public DuplicateColumnNamesException(String s1, String s2){
		super("Unable to map more than one fields to the same column name: %s, %s.", s1, s2);
	}
}
