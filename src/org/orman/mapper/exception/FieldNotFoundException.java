package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class FieldNotFoundException extends OrmanMappingException {
	public FieldNotFoundException(String type, String field){
		super("Could not find property (field) `%s` in %s.", field, type);
	}
}
