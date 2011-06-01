package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;

@SuppressWarnings("serial")
public class UnmappedFieldException extends OrmanMappingException {
	public UnmappedFieldException(String s){
		super("No column name or data type has been generated for the following field: %s", s);
	}
}
